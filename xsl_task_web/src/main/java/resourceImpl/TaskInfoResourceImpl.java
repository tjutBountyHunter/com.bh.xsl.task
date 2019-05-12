package resourceImpl;

import com.github.pagehelper.PageHelper;
import mapper.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import resource.TaskInfoResource;
import resource.UserInfoResouce;
import service.HunterRecommend;
import vo.*;
import vo.XslHunter;
import vo.XslMaster;
import vo.XslSchool;
import vo.XslUser;
import xsl.pojo.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class TaskInfoResourceImpl implements TaskInfoResource {
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
	private XslSchoolMapper xslSchoolMapper;

	@Autowired
	private HunterRecommend hunterRecommend;
	@Resource
	private UserInfoResouce userInfoResouce;

	@Autowired
	private ThreadPoolTaskExecutor taskExecutor;


	@Override
	public TaskInfoListResVo initTaskInfo(TaskInfoListReqVo taskInfoListReqVo) {
		TaskInfoListResVo taskInfoListResVo = new TaskInfoListResVo();
		taskInfoListResVo.setStatus(200);
		taskInfoListResVo.setMsg("成功");
		//1.获取学校id
		String schoolName = taskInfoListReqVo.getSchoolName();
		vo.XslSchool school = userInfoResouce.getSchoolByName(schoolName);
		if(school == null){
			taskInfoListResVo.setStatus(403);
			taskInfoListResVo.setMsg("请重新选择学校");
			return taskInfoListResVo;
		}
		Integer schoolId = school.getId();

		//2.获取学校id对应的任务
		Integer size = taskInfoListReqVo.getSize();
		PageHelper.startPage(1, size);
		List<String> taskIds = xslSchoolTaskMapper.selectTaskIdBySchoolId(schoolId);
		if(taskIds.size() == 0){
			return taskInfoListResVo;
		}

		PageHelper.startPage(1, size);
		List<Integer> ids = xslSchoolTaskMapper.selectIdBySchoolId(schoolId);
		Integer max = Collections.max(ids);
		Integer min = Collections.min(ids);
		taskInfoListResVo.setDownFlag(min);
		taskInfoListResVo.setUpFlag(max);

		//3.获取任务信息
		List<TaskInfoVo> taskInfoList = getTaskInfoList(taskIds);
		taskInfoListResVo.setTaskInfoVos(taskInfoList);

		return taskInfoListResVo;
	}


	private List<TaskInfoVo> getTaskInfoList(List<String> taskIds) {
		//3.获取任务信息
		XslTaskExample xslTaskExample = new XslTaskExample();
		xslTaskExample.createCriteria().andTaskidIn(taskIds);
		List<XslTask> taskList = xslTaskMapper.selectByExample(xslTaskExample);

		//4.封装返回数据
		List<TaskInfoVo> taskInfoVos = new ArrayList<>();
		for (XslTask xslTask : taskList) {
			TaskInfoVo taskInfoVo = new TaskInfoVo();
			String masterId = xslTask.getSendid();
			vo.XslMaster masterInfo = userInfoResouce.getMasterInfo(masterId);
			vo.XslUser userInfo = userInfoResouce.getUserInfoMasterId(masterId);

			//获取任务标签
			String taskid = xslTask.getTaskid();
			XslTaskTagExample xslTaskTagExample = new XslTaskTagExample();
			xslTaskTagExample.createCriteria().andTaskidEqualTo(taskid);
			List<String> tagIds = xslTaskTagMapper.selectTagIdByExample(xslTaskTagExample);

			XslTagExample xslTagExample = new XslTagExample();
			xslTagExample.createCriteria().andTagidIn(tagIds);
			List<XslTag> xslTags = xslTagMapper.selectByExample(xslTagExample);
			List<Tag> tags = new ArrayList<>();
			for(XslTag xslTag : xslTags){
				Tag tag = new Tag();
				BeanUtils.copyProperties(xslTag, tag);
				tags.add(tag);
			}


			BeanUtils.copyProperties(xslTask, taskInfoVo);
			BeanUtils.copyProperties(masterInfo, taskInfoVo);
			taskInfoVo.setMasterlevel(masterInfo.getLevel());
			BeanUtils.copyProperties(userInfo, taskInfoVo);
			taskInfoVo.setTaskId(xslTask.getTaskid());
			taskInfoVo.setTaskTitle(xslTask.getTasktitle());
			taskInfoVo.setCreateDate(xslTask.getCreatedate());
			taskInfoVo.setTxUrl("http://47.93.200.190/images/default.png");
			taskInfoVo.setMasterlevel(masterInfo.getLevel());
			taskInfoVo.setMasterId(xslTask.getSendid());
			taskInfoVo.setDeadLineDate(xslTask.getDeadline());
			taskInfoVo.setTags(tags);
			taskInfoVos.add(taskInfoVo);
		}

		return taskInfoVos;
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
		XslSchool school = userInfoResouce.getSchoolByName(schoolName);
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
		List<TaskInfoVo> taskInfoList = getTaskInfoList(taskIds);
		taskInfoListResVo.setTaskInfoVos(taskInfoList);

		return taskInfoListResVo;
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
		taskInfoResVo.setCreateDate(xslTask.getCreatedate());
		taskInfoResVo.setDeadLineDate(xslTask.getDeadline());

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
				List<Tag> tags = new ArrayList<>();
				for(XslTag xslTag : xslTags){
					Tag tag = new Tag();
					BeanUtils.copyProperties(xslTag, tag);
					tags.add(tag);
				}
				taskInfoResVo.setTags(tags);
			}
		}

		//获取雇主信息
		String masterId = xslTask.getSendid();
		XslMaster master = userInfoResouce.getMasterInfo(masterId);
		MasterInfo masterInfo = new MasterInfo();
		BeanUtils.copyProperties(master, masterInfo);
		masterInfo.setTxUrl("http://47.93.200.190/images/default.png");

		//获取手机号
		vo.XslUser userInfo = userInfoResouce.getUserInfoMasterId(masterId);
		masterInfo.setPhone(userInfo.getPhone());
		taskInfoResVo.setMasterInfo(masterInfo);

		//获取猎人信息
		if(2 == xslTask.getState()){
			//获取猎人id(在高并发环境下，这种代码肯定有问题)
			XslHunterTaskExample xslHunterTaskExample = new XslHunterTaskExample();
			xslHunterTaskExample.createCriteria().andTaskidEqualTo(taskId);
			List<XslHunterTask> xslHunterTasks = xslHunterTaskMapper.selectByExample(xslHunterTaskExample);
			String hunterId = xslHunterTasks.get(0).getHunterid();

			//获取猎人信息
			XslHunter hunter = userInfoResouce.getHunterInfo(hunterId);
			HunterInfo hunterInfo = new HunterInfo();
			BeanUtils.copyProperties(hunter, hunterInfo);
			XslUser user = userInfoResouce.getUserInfoByHunterId(hunterId);
			hunterInfo.setPhone(user.getPhone());
			hunterInfo.setTxUrl("http://47.93.200.190/images/default.png");
			taskInfoResVo.setHunterInfo(hunterInfo);
		}

		return taskInfoResVo;
	}
}
