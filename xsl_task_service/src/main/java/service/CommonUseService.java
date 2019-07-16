package service;

import com.xsl.task.vo.HunterInfo;
import com.xsl.task.vo.TaskInfo;
import xsl.pojo.XslTask;

public interface CommonUseService {

    /**
     * 获取猎人信息
     * @param hunterId
     * @return
     */
    HunterInfo getHunterInfo(String hunterId);

    /**
     * 初始化任务信息
     * @param xslTask
     * @return
     */
    TaskInfo initTaskInfo(XslTask xslTask);
}
