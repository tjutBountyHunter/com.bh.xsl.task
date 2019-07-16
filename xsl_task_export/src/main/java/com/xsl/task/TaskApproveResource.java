package com.xsl.task;

import com.xsl.task.vo.ErpTaskInfoReqVo;
import com.xsl.task.vo.PageObject;
import com.xsl.task.vo.Task;

public interface TaskApproveResource {
	//********task_approve的两个操作
	PageObject selectTaskApprove(ErpTaskInfoReqVo erpTaskInfoReqVo);

	boolean approve(Task task);

}
