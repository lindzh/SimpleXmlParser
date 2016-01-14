package com.linda.xmlparser.script;

import java.util.List;
import java.util.Map;

import com.linda.xmlparser.exception.XmlException;

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
	 * 取值条件
	 */
	private List<ScriptNodeCondition> conditions;
	
	/**
	 * content内容
	 */
	private String content;
	
	/**
	 * 操作符，默认contains
	 */
	private String operate;
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

	/**
	 * 取attribute中的值
	 * @return
	 */
	public boolean isValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}
	
	/**
	 * 取content中的值
	 * @return
	 */
	public boolean isContent(){
		return next==null&&!this.isValue();
	}
	
	public boolean isMulti(){
		if(this.indexes!=null){
			if (this.indexes.isAll()) {
				return true;
			} else {// 非所有，获取部分数据
				if (indexes != null) {
					List<Integer> list = indexes.getIndexes();
					boolean lst = indexes.isLast();
					int count = list != null ? list.size() : 0;
					count += lst ? 1 : 0;
					return count > 1;
				} else {
					throw new XmlException("index can't be null with xml content");
				}
			}
		}else{
			return false;
		}
	}

	public List<ScriptNodeCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<ScriptNodeCondition> conditions) {
		this.conditions = conditions;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOperate() {
		return operate;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}
}
