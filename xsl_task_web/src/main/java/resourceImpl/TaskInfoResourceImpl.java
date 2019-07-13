package resourceImpl;

import com.xsl.task.TaskInfoResource;
import com.xsl.task.vo.*;
import org.springframework.stereotype.Controller;

@Controller
public class TaskInfoResourceImpl implements TaskInfoResource {

	@Override
	public ResBaseVo UpCategoryHunter(String tagName, Integer type, Integer rows) {
		return null;
	}

	@Override
	public TaskInfoListResVo initTaskInfo(TaskInfoListReqVo taskInfoListReqVo) {
		return null;
	}

	@Override
	public TaskInfoListResVo reloadTaskInfo(TaskInfoListReqVo taskInfoListReqVo) {
		return null;
	}

	@Override
	public TaskInfoResVo taskInfo(String taskId) {
		return null;
	}

	@Override
	public TaskListResVo querySendTask(SendAndRecTaskReqVo sendAndRecTaskReqVo) {
		return null;
	}

	@Override
	public TaskListResVo queryReceiveTask(SendAndRecTaskReqVo sendAndRecTaskReqVo) {
		return null;
	}

	@Override
	public TaskConstantInfoResVo totalTask() {
		return null;
	}

	@Override
	public TaskConstantInfoResVo totalMoney() {
		return null;
	}

	@Override
	public TaskConstantInfoResVo totalDoneTask() {
		return null;
	}

	@Override
	public TaskListResVo getTaskByMasterId(String masterId) {
		return null;
	}
}
