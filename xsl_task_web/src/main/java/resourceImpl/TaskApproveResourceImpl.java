package resourceImpl;

import mapper.XslTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import resource.ErpTaskInfoResource;
import resource.TaskApproveResource;
import vo.ErpTaskInfoReqVo;
import vo.PageObject;
import vo.Task;
import xsl.pojo.XslTask;

import javax.annotation.Resource;

@Controller
public class TaskApproveResourceImpl implements TaskApproveResource {

	@Autowired
	private ErpTaskInfoResource erpTaskInfoResource;
	@Resource
	private XslTaskMapper xslTaskMapper;

	/* 任务服务 */
	Logger logger = LoggerFactory.getLogger(TaskApproveResourceImpl.class);

	/**
	 * 任务审核
	 * ①审核成功：state：0（待接收）
	 * ②审核失败：state：-4（审核未通过）
	 * @param task
	 * @return
	 */
	@Override
	public boolean approve(Task task) {
		String  tag = "任务审核";
		try{
			//-1代表冻结的意思，进行逻辑删除
			XslTask xslTask = new XslTask();
			BeanUtils.copyProperties(task, xslTask);

			int n = xslTaskMapper.updateByPrimaryKeySelective(xslTask);
			if( n < 0 ) {
				logger.error(tag + "失败!");
				return false;
			}
		}catch (Exception e){
			logger.error(tag + "异常警报 :" + e.getMessage());
		}
		return true;
	}

	/**
	 * user_approve页面的数据显示，只查询状态为state:4 (未审核)的任务
	 * @return 返回页面显示的数据的集合
	 */
	@Override
	public PageObject SelectTaskApprove(ErpTaskInfoReqVo erpTaskInfoReqVo) {
		return erpTaskInfoResource.SelectTaskAll(erpTaskInfoReqVo);//只查询待接收的任务
	}
}
