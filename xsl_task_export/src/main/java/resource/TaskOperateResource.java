package resource;

import vo.ConfirmTaskReqVo;
import vo.RecTaskReqVo;
import vo.TaskReqVo;
import vo.XslResult;

public interface TaskOperateResource {

	/**
	 * 发送任务
	 *
	 * @return
	 */
	XslResult sendTask(TaskReqVo taskReqVo);


	/**
	 * 接收任务
	 * @return
	 */
	XslResult receiveTask(RecTaskReqVo recTaskReqVo);


	/**
	 * 确认任务完成
	 * @return
	 */
	XslResult confirmTask(ConfirmTaskReqVo confirmTaskReqVo);

}
