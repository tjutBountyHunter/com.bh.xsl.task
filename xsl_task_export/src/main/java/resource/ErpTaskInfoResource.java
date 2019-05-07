package resource;

import vo.ErpTaskInfoReqVo;
import vo.PageObject;
import vo.Task;

import java.util.List;

public interface ErpTaskInfoResource {
	/**
	 * 页面数据的查询
	 * @return 返回一个数据的列表包含data和total
	 */
	PageObject SelectTaskAll(ErpTaskInfoReqVo erpTaskInfoReqVo);

	boolean InsertXslTask(List<Task> xslTasks);

	boolean UpdateXslTask(List<Task> xslTasks);

	boolean deleteXslTask(List<Task> xslTasks);

	boolean delXslTask(Task task);




}
