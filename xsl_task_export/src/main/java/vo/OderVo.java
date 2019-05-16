package vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OderVo implements Serializable {
    private String oderId;
    private String sendName;
    private String receiveName;
    private String taskName;
    private BigDecimal money;
    private int state;
    private Date startDate;
    private Date endDate;

    public void setOderId(String oderId) {
        this.oderId = oderId;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    public String getOderId() {
        return oderId;
    }

    public String getSendName() {
        return sendName;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public String getTaskName() {
        return taskName;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public int getState() {
        return state;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }


}
