package service.impl;

import com.github.pagehelper.PageHelper;
import com.xsl.search.export.SearchResource;
import com.xsl.search.export.vo.TaskInfoVo;
import com.xsl.search.export.vo.TaskSearchReqVo;
import com.xsl.task.vo.*;
import com.xsl.task.vo.ResBaseVo;
import com.xsl.task.vo.TagVo;
import com.xsl.user.LevelResource;
import com.xsl.user.UserInfoResouce;
import com.xsl.user.vo.*;
import mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import service.*;
import util.*;
import vo.CreateOrderReqVo;
import vo.JPushVo;
import vo.XslTagVo;
import xsl.pojo.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 梁俊伟
 * @version 1.0
 * @date 2019/5/27 20:53
 */
@Service
public class TaskOperateServiceImpl implements TaskOperateService {

    private static final Logger logger = LoggerFactory.getLogger(TaskOperateServiceImpl.class);

    @Autowired
    private XslTaskMapper xslTaskMapper;
    @Autowired
    private XslTaskTagMapper xslTaskTagMapper;
    @Autowired
    private XslTaskFileMapper xslTaskFileMapper;
    @Autowired
    private XslTagMapper xslTagMapper;
    @Autowired
    private XslUserMapper xslUserMapper;
    @Autowired
    private XslNetworkMapper xslNetworkMapper;
    @Autowired
    private XslHunterMapper xslHunterMapper;
    @Autowired
    private XslHunterTaskMapper xslHunterTaskMapper;
    @Autowired
    private XslSchoolTaskMapper xslSchoolTaskMapper;

    @Resource
    private LevelResource levelResource;

    @Resource
    private UserInfoResouce userInfoResouce;

    @Autowired
    private HunterRecommendService hunterRecommendService;
    @Autowired
    private JpushService jpushService;
    @Autowired
    private TagService tagService;
    @Autowired
    private TaskMqService taskMqService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private CommonUseService commonUseService;

    @Resource
    private SearchResource searchResource;



    @Value("${REDIS_USER_SESSION_KEY}")
    private String REDIS_USER_SESSION_KEY;
    @Value("${USER_INFO}")
    private String USER_INFO;
    @Value("${USER_HUNTER_INFO}")
    private String USER_HUNTER_INFO;
    @Value("${USER_MASTER_INFO}")
    private String USER_MASTER_INFO;

    @Override
    public ResBaseVo sendTask(TaskReqVo taskReqVo) {
        logger.info("sendTask param:{}", GsonSingle.getGson().toJson(taskReqVo));
        try {
            if (StringUtils.isEmpty(taskReqVo.getContent())) {
                return ResBaseVo.build(400, "参数错误");
            }
            //文字扫描屏蔽
            Map<String, String> map = new HashMap<>(1);
            map.put("sentence", taskReqVo.getContent());
            if (taskReqVo.getContent().contains("代课") || taskReqVo.getContent().contains("车队")) {
                return ResBaseVo.build(403, "悬赏任务不合法");
            }

//			String result = HttpClientUtil.doGet("http://47.93.19.164:8080/xsl-search-service/search/wordcheck", map);
//			XslResultOk fcResult = XslResultOk.format(result);
//			List<String> data = (List<String>) fcResult.getData();
//			if (data != null && data.size() > 0) {
//				return XslResult.build(400, "悬赏任务不合法");
//			}

            //设置任务分类--默认全种类
            XslTask xslTask = new XslTask();
            xslTask.setCid(1);
            xslTask.setSendid(taskReqVo.getMasterId());
            xslTask.setTaskid(UUIDUtil.getUUID());
            xslTask.setContent(taskReqVo.getContent());
            xslTask.setMoney(taskReqVo.getMoney());
            xslTask.setTasktitle(taskReqVo.getTaskTitle());
            xslTask.setCreatedate(DateUtils.stringToDate(taskReqVo.getCreateDate()));
            xslTask.setUpdatedate(DateUtils.stringToDate(taskReqVo.getCreateDate()));
            xslTask.setDeadline(DateUtils.stringToDate(taskReqVo.getDeadLineDate()));
            xslTask.setSourcetype(taskReqVo.getSourceType());
            //未启动推荐
            xslTask.setState((byte) 0);

            if (taskReqVo.getIsRecommend() == null) {
                return ResBaseVo.build(400, "参数错误");
            }

            //启动推荐
            if (taskReqVo.getIsRecommend()) {
                xslTask.setState((byte) 1);
            }

            //记录任务
            int insert = xslTaskMapper.insertSelective(xslTask);

            if (insert < 1) {
                return ResBaseVo.build(500, "服务器异常");
            }

            XslResult xslResultTag = addTaskTag(taskReqVo, xslTask.getTaskid());
            XslResult xslResultFile = addTaskFile(taskReqVo, xslTask.getTaskid());
            XslResult xslResultSchool = addSchoolTask(taskReqVo, xslTask.getTaskid());

            if (xslResultFile.isOK() && xslResultTag.isOK() && xslResultSchool.isOK()) {
                //异步启动推荐
                if (taskReqVo.getIsRecommend()) {
                    taskExecutor.execute(() -> hunterRecommendAndPush(xslTask));
                }

                UserLevelReqVo userLevelReqVo = new UserLevelReqVo();
                userLevelReqVo.setMasterId(taskReqVo.getMasterId());
                logger.info("UserLevelReqVo:" + GsonSingle.getGson().toJson(userLevelReqVo));
                //异步更新雇主信息
                taskExecutor.execute(() -> levelResource.AddEmpirical(userLevelReqVo));

                //异步封装数据发送mq到搜索系统
                taskExecutor.execute(() -> sendTaskInfoToSearch(xslTask));

                return ResBaseVo.ok(xslTask.getTaskid());
            }

            return ResBaseVo.build(500, "服务器异常");
        } catch (Exception e) {
            e.printStackTrace();
            return ResBaseVo.build(500, "服务器异常");
        }
    }

    @Override
    public ResBaseVo receiveTask(RecTaskReqVo recTaskReqVo) {
        String hunterid = recTaskReqVo.getHunterid();
        String taskid = recTaskReqVo.getTaskId();
        //1.判断用户状态
        UserVo userInfo = userInfoResouce.getUserInfoByHunterId(hunterid);
        if(userInfo == null){
            return ResBaseVo.build(403, "您无权操作");
        }
        if(1 != userInfo.getState()){
            return ResBaseVo.build(403, "您无权操作");
        }

        //2.获取任务信息
        XslTaskExample xslTaskExample = new XslTaskExample();
        XslTaskExample.Criteria criteria = xslTaskExample.createCriteria();
        criteria.andTaskidEqualTo(taskid);
        List<XslTask> taskList = xslTaskMapper.selectByExample(xslTaskExample);
        if(taskList == null || taskList.size() < 1){
            return ResBaseVo.build(500, "任务信息异常");
        }

        XslTask xslTask = taskList.get(0);

        String masterid = userInfo.getMasterid();

        if(masterid.equals(xslTask.getSendid())){
            return ResBaseVo.build(403, "请不要接自己发送的任务");
        }
        Byte state = xslTask.getState();
        if(!(0 == state || 1 == state)){
            return ResBaseVo.build(403, "任务已经被抢走");
        }

        xslTask.setState((byte) 2);
        if(state == 0){
            criteria.andStateEqualTo((byte) 0);
        }
        if(state == 1){
            criteria.andStateEqualTo((byte) 1);
        }
        int i = xslTaskMapper.updateByExampleSelective(xslTask, xslTaskExample);

        if(i < 1){
            return ResBaseVo.build(403, "任务接收失败");
        }

        XslHunterTask xslHunterTask = new XslHunterTask();
        xslHunterTask.setHunterid(hunterid);
        xslHunterTask.setTaskid(recTaskReqVo.getTaskId());
        xslHunterTask.setTaskstate((byte) 2);
        int count = xslHunterTaskMapper.insertSelective(xslHunterTask);
        if(count < 1){
            return ResBaseVo.build(403, "请不要接自己发送的任务");
        }

        //异步生成订单
        taskExecutor.execute(() ->createOrder(hunterid, taskid));

        //异步给雇主发推送
        String masterId = xslTask.getSendid();
        UserVo userInfoMasterId = userInfoResouce.getUserInfoMasterId(masterId);
        JPushVo jPushVo = new JPushVo();
        jPushVo.setMsgTitle("任务状态提醒");
        jPushVo.setMsgContent("你发布的任务《"+xslTask.getTasktitle()+"》已被接");
        jPushVo.setNotificationTitle("你发布的任务《"+xslTask.getTasktitle()+"》已被接");
        jPushVo.setExtrasparam(xslTask.getTaskid());
        sendToMessage(jPushVo, userInfoMasterId.getPhone());

        taskExecutor.execute(()-> updateEsTaskInfo(xslTask));
        taskExecutor.execute(() -> updateNetwork(hunterid, masterId));

        return ResBaseVo.ok();
    }

    private void createOrder(String hunterId, String taskId){
        CreateOrderReqVo createOrderReqVo=new CreateOrderReqVo();
        createOrderReqVo.setHunterId(hunterId);
        createOrderReqVo.setTaskId(taskId);
        taskMqService.createOrder(createOrderReqVo);
    }
    private void updateEsTaskInfo(XslTask xslTask){
        UpdateTaskVo updateTaskVo = new UpdateTaskVo();
        updateTaskVo.setState(xslTask.getState());
        updateTaskVo.setUpdatedate(DateUtils.getDateTimeToString(new Date()));
        updateTaskVo.setTaskId(xslTask.getTaskid());
        taskMqService.updateEsTask(updateTaskVo);
    }

    private void updateNetwork(String hunterId, String masterId){
        NetworkReqVo networkReqVo = new NetworkReqVo();
        networkReqVo.setHunterId(hunterId);
        networkReqVo.setMasterId(masterId);

        String s = GsonSingle.getGson().toJson(networkReqVo);
        taskMqService.updateNetwork(s);
    }

    @Override
    public ResBaseVo confirmTask(ConfirmTaskReqVo confirmTaskReqVo) {
        Byte nowState = confirmTaskReqVo.getNowState();
        Byte afterState = confirmTaskReqVo.getAfterState();
        String taskId = confirmTaskReqVo.getTaskId();
        String hunterId = confirmTaskReqVo.getHunterid();
        //检测任务状态
        XslTaskExample xslTaskExample = new XslTaskExample();
        xslTaskExample.createCriteria().andTaskidEqualTo(taskId);
        List<XslTask> taskList = xslTaskMapper.selectByExample(xslTaskExample);

        if(taskList == null || taskList.size() == 0){
            return ResBaseVo.build(403, "任务不存在");
        }
        XslTask xslTask = taskList.get(0);
        if(nowState != xslTask.getState()){
            return ResBaseVo.build(403, "请勿重复操作");
        }

        //检测连接状态
        XslHunterTaskExample xslHunterTaskExample = new XslHunterTaskExample();
        xslHunterTaskExample.createCriteria().andHunteridEqualTo(hunterId).andTaskidEqualTo(taskId);
        List<XslHunterTask> xslHunterTasks = xslHunterTaskMapper.selectByExample(xslHunterTaskExample);

        if(xslHunterTasks == null || xslHunterTasks.size() == 0){
            return ResBaseVo.build(403, "猎人信息有误");
        }
        XslHunterTask xslHunterTask = xslHunterTasks.get(0);
        if(nowState != xslHunterTask.getTaskstate()){
            return ResBaseVo.build(403, "请勿重复操作");
        }

        //更新任务状态
        xslTask.setState(afterState);
        int i = xslTaskMapper.updateByExampleSelective(xslTask, xslTaskExample);
        if(i < 1){
            return ResBaseVo.build(500, "服务器异常");
        }

        //更新连接状态
        xslHunterTask.setTaskstate(afterState);
        int i1 = xslHunterTaskMapper.updateByExampleSelective(xslHunterTask, xslHunterTaskExample);
        if(i1 < 1){
            return ResBaseVo.build(500, "服务器异常");
        }

        //猎人确认完成
        if(4 == afterState){
            //异步给雇主发推送
            String masterId = xslTask.getSendid();
            UserVo userInfoMasterId = userInfoResouce.getUserInfoMasterId(masterId);
            JPushVo jPushVo = new JPushVo();
            jPushVo.setMsgTitle("任务完成提醒");
            jPushVo.setMsgContent("你发布的任务《"+xslTask.getTasktitle()+"》已完成");
            jPushVo.setNotificationTitle("你发布的任务《"+xslTask.getTasktitle()+"》已完成");
            jPushVo.setExtrasparam(taskId);
            sendToMessage(jPushVo, userInfoMasterId.getPhone());
        }

        //雇主确认完成
        if(3 == afterState){
            UserLevelReqVo userLevelReqVo = new UserLevelReqVo();
            userLevelReqVo.setMasterId(confirmTaskReqVo.getHunterid());
            //异步增加经验
            taskExecutor.execute(() -> levelResource.AddEmpirical(userLevelReqVo));

            //给猎人发推送
            UserVo userInfoMasterId = userInfoResouce.getUserInfoByHunterId(hunterId);
            JPushVo jPushVo = new JPushVo();
            jPushVo.setMsgTitle("任务完成提醒");
            jPushVo.setMsgContent("你接收的任务《"+xslTask.getTasktitle()+"》雇主已完成确认");
            jPushVo.setNotificationTitle("你接收的任务《"+xslTask.getTasktitle()+"》雇主已完成确认");
            jPushVo.setExtrasparam(taskId);
            sendToMessage(jPushVo, userInfoMasterId.getPhone());

            //任务终结处理订单
        }
        HunterInfo hunterInfo = commonUseService.getHunterInfo(hunterId);

        //异步更新搜索库状态
        UpdateTaskVo updateTaskVo = new UpdateTaskVo();
        updateTaskVo.setState(nowState);
        updateTaskVo.setUpdatedate(DateUtils.getDateTimeToString(new Date()));
        updateTaskVo.setTaskId(xslTask.getTaskid());
        taskExecutor.execute(()-> taskMqService.updateEsTask(updateTaskVo));

        return ResBaseVo.ok(hunterInfo);
    }

    @Override
    public SearchTaskInfoListResVo searchTask(SearchTaskReqVo searchTaskReqVo) {
        String schoolName = searchTaskReqVo.getSchoolName();
        int size = searchTaskReqVo.getSize();
        int idSize = 1000;
        List<String> schoolTaskIds = getSchoolTaskIds(schoolName, idSize);
        TaskSearchReqVo taskSearchReqVo = new TaskSearchReqVo();
        taskSearchReqVo.setKeyword(searchTaskReqVo.getKeyword());
        taskSearchReqVo.setSize(size);
        taskSearchReqVo.setTaskIds(schoolTaskIds);
        List<TaskInfoVo> taskInfoVos = searchResource.searchTask(taskSearchReqVo);
        SearchTaskInfoListResVo searchTaskInfoListResVo = new SearchTaskInfoListResVo();
        searchTaskInfoListResVo.setStatus(200);
        searchTaskInfoListResVo.setMsg("成功");
        if(!ListUtil.isNotEmpty(taskInfoVos)){
            return searchTaskInfoListResVo;
        }

        List<TaskInfo> taskInfos = new ArrayList<>();
        for (TaskInfoVo taskInfoVo : taskInfoVos){
            TaskInfo taskInfo = new TaskInfo();
            BeanUtils.copyProperties(taskInfoVo, taskInfo);
            String taskid = taskInfoVo.getTaskId();
            List<XslTag> taskTags = tagService.getTaskTags(taskid);

            List<XslTagVo> XslTagVos = new ArrayList<>();
            if(ListUtil.isNotEmpty(taskTags)){
                for (XslTag xslTag : taskTags){
                    XslTagVo XslTagVo = new XslTagVo();
                    XslTagVo.setTagName(xslTag.getName());
                    XslTagVo.setTagid(xslTag.getTagid());
                    XslTagVos.add(XslTagVo);
                }

            }
            List<TagVo> tags = GsonUtil.gsonToList(GsonUtil.gsonString(XslTagVos),TagVo.class);
            taskInfo.setTags(tags);
            taskInfos.add(taskInfo);
        }
        searchTaskInfoListResVo.setTaskInfoList(taskInfos);
        return searchTaskInfoListResVo;
    }

    @Override
    public ResBaseVo cancelTask(String taskId) {
        //检测任务状态
        XslTaskExample xslTaskExample = new XslTaskExample();
        xslTaskExample.createCriteria().andTaskidEqualTo(taskId);
        List<XslTask> taskList = xslTaskMapper.selectByExample(xslTaskExample);

        if(taskList == null || taskList.size() == 0){
            return ResBaseVo.build(403, "任务不存在");
        }
        XslTask xslTask = taskList.get(0);
        if(0 != xslTask.getState() && 1 != xslTask.getState()){
            return ResBaseVo.build(403, "任务已被接，无法取消");
        }

        //更新任务状态
        xslTask.setState((byte) -2);
        int i = xslTaskMapper.updateByExampleSelective(xslTask, xslTaskExample);
        if(i < 1){
            return ResBaseVo.build(500, "服务器异常");
        }

        taskExecutor.execute(()-> updateEsTaskInfo(xslTask));

        return ResBaseVo.ok();
    }

    private List<String> getSchoolTaskIds(String schoolName, Integer size){
        SchoolVo school = userInfoResouce.getSchoolByName(schoolName);
        if(school == null){
            return new ArrayList<>();
        }
        Integer schoolId = school.getId();

        //2.获取学校id对应的任务
        PageHelper.startPage(1, size);
        List<String> taskIds = xslSchoolTaskMapper.selectTaskIdBySchoolId(schoolId);
        if(taskIds.size() == 0){
            return new ArrayList<>();
        }

        return taskIds;
    }

    /**
     * 添加任务标签关系数据
     */
    private XslResult addTaskTag(TaskReqVo taskReqVo, String taskId) {
        try {
            List<TagVo> tags = taskReqVo.getTags();

            if(tags.size() < 1){
                return XslResult.ok();
            }

            List<XslTaskTag> xslTaskTags = new ArrayList<>();
            for (TagVo tagVo : tags){
                XslTaskTag xslTaskTag = new XslTaskTag();
                xslTaskTag.setTaskid(taskId);
                xslTaskTag.setTagid(tagVo.getTagid());
                xslTaskTags.add(xslTaskTag);
            }

            int i = xslTaskTagMapper.insertSelectiveBatch(xslTaskTags);
            if(i < xslTaskTags.size()){
                throw new RuntimeException();
            }

            //异步去处理标签使用的次数
            taskExecutor.execute(() -> updateTagNum(taskReqVo.getTags()));

        } catch (Exception e) {
            e.printStackTrace();
            return XslResult.build(500, "服务器异常");
        }

        return XslResult.ok();
    }

    private XslResult updateTagNum(List<TagVo> tags){
        List<String> tagIds = new ArrayList<>(tags.size());
        for (TagVo tagVo : tags){
            tagIds.add(tagVo.getTagid());
        }

        XslTagExample xslTagExample = new XslTagExample();
        XslTagExample.Criteria criteria = xslTagExample.createCriteria();
        criteria.andTagidIn(tagIds);
        int i = xslTagMapper.updateUseNumByExample(xslTagExample);
        if(i < 1){
            throw new RuntimeException();
        }
        return XslResult.ok();
    }

    private XslResult addTaskFile(TaskReqVo taskReqVo, String taskId) {
        try {
            List<ImageVo> images = taskReqVo.getImages();
            if(images.size() < 1){
                return XslResult.ok();
            }

            List<XslTaskFile> xslTaskFiles = new ArrayList<>();
            for (ImageVo imageVo : images){
                XslTaskFile xslTaskFile = new XslTaskFile();
                xslTaskFile.setTaskid(taskId);
                xslTaskFile.setFileid(imageVo.getImageId());
                xslTaskFiles.add(xslTaskFile);
            }

            int i = xslTaskFileMapper.insertSelectiveBatch(xslTaskFiles);
            if(i < xslTaskFiles.size()){
                throw new RuntimeException();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return XslResult.build(500, "服务器异常");
        }
        return XslResult.ok();
    }

    private XslResult addSchoolTask(TaskReqVo taskReqVo, String taskid) {
        UserVo userVo = userInfoResouce.getUserInfoMasterId(taskReqVo.getMasterId());
        String schoolinfo = userVo.getSchoolinfo();
        SchoolinfoVo schoolInfo = userInfoResouce.getSchoolInfo(schoolinfo);
        String schoolName = schoolInfo.getSchool();
        SchoolVo school = userInfoResouce.getSchoolByName(schoolName);
        XslSchoolTask xslSchoolTask = new XslSchoolTask();
        xslSchoolTask.setSchoolid(school.getId());
        xslSchoolTask.setTaskid(taskid);

        int insert = xslSchoolTaskMapper.insertSelective(xslSchoolTask);

        if(insert < 1){
            throw new RuntimeException("任务学校信息关联异常");
        }
        return XslResult.ok();
    }

    private XslResult hunterRecommendAndPush(XslTask xslTask){

        List<String> recommend;
        //猎人标签推优算法
        recommend = hunterRecommendService.recommend(xslTask.getTaskid(), 10);

        if(recommend == null || recommend.size() == 0){
            //血缘关系推荐算法启动
            Set<String> hunters = networkHunter(xslTask);

            recommend.addAll(hunters);

            if(hunters.size() == 0){
                recommend = getGoodHunter();
            }

        }

        JPushVo jPushVo = new JPushVo();
        jPushVo.setMsgTitle("悬赏任务推荐");
        jPushVo.setMsgContent("有一个适合你的悬赏任务");
        jPushVo.setNotificationTitle("有一个适合你的悬赏任务");
        jPushVo.setExtrasparam(xslTask.getTaskid());

        String myHunterId = userInfoResouce.getUserInfoMasterId(xslTask.getSendid()).getHunterid();

        for (String hunterId : recommend){
            if(hunterId.equals(myHunterId)){
                continue;
            }
            //查电话号码
            XslUserExample xslUserExample = new XslUserExample();
            xslUserExample.createCriteria().andHunteridEqualTo(hunterId);
            List<XslUser> xslUsers = xslUserMapper.selectByExample(xslUserExample);
            if(xslTask != null && xslUsers.size() > 0){
                String phone = xslUsers.get(0).getPhone();
                sendToMessage(jPushVo, phone);
            }
        }

        return XslResult.ok();
    }

    private void sendTaskInfoToSearch(XslTask xslTask) {
        TaskInfo taskInfoVo = commonUseService.initTaskInfo(xslTask);
        TaskEsInfo taskEsInfo = new TaskEsInfo();
        BeanUtils.copyProperties(taskInfoVo, taskEsInfo);
        logger.info("sendTaskInfoToSearch:"+ GsonSingle.getGson().toJson(taskEsInfo));
        taskMqService.addEsTask(taskEsInfo);
    }


    private List<String> getGoodHunter() {
        return xslHunterMapper.selectGoodHunter();
    }

    private Set<String> networkHunter(XslTask xslTask) {
        //1.获取用户ID
        XslUserExample xslUserExample = new XslUserExample();
        String masterId = xslTask.getSendid();
        xslUserExample.createCriteria().andMasteridEqualTo(masterId);
        List<XslUser> xslUsers = xslUserMapper.selectByExample(xslUserExample);
        String userId = xslUsers.get(0).getUserid();

        //2.符合条件的用户
        Set<String> hunterIds = new HashSet<>();
        XslNetworkExample xslNetworkExample = new XslNetworkExample();
        xslNetworkExample.createCriteria().andAidEqualTo(userId);
        List<XslNetwork> xslNetworkAs = xslNetworkMapper.selectByExample(xslNetworkExample);
        for(XslNetwork xslNetworkA : xslNetworkAs){
            hunterIds.add(xslNetworkA.getBid());
        }

        xslNetworkExample.createCriteria().andBidEqualTo(userId);
        List<XslNetwork> xslNetworkBs = xslNetworkMapper.selectByExample(xslNetworkExample);

        for(XslNetwork xslNetworkB : xslNetworkBs){
            hunterIds.add(xslNetworkB.getAid());
        }

        return hunterIds;
    }

    private void sendToMessage(JPushVo jPushVo, String phone){
        //获取设备码
        String s = JedisClientUtil.get(REDIS_USER_SESSION_KEY + ":" + phone);
        jPushVo.setRegistrationId(s);
        if(StringUtils.isEmpty(s)){
            return;
        }
        //TODO 修改极光推送
        //发推送
        jpushService.sendToRegistrationId(jPushVo,phone);
    }
}
