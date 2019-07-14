package resourceImpl;

import com.xsl.task.TaskApproveResource;
import com.xsl.task.vo.ErpTaskInfoReqVo;
import com.xsl.task.vo.PageObject;
import com.xsl.task.vo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import service.ErpTaskInfoService;
import service.TaskApproveService;

@Controller
public class TaskApproveResourceImpl implements TaskApproveResource {

	@Autowired
	private ErpTaskInfoService erpTaskInfoService;

	@Autowired
	private TaskApproveService taskApproveService;
	/**
	 * 任务审核
	 * ①审核成功：state：0（待接收）
	 * ②审核失败：state：-4（审核未通过）
	 * @param task
	 * @return
	 */
	@Override
	public boolean approve(Task task) {
		try {
			return taskApproveService.approve(task);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * user_approve页面的数据显示，只查询状态为state:4 (未审核)的任务
	 * @return 返回页面显示的数据的集合
	 */
	@Override
	public PageObject selectTaskApprove(ErpTaskInfoReqVo erpTaskInfoReqVo) {
		try {
			//只查询待接收的任务
			return erpTaskInfoService.selectTaskAll(erpTaskInfoReqVo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
