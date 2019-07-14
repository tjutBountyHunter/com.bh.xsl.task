package service.impl;

import com.github.pagehelper.PageHelper;
import com.xsl.task.vo.*;
import com.xsl.user.UserInfoResouce;
import com.xsl.user.vo.MasterVo;
import com.xsl.user.vo.SchoolVo;
import com.xsl.user.vo.UserVo;
import mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import service.CommonUseService;
import service.TaskAccountService;
import service.TaskInfoService;
import service.TaskOperateService;
import util.DateUtils;
import util.GsonSingle;
import util.GsonUtil;
import util.ListUtil;
import xsl.pojo.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author 梁俊伟
 * @version 1.0
 * @date 2019/5/27 20:01
 */
@Service
public class TaskInfoServiceImpl implements TaskInfoService {

    private static final Logger logger = LoggerFactory.getLogger(TaskInfoServiceImpl.class);

    @Autowired
    private XslHuntershowMapper xslHuntershowMapper;

    @Autowired
    private XslTaskMapper xslTaskMapper;
    @Autowired
    private XslTaskTagMapper xslTaskTagMapper;
    @Autowired
    private XslTagMapper xslTagMapper;
    @Autowired
    private XslHunterTaskMapper xslHunterTaskMapper;
    @Autowired
    private XslSchoolTaskMapper xslSchoolTaskMapper;

    @Autowired
    private TaskOperateService taskOperateService;

    @Autowired
    private CommonUseService commonUseService;
    @Autowired
    private TaskAccountService taskAccountService;

    @Resource
    private UserInfoResouce userInfoResouce;



    @Value("${REDIS_USER_SESSION_KEY}")
    private String REDIS_USER_SESSION_KEY;
    @Value("${USER_INFO}")
    private String USER_INFO;
    @Value("${USER_HUNTER_INFO}")
    private String USER_HUNTER_INFO;
    @Value("${USER_MASTER_INFO}")
    private String USER_MASTER_INFO;

    @Override
    public ResBaseVo UpCategoryHunter(String tagName, Integer type, Integer rows) {
        try {
            tagName = new String(tagName.getBytes("iso-8859-1"), "utf-8");
            Map<String, Object> map = new HashMap<>(2);
            map.put("tagName", tagName);
            map.put("rows", rows);
            List<pojo.XslOneHunter> oneHunterList = null;
            if (type == 0) {
                oneHunterList = xslHuntershowMapper.getXslHunterListfirst(map);
                return ResBaseVo.ok(oneHunterList);
            } else if (type == 1) {
                oneHunterList = xslHuntershowMapper.getXslHunterOld(map);
                return ResBaseVo.ok(oneHunterList);
            } else {
                oneHunterList = xslHuntershowMapper.getXslHunterNew(map);
                return ResBaseVo.ok(oneHunterList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResBaseVo.build(500, "服务器异常");
        }
    }

    @Override
    public TaskInfoListResVo initTaskInfo(TaskInfoListReqVo taskInfoListReqVo) {
        //1.获取学校id
        logger.info("initTaskInfo:"+GsonSingle.getGson().toJson(taskInfoListReqVo));
        TaskInfoListResVo taskInfoListResVo = new TaskInfoListResVo();
        taskInfoListResVo.setStatus(200);
        taskInfoListResVo.setMsg("成功");
        String schoolName = taskInfoListReqVo.getSchoolName();
        if(StringUtils.isEmpty(schoolName) || "Empty".equals(schoolName)){
            taskInfoListResVo.setStatus(403);
            taskInfoListResVo.setMsg("请登记学校信息");
            return taskInfoListResVo;
        }
        Integer size = taskInfoListReqVo.getSize();
        SchoolVo school = userInfoResouce.getSchoolByName(schoolName);
        if(school == null){
            taskInfoListResVo.setStatus(403);
            taskInfoListResVo.setMsg("请重新选择学校");
            return taskInfoListResVo;
        }
        Integer schoolId = school.getId();

        PageHelper.startPage(1, size);
        List<Integer> ids = xslSchoolTaskMapper.selectIdBySchoolId(schoolId);

        if(!ListUtil.isNotEmpty(ids)){
            return taskInfoListResVo;
        }

        Integer	max = Collections.max(ids);
        Integer	min = Collections.min(ids);


        taskInfoListResVo.setDownFlag(min);
        taskInfoListResVo.setUpFlag(max);

        //3.获取任务信息

        SearchTaskReqVo taskSearchVo = new SearchTaskReqVo();
        taskSearchVo.setSize(size);
        taskSearchVo.setKeyword("");
        taskSearchVo.setSchoolName(schoolName);
        SearchTaskInfoListResVo searchTaskInfoListResVo = taskOperateService.searchTask(taskSearchVo);

        taskInfoListResVo.setTaskInfoVos(new ArrayList<>());
        List<TaskInfo> data = searchTaskInfoListResVo.getTaskInfoList();
        if(ListUtil.isNotEmpty(data)){
            List<TaskInfoVo> taskInfoVos = GsonUtil.gsonToList(GsonUtil.gsonString(data),TaskInfoVo.class);
            taskInfoListResVo.setTaskInfoVos(taskInfoVos);
        }

        logger.info("initTaskInfo.taskInfoListResVo msg:" + GsonSingle.getGson().toJson(taskInfoListResVo));
        return taskInfoListResVo;
    }

    @Override
    public TaskInfoListResVo reloadTaskInfo(TaskInfoListReqVo taskInfoListReqVo) {
        TaskInfoListResVo taskInfoListResVo = new TaskInfoListResVo();
        taskInfoListResVo.setStatus(200);
        taskInfoListResVo.setMsg("成功");
        List<String> taskIds = null;

        //1.获取学校id对应的任务
        if("UP".equals(taskInfoListReqVo.getType())){
            Integer id = taskInfoListReqVo.getUpFlag();
            taskIds = xslSchoolTaskMapper.selectTaskIdByGreaterThanId(id);
        }

        if("DOWN".equals(taskInfoListReqVo.getType())){
            Integer id = taskInfoListReqVo.getDownFlag();
            taskIds = xslSchoolTaskMapper.selectTaskIdByLessThanSchoolId(id);
        }


        if(taskIds == null || taskIds.size() == 0){
            return taskInfoListResVo;
        }

        //2.获取学校id
        String schoolName = taskInfoListReqVo.getSchoolName();
        SchoolVo school = userInfoResouce.getSchoolByName(schoolName);
        if(school == null){
            taskInfoListResVo.setStatus(403);
            taskInfoListResVo.setMsg("请重新选择学校");
            return taskInfoListResVo;
        }
        Integer schoolId = school.getId();

        //4.获取id列表
        Integer size = taskInfoListReqVo.getSize();
        PageHelper.startPage(1, size);
        List<Integer> ids = xslSchoolTaskMapper.selectIdBySchoolId(schoolId);

        if("UP".equals(taskInfoListReqVo.getType())){
            Integer max = Collections.max(ids);
            taskInfoListResVo.setUpFlag(max);
        }

        if("DOWN".equals(taskInfoListReqVo.getType())){
            Integer min = Collections.min(ids);
            taskInfoListResVo.setDownFlag(min);
        }


        //3.获取任务信息
        List<TaskInfo> taskInfoList = getTaskInfoList(taskIds);
        List<TaskInfoVo> taskInfoVos = GsonUtil.gsonToList(GsonUtil.gsonString(taskInfoList),TaskInfoVo.class);
        taskInfoListResVo.setTaskInfoVos(taskInfoVos);

        return taskInfoListResVo;
    }

    private List<TaskInfo> getTaskInfoList(List<String> taskIds) {
        //3.获取任务信息
        XslTaskExample xslTaskExample = new XslTaskExample();
        List<Byte> status = new ArrayList<>();
        status.add((byte) 0);
        status.add((byte) 1);
        xslTaskExample.createCriteria().andTaskidIn(taskIds).andStateIn(status);
        List<XslTask> taskList = xslTaskMapper.selectByExample(xslTaskExample);

        //4.封装返回数据
        List<TaskInfo> taskInfoVos = new ArrayList<>();
        for (XslTask xslTask : taskList) {
            TaskInfo taskInfoVo = commonUseService.initTaskInfo(xslTask);
            taskInfoVos.add(taskInfoVo);
        }

        return taskInfoVos;
    }

    @Override
    public TaskInfoResVo taskInfo(String taskId) {
        TaskInfoResVo taskInfoResVo = new TaskInfoResVo();
        taskInfoResVo.setStatus(200);
        taskInfoResVo.setMsg("成功");
        if(StringUtils.isEmpty(taskId)){
            taskInfoResVo.setStatus(403);
            taskInfoResVo.setMsg("任务不存在");
            return taskInfoResVo;
        }

        //获取任务信息
        XslTaskExample xslTaskExample = new XslTaskExample();
        xslTaskExample.createCriteria().andTaskidEqualTo(taskId);
        List<XslTask> taskList = xslTaskMapper.selectByExample(xslTaskExample);

        if(taskList == null || taskList.size() == 0){
            taskInfoResVo.setStatus(403);
            taskInfoResVo.setMsg("任务不存在");
            return taskInfoResVo;
        }

        XslTask xslTask = taskList.get(0);
        BeanUtils.copyProperties(xslTask, taskInfoResVo);
        taskInfoResVo.setTaskId(xslTask.getTaskid());
        taskInfoResVo.setTaskTitle(xslTask.getTasktitle());
        taskInfoResVo.setCreateDate(DateUtils.getDateTimeToString(xslTask.getCreatedate()));
        taskInfoResVo.setDeadLineDate(DateUtils.getDateTimeToString(xslTask.getDeadline()));

        //获取任务标签id
        XslTaskTagExample xslTaskTagExample = new XslTaskTagExample();
        xslTaskTagExample.createCriteria().andTaskidEqualTo(taskId);
        List<String> tagids = xslTaskTagMapper.selectTagidByExample(xslTaskTagExample);
        if(tagids != null && tagids.size() > 0){
            //获取任务标签
            XslTagExample xslTagExample = new XslTagExample();
            xslTagExample.createCriteria().andTagidIn(tagids);
            List<XslTag> xslTags = xslTagMapper.selectByExample(xslTagExample);
            if(xslTags != null && xslTags.size() > 0){
                List<Tag> tags = GsonUtil.gsonToList(GsonUtil.gsonString(xslTags),Tag.class);
                taskInfoResVo.setTags(tags);
            }
        }

        //获取雇主信息
        String masterId = xslTask.getSendid();
        MasterVo master = userInfoResouce.getMasterInfo(masterId);
        MasterInfo masterInfo = new MasterInfo();
        BeanUtils.copyProperties(master, masterInfo);
        masterInfo.setTxUrl("http://47.93.200.190/images/default.png");
        String userTx = userInfoResouce.getUserTx(masterInfo.getUserid());
        if(!StringUtils.isEmpty(userTx)){
            masterInfo.setTxUrl(userTx);
        }
        //获取手机号
        UserVo userInfo = userInfoResouce.getUserInfoMasterId(masterId);
        masterInfo.setName(userInfo.getName());
        masterInfo.setPhone(userInfo.getPhone());
        taskInfoResVo.setMasterInfo(masterInfo);

        //获取猎人信息
        if(2 == xslTask.getState() || 4 == xslTask.getState()){
            //获取猎人id(在高并发环境下，这种代码肯定有问题)
            XslHunterTaskExample xslHunterTaskExample = new XslHunterTaskExample();
            xslHunterTaskExample.createCriteria().andTaskidEqualTo(taskId);
            List<XslHunterTask> xslHunterTasks = xslHunterTaskMapper.selectByExample(xslHunterTaskExample);
            String hunterId = xslHunterTasks.get(0).getHunterid();

            //获取猎人信息
            HunterInfo hunterInfo = commonUseService.getHunterInfo(hunterId);
            taskInfoResVo.setHunterInfo(hunterInfo);
        }

        return taskInfoResVo;
    }


    @Override
    public TaskListResVo querySendTask(SendAndRecTaskReqVo sendAndRecTaskReqVo) {
        TaskListResVo taskListResVo = new TaskListResVo();
        taskListResVo.setStatus(200);
        taskListResVo.setMsg("成功");
        Integer page = sendAndRecTaskReqVo.getPage();
        Integer rows = sendAndRecTaskReqVo.getRows();

        PageHelper.startPage(page,rows);
        vo.SendAndRecTaskReqVo sendAndRecTaskReqVoTemp = new vo.SendAndRecTaskReqVo();
        BeanUtils.copyProperties(sendAndRecTaskReqVo,sendAndRecTaskReqVoTemp);
        List<XslTask> taskList = xslTaskMapper.selectBySendId(sendAndRecTaskReqVoTemp);

        if(!ListUtil.isNotEmpty(taskList)){
            return taskListResVo;
        }

        ArrayList<SendRecTaskVo> sendRecTaskVos = taskList.stream().collect(ArrayList::new, (res, item) -> res.add(initSendRecVo(item)), ArrayList::addAll);
        taskListResVo.setSendRecTaskVoList(sendRecTaskVos);

        return taskListResVo;
    }


    @Override
    public TaskListResVo queryReceiveTask(SendAndRecTaskReqVo sendAndRecTaskReqVo) {
        TaskListResVo taskListResVo = new TaskListResVo();
        taskListResVo.setStatus(200);
        taskListResVo.setMsg("成功");
        Integer page = sendAndRecTaskReqVo.getPage();
        Integer rows = sendAndRecTaskReqVo.getRows();

        PageHelper.startPage(page,rows);
        vo.SendAndRecTaskReqVo sendAndRecTaskReqVoTemp = new vo.SendAndRecTaskReqVo();
        BeanUtils.copyProperties(sendAndRecTaskReqVo,sendAndRecTaskReqVoTemp);
        List<String> taskIds = xslHunterTaskMapper.selectByRecId(sendAndRecTaskReqVoTemp);

        if(ListUtil.isNotEmpty(taskIds)){
            XslTaskExample xslTaskExample = new XslTaskExample();
            xslTaskExample.createCriteria().andTaskidIn(taskIds);
            List<XslTask> taskList = xslTaskMapper.selectByExample(xslTaskExample);
            ArrayList<SendRecTaskVo> sendRecTaskVos = taskList.stream().collect(ArrayList::new, (res, item) -> res.add(initSendRecVo(item)), ArrayList::addAll);
            taskListResVo.setSendRecTaskVoList(sendRecTaskVos);
            return taskListResVo;
        }

        return taskListResVo;
    }

    @Override
    public TaskConstantInfoResVo totalTask() {
        TaskConstantInfoResVo taskConstantInfoResVo = new TaskConstantInfoResVo();
        int totalTaskCount = taskAccountService.totalTask();
        taskConstantInfoResVo.setTotalTask(totalTaskCount);
        return taskConstantInfoResVo;
    }

    @Override
    public TaskConstantInfoResVo totalMoney() {
        TaskConstantInfoResVo taskConstantInfoResVo = new TaskConstantInfoResVo();
        BigDecimal totalTaskMoney = taskAccountService.totalMoney();
        taskConstantInfoResVo.setTotalMoney(totalTaskMoney);
        return taskConstantInfoResVo;
    }

    @Override
    public TaskConstantInfoResVo totalDoneTask() {
        TaskConstantInfoResVo taskConstantInfoResVo = new TaskConstantInfoResVo();
        int totalFinishTask = taskAccountService.totalDoneTask();
        taskConstantInfoResVo.setTotalDoneTask(totalFinishTask);
        return taskConstantInfoResVo;
    }

    @Override
    public TaskListResVo getTaskByMasterId(String masterId) {
        TaskListResVo taskListResVo = new TaskListResVo();
        taskListResVo.setStatus(200);
        taskListResVo.setMsg("成功");
        XslTaskExample xslTaskExample = new XslTaskExample();
        xslTaskExample.createCriteria().andSendidEqualTo(masterId).andStateGreaterThan((byte) -1).andStateLessThan((byte) 2);

        List<XslTask> taskList = xslTaskMapper.selectByExample(xslTaskExample);
        if(taskList.size()<1){
            taskListResVo.setStatus(403);
            taskListResVo.setMsg("您为发布或接收任何任务");
        }
        ArrayList<SendRecTaskVo> sendRecTaskVos = taskList.stream().collect(ArrayList::new, (res, item) -> res.add(initSendRecVo(item)), ArrayList::addAll);
        taskListResVo.setSendRecTaskVoList(sendRecTaskVos);
        return taskListResVo;
    }

    private SendRecTaskVo initSendRecVo(XslTask xslTask){
        SendRecTaskVo sendRecTaskVo = new SendRecTaskVo();
        BeanUtils.copyProperties(xslTask, sendRecTaskVo);
        sendRecTaskVo.setCreatedate(DateUtils.getDateTimeToString(xslTask.getCreatedate()));
        sendRecTaskVo.setUpdatedate(DateUtils.getDateTimeToString(xslTask.getUpdatedate()));
        sendRecTaskVo.setDeadline(DateUtils.getDateTimeToString(xslTask.getDeadline()));
        return sendRecTaskVo;
    }
}
