package com.linda.xmlparser.script;

import java.util.Map;

public class JSONScript {

	private String json;

	private JSONValueType valueType;

	// string，list string定义
	private JSONNode string;

	// object list object定义
	private Map<String, JSONNode> object;

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public JSONValueType getValueType() {
		return valueType;
	}

	public void setValueType(JSONValueType valueType) {
		this.valueType = valueType;
	}

	public JSONNode getString() {
		return string;
	}

	public void setString(JSONNode string) {
		this.string = string;
	}

	public Map<String, JSONNode> getObject() {
		return object;
	}

	public void setObject(Map<String, JSONNode> object) {
		this.object = object;
	}

}
