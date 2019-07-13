package resourceImpl;

import com.xsl.task.TaskOperateResource;
import com.xsl.task.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import service.CancelTaskService;
import service.TaskOperateService;

@Controller
public class TaskOperateResourceImpl implements TaskOperateResource {

	@Autowired
	private TaskOperateService taskOperateService;

	@Autowired
	private CancelTaskService cancelTaskService;

	@Override
	public ResBaseVo sendTask(TaskReqVo taskReqVo) {
		try {
			return taskOperateService.sendTask(taskReqVo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ResBaseVo receiveTask(RecTaskReqVo recTaskReqVo) {
		try {
			return taskOperateService.receiveTask(recTaskReqVo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ResBaseVo confirmTask(ConfirmTaskReqVo confirmTaskReqVo) {
		try {
			return taskOperateService.confirmTask(confirmTaskReqVo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public SearchTaskInfoListResVo searchTask(SearchTaskReqVo searchTaskReqVo) {
		try {
			return taskOperateService.searchTask(searchTaskReqVo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ResBaseVo calcelTaskDDL() {
		try {
			return cancelTaskService.cancelTaskDDL();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public ResBaseVo cancelTask(String taskId) {
		try {
			return taskOperateService.cancelTask(taskId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
