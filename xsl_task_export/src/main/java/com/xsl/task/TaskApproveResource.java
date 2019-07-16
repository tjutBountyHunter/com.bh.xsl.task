package com.xsl.task;

import com.xsl.task.vo.ErpTaskInfoReqVo;
import com.xsl.task.vo.PageObject;
import com.xsl.task.vo.Task;

public interface TaskApproveResource {
	//********task_approve的两个操作

	/**
	 * user_approve页面的数据显示，只查询状态为state:4 (未审核)的任务
	 * @return 返回页面显示的数据的集合
	 */
	PageObject selectTaskApprove(ErpTaskInfoReqVo erpTaskInfoReqVo);

	/**
	 * 任务审核
	 * ①审核成功：state：0（待接收）
	 * ②审核失败：state：-4（审核未通过）
	 * @param task
	 * @return
	 */
	boolean approve(Task task);

}
