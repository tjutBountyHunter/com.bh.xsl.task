package resourceImpl;

import mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import resource.JpushResource;
import resource.TaskOperateResource;
import resource.UserInfoResouce;
import service.HunterRecommend;
import service.impl.searchTaskMQImpl;
import service.searchTaskMQ;
import util.*;
import vo.*;
import vo.XslResult;
import vo.XslSchool;
import vo.XslSchoolinfo;
import xsl.pojo.*;
import xsl.pojo.XslUser;

import javax.annotation.Resource;
import javax.jms.Destination;
import java.util.*;

@Controller
public class TaskOperateResourceImpl implements TaskOperateResource {
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

	@Autowired
	private HunterRecommend hunterRecommend;
	@Resource
	private JpushResource jpushResource;
	@Resource
	private UserInfoResouce userInfoResouce;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination creatOrder;


	@Override
	public XslResult sendTask(TaskReqVo taskReqVo) {
		try {
			if(StringUtils.isEmpty(taskReqVo.getContent())){
				return XslResult.build(400, "参数错误");
			}
			//文字扫描屏蔽
			Map<String, String> map = new HashMap<>(1);
			map.put("sentence", taskReqVo.getContent());
			String result = HttpClientUtil.doGet("http://47.93.19.164:8080/xsl-search-service/search/wordcheck", map);
			XslResultOk fcResult = XslResultOk.format(result);
			List<String> data = (List<String>) fcResult.getData();
			if (data != null && data.size() > 0) {
				return XslResult.build(400, "悬赏任务不合法");
			}


			//设置任务分类--默认全种类
			XslTask xslTask = new XslTask();
			xslTask.setCid(1);
			xslTask.setSendid(taskReqVo.getMasterId());
			xslTask.setTaskid(UUIDUtil.getUUID());
			xslTask.setContent(taskReqVo.getContent());
			xslTask.setMoney(taskReqVo.getMoney());
			xslTask.setTasktitle(taskReqVo.getTaskTitle());
			xslTask.setCreatedate(taskReqVo.getCreateDate());
			xslTask.setUpdatedate(taskReqVo.getCreateDate());
			xslTask.setDeadline(taskReqVo.getDeadLineDate());
			xslTask.setSourcetype(taskReqVo.getSourceType());
			//未启动推荐
			xslTask.setState((byte) 0);

			if(taskReqVo.getIsRecommend() == null){
				return XslResult.build(400, "参数错误");
			}

			//启动推荐
			if(taskReqVo.getIsRecommend()){
				xslTask.setState((byte) 1);
			}

			//记录任务
			int insert = xslTaskMapper.insertSelective(xslTask);

			if(insert < 1){
				return XslResult.build(500, "服务器异常");
			}

			//发送mq到搜索系统
			searchTaskMQ searchTaskMQ = new searchTaskMQImpl();
			searchTaskMQ.addTaskJson(JsonUtils.objectToJson(xslTask));

			XslResult xslResultTag = addTaskTag(taskReqVo, xslTask.getTaskid());
			XslResult xslResultFile = addTaskFile(taskReqVo, xslTask.getTaskid());
			XslResult xslResultSchool = addSchoolTask(taskReqVo, xslTask.getTaskid());

			if(xslResultFile.isOK() && xslResultTag.isOK() && xslResultSchool.isOK()){
				//异步启动推荐
				if(taskReqVo.getIsRecommend()){
					taskExecutor.execute(() -> hunterRecommendAndPush(xslTask));
				}

				//异步更新雇主信息


				return XslResult.ok(xslTask.getTaskid());
			}

			return XslResult.build(500, "服务器异常");
		} catch (Exception e) {
			e.printStackTrace();
			return XslResult.build(500, "服务器异常");
		}
	}

	@Override
	public XslResult receiveTask(RecTaskReqVo recTaskReqVo) {
		String hunterid = recTaskReqVo.getHunterid();
		String taskid = recTaskReqVo.getTaskId();
		//1.判断用户状态
		vo.XslUser userInfo = userInfoResouce.getUserInfoByHunterId(hunterid);
		if(userInfo == null){
			return XslResult.build(403, "您无权操作");
		}
		if(1 != userInfo.getState()){
			return XslResult.build(403, "您无权操作");
		}

		//2.获取任务信息
		XslTaskExample xslTaskExample = new XslTaskExample();
		XslTaskExample.Criteria criteria = xslTaskExample.createCriteria();
		criteria.andTaskidEqualTo(taskid);
		List<XslTask> taskList = xslTaskMapper.selectByExample(xslTaskExample);
		if(taskList == null || taskList.size() < 1){
			return XslResult.build(500, "任务信息异常");
		}

		XslTask xslTask = taskList.get(0);

		String masterid = userInfo.getMasterid();

		if(masterid.equals(xslTask.getSendid())){
			return XslResult.build(403, "请不要接自己发送的任务");
		}
		Byte state = xslTask.getState();
		if(!(0 == state || 1 == state)){
			return XslResult.build(403, "任务已经被抢走");
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
			return XslResult.build(403, "任务接收失败");
		}

		XslHunterTask xslHunterTask = new XslHunterTask();
		xslHunterTask.setHunterid(hunterid);
		xslHunterTask.setTaskid(recTaskReqVo.getTaskId());
		xslHunterTask.setTaskstate((byte) 2);
		int count = xslHunterTaskMapper.insertSelective(xslHunterTask);
		if(count < 1){
			return XslResult.build(403, "请不要接自己发送的任务");
		}

		//异步建立用户关联
		//异步更新猎人信息
		//异步生成订单
		jmsTemplate.send(creatOrder, (session) -> session.createTextMessage("test"));


		//异步给雇主发推送

		return XslResult.ok();
	}

	@Override
	public XslResult confirmTask(ConfirmTaskReqVo confirmTaskReqVo) {
		String taskId = confirmTaskReqVo.getTaskId();
		String hunterId = confirmTaskReqVo.getHunterid();
		//检测任务状态
		XslTaskExample xslTaskExample = new XslTaskExample();
		xslTaskExample.createCriteria().andTaskidEqualTo(taskId);
		List<XslTask> taskList = xslTaskMapper.selectByExample(xslTaskExample);

		if(taskList == null || taskList.size() == 0){
			return XslResult.build(403, "任务不存在");
		}
		XslTask xslTask = taskList.get(0);
		if(2 != xslTask.getState()){
			return XslResult.build(403, "任务状态错误");
		}

		//检测连接状态
		XslHunterTaskExample xslHunterTaskExample = new XslHunterTaskExample();
		xslHunterTaskExample.createCriteria().andHunteridEqualTo(hunterId).andTaskidEqualTo(taskId);
		List<XslHunterTask> xslHunterTasks = xslHunterTaskMapper.selectByExample(xslHunterTaskExample);

		if(xslHunterTasks == null || xslHunterTasks.size() == 0){
			return XslResult.build(403, "猎人信息有误");
		}
		XslHunterTask xslHunterTask = xslHunterTasks.get(0);
		if(2 != xslHunterTask.getTaskstate()){
			return XslResult.build(403, "任务状态有误");
		}

		//更新任务状态
		xslTask.setState((byte) 4);
		int i = xslTaskMapper.updateByExampleSelective(xslTask, xslTaskExample);
		if(i < 1){
			return XslResult.build(500, "服务器异常");
		}

		//更新连接状态
		xslHunterTask.setTaskstate((byte) 2);
		int i1 = xslHunterTaskMapper.updateByExampleSelective(xslHunterTask, xslHunterTaskExample);
		if(i1 < 1){
			return XslResult.build(500, "服务器异常");
		}
		//增加经验


		return XslResult.ok();
	}

	private XslResult hunterRecommendAndPush(XslTask xslTask){

		List<String> recommend;
		//猎人标签推优算法
		recommend = hunterRecommend.recommend(xslTask.getTaskid(), 10);

		if(recommend == null || recommend.size() == 0){
			//血缘关系推荐算法启动
			Set<String> hunters = networkHunter(xslTask);

			recommend.addAll(hunters);

			if(hunters.size() == 0){
				recommend = getGoodHunter();
			}

		}

		JPushReqVo jPushVo = new JPushReqVo();
		jPushVo.setMsgTitle("悬赏任务推荐");
		jPushVo.setMsgContent("有一个适合你的悬赏任务");
		jPushVo.setNotificationTitle("悬赏任务推荐");
		jPushVo.setExtrasparam("");

		for (String hunterId : recommend){
			//查电话号码
			XslUserExample xslUserExample = new XslUserExample();

			xslUserExample.createCriteria().andHunteridEqualTo(hunterId);
			List<XslUser> xslUsers = xslUserMapper.selectByExample(xslUserExample);
			String phone = xslUsers.get(0).getPhone();
			jPushVo.setPhone(phone);
			//发推送
			jpushResource.sendByPhone(jPushVo);
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
		vo.XslUser user = userInfoResouce.getUserInfoMasterId(taskReqVo.getMasterId());
		String schoolinfo = user.getSchoolinfo();
		XslSchoolinfo schoolInfo = userInfoResouce.getSchoolInfo(schoolinfo);
		String schoolName = schoolInfo.getSchool();
		XslSchool school = userInfoResouce.getSchoolByName(schoolName);

		XslSchoolTask xslSchoolTask = new XslSchoolTask();
		xslSchoolTask.setSchoolid(school.getId());
		xslSchoolTask.setTaskid(taskid);

		int insert = xslSchoolTaskMapper.insertSelective(xslSchoolTask);

		if(insert < 1){
			throw new RuntimeException("任务学校信息关联异常");
		}

		return XslResult.ok();
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

	private List<String> getGoodHunter() {
		return xslHunterMapper.selectGoodHunter();
	}
}
