package service.impl;

import com.xsl.task.vo.ErpTaskInfoReqVo;
import com.xsl.task.vo.PageObject;
import com.xsl.task.vo.Task;
import mapper.XslTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.ErpTaskInfoService;
import service.TaskApproveService;
import xsl.pojo.XslTask;

/**
 * @author 梁俊伟
 * @version 1.0
 * @date 2019/5/27 21:06
 */
@Service
public class TaskApproveServiceImpl implements TaskApproveService {

    @Autowired
    private ErpTaskInfoService erpTaskInfoService;
    @Autowired
    private XslTaskMapper xslTaskMapper;

    /* 任务服务 */
    Logger logger = LoggerFactory.getLogger(TaskApproveServiceImpl.class);

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
