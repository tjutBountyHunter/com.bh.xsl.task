package service;

import com.xsl.task.vo.ErpTaskInfoReqVo;
import com.xsl.task.vo.PageObject;
import com.xsl.task.vo.Task;

/**
 * @author 梁俊伟
 * @version 1.0
 * @date 2019/5/27 21:04
 */
public interface TaskApproveService {

    /**
     * 任务审核
     * ①审核成功：state：0（待接收）
     * ②审核失败：state：-4（审核未通过）
     * @param task
     * @return
     */
    boolean approve(Task task);

    /**
     * user_approve页面的数据显示，只查询状态为state:4 (未审核)的任务
     * @return 返回页面显示的数据的集合
     */
    PageObject selectTaskApprove(ErpTaskInfoReqVo erpTaskInfoReqVo);

}
