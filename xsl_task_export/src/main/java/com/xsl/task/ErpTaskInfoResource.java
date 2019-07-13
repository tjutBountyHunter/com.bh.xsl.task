package com.xsl.task;

import com.xsl.task.vo.ErpTaskInfoReqVo;
import com.xsl.task.vo.PageObject;
import com.xsl.task.vo.Task;

import java.util.List;

public interface ErpTaskInfoResource {
	/**
	 * 页面数据的查询
	 * @return 返回一个数据的列表包含data和total
	 */
	PageObject selectTaskAll(ErpTaskInfoReqVo erpTaskInfoReqVo);

	boolean insertXslTask(List<Task> xslTasks);

	boolean updateXslTask(List<Task> xslTasks);

	boolean deleteXslTask(List<Task> xslTasks);

	boolean delXslTask(Task task);




}
