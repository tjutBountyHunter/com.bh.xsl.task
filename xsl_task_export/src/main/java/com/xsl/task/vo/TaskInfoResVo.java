package com.xsl.task.vo;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TaskInfoResVo extends ResBaseVo{
	private Integer cid;

	private String taskId;

	private String taskTitle;

	private String content;

	private BigDecimal money;

	private String masterId;

	private Byte state;

	private Date createDate;

	private Date updatedate;

	private Date deadLineDate;

	private String sourcetype;

	private List<Tag> tags;

	private HunterInfo hunterInfo;

	private MasterInfo masterInfo;

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskTitle() {
		return taskTitle;
	}

	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getMasterId() {
		return masterId;
	}

	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}

	public Date getDeadLineDate() {
		return deadLineDate;
	}

	public void setDeadLineDate(Date deadLineDate) {
		this.deadLineDate = deadLineDate;
	}

	public String getSourcetype() {
		return sourcetype;
	}

	public void setSourcetype(String sourcetype) {
		this.sourcetype = sourcetype;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public HunterInfo getHunterInfo() {
		return hunterInfo;
	}

	public void setHunterInfo(HunterInfo hunterInfo) {
		this.hunterInfo = hunterInfo;
	}

	public MasterInfo getMasterInfo() {
		return masterInfo;
	}

	public void setMasterInfo(MasterInfo masterInfo) {
		this.masterInfo = masterInfo;
	}
}
