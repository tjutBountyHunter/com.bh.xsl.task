package vo;

public class ErpTaskInfoReqVo extends ReqBaseVo{
	private Integer page;
	private Integer rows;
	private Integer key;
	private Byte key1;

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

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	public Byte getKey1() {
		return key1;
	}

	public void setKey1(Byte key1) {
		this.key1 = key1;
	}
}
