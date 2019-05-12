package vo;

import java.io.Serializable;

public class CreateOrderReqVo implements Serializable {
	private String taskId;
	private String hunterId;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getHunterId() {
		return hunterId;
	}

	public void setHunterId(String hunterId) {
		this.hunterId = hunterId;
	}
}
