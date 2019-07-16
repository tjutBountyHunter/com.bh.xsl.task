package com.xsl.task.vo;

public class ErpTaskInfoReqVo extends ReqBaseVo{
	private Integer page;
	private Integer rows;
	private Integer id;
	private Byte State;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Byte getState() {
		return State;
	}

	public void setState(Byte state) {
		State = state;
	}
}
