package com.xsl.task.vo;

import java.util.List;

/**
 * @author 梁俊伟
 * @version 1.0
 * @date 2019/7/11 15:24
 */
public class SearchTaskInfoListResVo extends ResBaseVo{
    List<TaskInfo> taskInfoList;

    public List<TaskInfo> getTaskInfoList() {
        return this.taskInfoList;
    }

    public void setTaskInfoList(List<TaskInfo> taskInfoList) {
        this.taskInfoList = taskInfoList;
    }
}
