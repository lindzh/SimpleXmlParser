package com.linda.xmlparser.script;

import java.util.Map;

/**
 * div[*]{ddd=""ddd}
 * div[8888-5644,2]{class="" fff=""}
 * fffff
 * @author lindezhi
 * 2016年1月5日 下午12:00:24
 */
public class ScriptNode {
	
	/**
	 * 取值
	 */
	private boolean value;
	
	/**
	 * 脚本
	 */
	private String script;
	
	/**
	 * 类型
	 */
	private String type;
	
	/**
	 * 属性过滤
	 */
	private Map<String,String> attributes;
	
	/**
	 * 索引
	 */
	private NodeIndex indexes;
	
	/**
	 * 下一级
	 */
	private ScriptNode next;
	
	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public NodeIndex getIndexes() {
		return indexes;
	}

	public void setIndexes(NodeIndex indexes) {
		this.indexes = indexes;
	}

	public ScriptNode getNext() {
		return next;
	}

	public void setNext(ScriptNode next) {
		this.next = next;
	}

	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
	
	public boolean isContent(){
		return next==null&&!this.isValue();
	}
}
