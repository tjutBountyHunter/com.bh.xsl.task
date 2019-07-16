package resourceImpl;

import com.xsl.task.ErpTaskInfoResource;
import com.xsl.task.vo.ErpTaskInfoReqVo;
import com.xsl.task.vo.PageObject;
import com.xsl.task.vo.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import service.ErpTaskInfoService;

import java.util.List;

@Controller
public class ErpTaskInfoResourceImpl implements ErpTaskInfoResource {

	@Autowired
	private ErpTaskInfoService erpTaskInfoService;

	@Override
	public PageObject selectTaskAll(ErpTaskInfoReqVo erpTaskInfoReqVo) {
		try {
			return erpTaskInfoService.selectTaskAll(erpTaskInfoReqVo);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 任务的插入
	 * 只有状态为4和0（未审核、待接收）的时候才被创建
	 * @param xslTasks
	 * @return
	 */
	@Override
	public boolean insertXslTask(List<Task> xslTasks) {
		try {
			return erpTaskInfoService.insertXslTask(xslTasks);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 任务的更新
	 * @param xslTasks
	 * @return
	 */
	@Override
	public boolean updateXslTask(List<Task> xslTasks) {
		try {
			return erpTaskInfoService.updateXslTask(xslTasks);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 任务的删除
	 * ①任务逻辑删除 -1：冻结
	 * @return
	 */
	@Override
	public boolean deleteXslTask(List<Task> xslTasks) {
		try {
			return erpTaskInfoService.deleteXslTask(xslTasks);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 单任务删除
	 * @param task
	 * @return
	 */
	@Override
	public boolean delXslTask(Task task) {
		try {
			return erpTaskInfoService.delXslTask(task);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
