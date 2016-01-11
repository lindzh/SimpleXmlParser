package com.linda.xmlparser.script;

import java.util.Map;

public class ScriptNodeCondition {
	
	private String script;

	//类型
	private String type;

	//索引
	private NodeIndex index;

	//attribute条件
	private Map<String, String> attributes;
	
	//包含内容
	private String content;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public NodeIndex getIndex() {
		return index;
	}

	public void setIndex(NodeIndex index) {
		this.index = index;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
}
