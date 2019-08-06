package service;

import com.xsl.task.vo.*;

/**
 * @author 梁俊伟
 * @version 1.0
 * @date 2019/5/27 20:52
 */
public interface TaskOperateService {

    /**
     * 发送任务
     *
     * @return
     */
    ResBaseVo sendTask(TaskReqVo taskReqVo);


    /**
     * 接收任务
     * @return
     */
    ResBaseVo receiveTask(RecTaskReqVo recTaskReqVo);


    /**
     * 猎人确认任务完成
     * @return
     */
    ResBaseVo confirmTask(ConfirmTaskReqVo confirmTaskReqVo);

    /**
     * 任务搜索
     * @param searchTaskReqVo
     * @return
     */
    SearchTaskInfoListResVo searchTask(SearchTaskReqVo searchTaskReqVo);

    /**
     * 取消任务
     * @param taskId
     * @return
     */
    ResBaseVo cancelTask(String taskId);

}
