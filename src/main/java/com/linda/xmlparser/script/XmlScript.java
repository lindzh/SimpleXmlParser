package com.linda.xmlparser.script;

import java.util.List;

import com.linda.xmlparser.exception.XmlException;

public class XmlScript {

	private String src;

	private ScriptNode script;

	private String type;

	public XmlScript(String src, ScriptNode script) {
		this.src = src;
		this.script = script;
		this.genType();
	}

	public ScriptNode getScript() {
		return script;
	}

	public void setScript(ScriptNode script) {
		this.script = script;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private void genType() {
		ScriptNode last = script;
		boolean multi = false;
		while (last!=null) {
			multi |= last.isMulti();
//			multi |= this.isMulti(last);
			last = last.getNext();
		}
		if(multi){
			type = "list";
		}else{
			type = "string";
		}
	}

	private boolean isMulti(ScriptNode node) {
		if (node.isContent()||node.getNext()!=null) {//取content值
			if (node.getIndexes().isAll()) {
				return true;
			} else {// 非所有，获取部分数据
				NodeIndex index = node.getIndexes();
				if (index != null) {
					List<Integer> list = index.getIndexes();
					boolean lst = index.isLast();
					int count = list != null ? list.size() : 0;
					count += lst ? 1 : 0;
					return count > 1;
				} else {
					throw new XmlException("index can't be null with xml content");
				}
			}
		} else {
			return false;
		}
	}

	public boolean isList() {
		return type != null && type.equals("list");
	}

	public boolean isString() {
		return type != null && type.equals("string");
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}
}
