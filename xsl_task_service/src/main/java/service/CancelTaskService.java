package service;

import com.xsl.task.vo.ResBaseVo;

public interface CancelTaskService {

    /**
     * 过期任务自动取消
     * @return ResBaseVo
     */
    ResBaseVo cancelTaskDDL();
}
