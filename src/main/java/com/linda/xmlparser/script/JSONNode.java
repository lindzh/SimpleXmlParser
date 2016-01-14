package com.linda.xmlparser.script;

import java.util.Map;

/**
 * 
 * @author lindezhi
 * 2016年1月14日 下午2:43:17
 */
public class JSONNode {

	// 名称
	private String name;

	/**
	 * 脚本
	 */
	private XmlScript script;

	/**
	 * 值类型
	 */
	private JSONValueType valueType;
	
	/**
	 * 如果value是对象或者对象数组时
	 * 对象定义
	 */
	private Map<String,JSONNode> object;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public XmlScript getScript() {
		return script;
	}

	public void setScript(XmlScript script) {
		this.script = script;
	}

	public JSONValueType getValueType() {
		return valueType;
	}

	public void setValueType(JSONValueType valueType) {
		this.valueType = valueType;
	}

	public Map<String, JSONNode> getObject() {
		return object;
	}

	public void setObject(Map<String, JSONNode> object) {
		this.object = object;
	}
}
