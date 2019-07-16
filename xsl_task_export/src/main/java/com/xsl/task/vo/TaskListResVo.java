package com.xsl.task.vo;

import java.util.List;

/**
 * @author 梁俊伟
 * @version 1.0
 * @date 2019/5/31 21:25
 */
public class TaskListResVo extends ResBaseVo{

    private List<SendRecTaskVo> sendRecTaskVoList;

    public TaskListResVo() {
    }

    public List<SendRecTaskVo> getSendRecTaskVoList() {
        return sendRecTaskVoList;
    }

    public void setSendRecTaskVoList(List<SendRecTaskVo> sendRecTaskVoList) {
        this.sendRecTaskVoList = sendRecTaskVoList;
    }
}
