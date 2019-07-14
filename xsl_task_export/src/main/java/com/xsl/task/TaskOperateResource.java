package com.xsl.task;

import com.xsl.task.vo.*;

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
	 * 任务搜索
	 * @param searchTaskReqVo
	 * @return
	 */
	SearchTaskInfoListResVo searchTask(SearchTaskReqVo searchTaskReqVo);

	/**
	 * 取消超时任务
	 * @return
	 */
	ResBaseVo cancelTaskDDL();

	/**
	 * 取消任务
	 * @param taskId
	 * @return
	 */
	ResBaseVo cancelTask(String taskId);

}
