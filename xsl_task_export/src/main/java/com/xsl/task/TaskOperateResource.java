package resource;

import vo.*;

public interface TaskOperateResource {

	/**
	 * 发送任务
	 *
	 * @return
	 */
	ResBaseVo sendTask(TaskReqVo taskReqVo);


	/**
	 * 接收任务
	 * @return
	 */
	ResBaseVo receiveTask(RecTaskReqVo recTaskReqVo);


	/**
	 * 确认任务完成
	 * @return
	 */
	ResBaseVo confirmTask(ConfirmTaskReqVo confirmTaskReqVo);


	/**
	 * 取消超时任务
	 * @return
	 */
	ResBaseVo calcelTaskDDL();

}
