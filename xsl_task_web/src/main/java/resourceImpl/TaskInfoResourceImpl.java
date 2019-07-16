package resourceImpl;

import com.xsl.task.TaskInfoResource;
import com.xsl.task.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import service.TaskInfoService;

@Controller
public class TaskInfoResourceImpl implements TaskInfoResource {

	@Autowired
	private TaskInfoService taskInfoService;


	@Override
	public ResBaseVo UpCategoryHunter(String tagName, Integer type, Integer rows) {
		try {
			return taskInfoService.UpCategoryHunter(tagName,type,rows);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public TaskInfoListResVo initTaskInfo(TaskInfoListReqVo taskInfoListReqVo) {
		try {
			return taskInfoService.initTaskInfo(taskInfoListReqVo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public TaskInfoListResVo reloadTaskInfo(TaskInfoListReqVo taskInfoListReqVo) {
		try {
			return taskInfoService.reloadTaskInfo(taskInfoListReqVo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public TaskInfoResVo taskInfo(String taskId) {
		try {
			return taskInfoService.taskInfo(taskId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public TaskListResVo querySendTask(SendAndRecTaskReqVo sendAndRecTaskReqVo) {

		try {
			return taskInfoService.querySendTask(sendAndRecTaskReqVo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public TaskListResVo queryReceiveTask(SendAndRecTaskReqVo sendAndRecTaskReqVo) {
		try {
			return taskInfoService.queryReceiveTask(sendAndRecTaskReqVo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public TaskConstantInfoResVo totalTask() {
		try {
			return taskInfoService.totalTask();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public TaskConstantInfoResVo totalMoney() {
		try {
			return taskInfoService.totalMoney();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public TaskConstantInfoResVo totalDoneTask() {
		try {
			return taskInfoService.totalDoneTask();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public TaskListResVo getTaskByMasterId(String masterId) {
		try {
			return taskInfoService.getTaskByMasterId(masterId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
