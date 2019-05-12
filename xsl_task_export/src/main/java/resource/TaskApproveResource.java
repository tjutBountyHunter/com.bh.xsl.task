package resource;

import vo.ErpTaskInfoReqVo;
import vo.PageObject;
import vo.Task;
import vo.User;

public interface TaskApproveResource {
	//********task_approve的两个操作
	PageObject selectTaskApprove(ErpTaskInfoReqVo erpTaskInfoReqVo);

	boolean approve(Task task);


}
