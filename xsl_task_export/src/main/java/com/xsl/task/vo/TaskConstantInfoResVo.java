package com.xsl.task.vo;

import java.math.BigDecimal;

/**
 * @author 梁俊伟
 * @version 1.0
 * @date 2019/7/13 19:12
 */
public class TaskConstantInfoResVo extends ResBaseVo {
    /**总任务量*/
    private int totalTask;
    /**任务总金额*/
    private BigDecimal totalMoney;
    /**所有完成任务数*/
    private int totalDoneTask;

    public int getTotalTask() {
        return totalTask;
    }

    public void setTotalTask(int totalTask) {
        this.totalTask = totalTask;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public int getTotalDoneTask() {
        return totalDoneTask;
    }

    public void setTotalDoneTask(int totalDoneTask) {
        this.totalDoneTask = totalDoneTask;
    }
}
