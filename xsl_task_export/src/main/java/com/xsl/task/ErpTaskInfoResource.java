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

	/**
	 * 任务的插入
	 * 只有状态为4和0（未审核、待接收）的时候才被创建
	 * @param xslTasks
	 * @return
	 */
	boolean insertXslTask(List<Task> xslTasks);

	/**
	 * 任务的更新
	 * @param xslTasks
	 * @return
	 */
	boolean updateXslTask(List<Task> xslTasks);

	/**
	 * 任务的删除
	 * ①任务逻辑删除 -1：冻结
	 * @return
	 */
	boolean deleteXslTask(List<Task> xslTasks);

	/**
	 * 单任务删除
	 * @param task
	 * @return
	 */
	boolean delXslTask(Task task);




}
