package service;

import com.xsl.task.vo.ResBaseVo;

public interface CancelTask {

    /**
     * 过期任务自动取消
     * @return ResBaseVo
     */
    ResBaseVo cancelTaskDDL();
}
