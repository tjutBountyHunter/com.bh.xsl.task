package com.xsl.task.vo;

import java.io.Serializable;
import java.util.List;

public class TagReqVo implements Serializable {
	private Integer tagNum;
	private List<String> obtainedTags;
	private String tagName;

	public Integer getTagNum() {
		return tagNum;
	}

	public void setTagNum(Integer tagNum) {
		this.tagNum = tagNum;
	}

	public List<String> getObtainedTags() {
		return obtainedTags;
	}

	public void setObtainedTags(List<String> obtainedTags) {
		this.obtainedTags = obtainedTags;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
}
