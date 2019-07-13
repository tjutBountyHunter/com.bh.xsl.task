package com.xsl.task;

import com.xsl.task.vo.*;

import java.math.BigDecimal;

public interface TaskInfoResource {

	/**
	 * 分页展示猎人种类
	 *
	 * @param tagName
	 * @param type
	 * @param rows
	 * @return
	 */
	ResBaseVo UpCategoryHunter(String tagName, Integer type, Integer rows);

	/**
	 * 初始化任务大厅数据
	 * @return
	 */
	TaskInfoListResVo initTaskInfo(TaskInfoListReqVo taskInfoListReqVo);


	/**
	 * 刷新任务大厅数据
	 * @return
	 */
	TaskInfoListResVo reloadTaskInfo(TaskInfoListReqVo taskInfoListReqVo);

	/**
	 * 任务详情
	 * @return
	 */
	TaskInfoResVo taskInfo(String taskId);

	/**
	 * 获取已发任务
	 * @param sendAndRecTaskReqVo
	 * @return
	 */
	Task querySendTask(SendAndRecTaskReqVo sendAndRecTaskReqVo);

	/**
	 * 获取已结任务
	 * @param sendAndRecTaskReqVo
	 * @return
	 */
	Task queryReceiveTask(SendAndRecTaskReqVo sendAndRecTaskReqVo);

	/**
	 * 所有任务数
	 * @return int
	 */
	int totalTask();
	/**
	 * 所有金额
	 * @return int
	 */
	BigDecimal totalMoney();
	/**
	 * 所有完成任务数
	 * @return int
	 */
	int totalDoneTask();

}
