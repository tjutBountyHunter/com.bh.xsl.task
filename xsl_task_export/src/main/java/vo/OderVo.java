package vo;

import com.fasterxml.jackson.annotation.JsonFormat;

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
    private String startDate;
    private String endDate;

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

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public String getStartDate() {
        return startDate;
    }
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public String getEndDate() {
        return endDate;
    }


}
