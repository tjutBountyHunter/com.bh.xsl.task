package vo;

import java.io.Serializable;

public class XslTagVo implements Serializable {
	private String tagName;
	private String tagid;

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagid() {
		return tagid;
	}

	public void setTagid(String tagid) {
		this.tagid = tagid;
	}
}
