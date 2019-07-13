package com.xsl.task.vo;

import java.util.List;

/**
 * @author 梁俊伟
 * @version 1.0
 * @date 2019/5/31 21:25
 */
public class TaskListResVo extends ResBaseVo{

    private List<Task> taskList;

    public TaskListResVo() {
    }

    public List<Task> getTaskList() {
        return this.taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

}
