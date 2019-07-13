package service.impl;

import com.xsl.task.vo.*;
import org.springframework.stereotype.Service;
import service.TaskInfoService;

/**
 * @author 梁俊伟
 * @version 1.0
 * @date 2019/5/27 20:01
 */
@Service
public class TaskInfoServiceImpl implements TaskInfoService {
    @Override
    public ResBaseVo UpCategoryHunter(String tagName, Integer type, Integer rows) {
        return null;
    }

    @Override
    public TaskInfoListResVo initTaskInfo(TaskInfoListReqVo taskInfoListReqVo) {
        return null;
    }

    @Override
    public TaskInfoListResVo reloadTaskInfo(TaskInfoListReqVo taskInfoListReqVo) {
        return null;
    }

    @Override
    public TaskInfoResVo taskInfo(String taskId) {
        return null;
    }

    @Override
    public TaskListResVo querySendTask(SendAndRecTaskReqVo sendAndRecTaskReqVo) {
        return null;
    }

    @Override
    public TaskListResVo queryReceiveTask(SendAndRecTaskReqVo sendAndRecTaskReqVo) {
        return null;
    }

    @Override
    public TaskConstantInfoResVo totalTask() {
        return null;
    }

    @Override
    public TaskConstantInfoResVo totalMoney() {
        return null;
    }

    @Override
    public TaskConstantInfoResVo totalDoneTask() {
        return null;
    }

    @Override
    public TaskListResVo getTaskByMasterId(String masterId) {
        return null;
    }
}
