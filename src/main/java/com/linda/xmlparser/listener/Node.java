package com.linda.xmlparser.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * html 或者xml 节点
 * @author lindezhi
 * 2016年1月4日 下午10:47:15
 */
public class Node {

	/**
	 * 节点类型如<div>haha</div>节点类型为div
	 */
	private String name;

	/**
	 * 节点属性,如<a href="abx" class="alink">abc</a>
	 * 节点a的属性包含href和class
	 */
	private Map<String, String> attributes;

	/**
	 * 节点中的文本信息
	 * 如<string>hahah<string>
	 * hahah就是content
	 */
	private String content;

	/**
	 * 父节点
	 * <div>
	 * 	<a href="aaa">ggg</a>
	 * </div>
	 * a节点的父节点为div
	 */
	private Node parent;
	
	/**
	 * 子节点列表
	 */
	private List<Node> children;
	
	private Map<String,List<Node>> childrenMap = new HashMap<String,List<Node>>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	
	public String getAttribute(String key){
		return attributes!=null?attributes.get(key):null;
	}
	
	public void setAttribute(String key,String value){
		if(attributes==null){
			attributes = new HashMap<String,String>();
		}
		attributes.put(key, value);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public List<Node> getChildren() {
		return children;
	}
	
	public List<Node> getChildren(String name) {
		return childrenMap.get(name);
	}
	
	public void addChild(Node child){
		if(children==null){
			children = new ArrayList<Node>();
		}
		children.add(child);
		
		List<Node> list = childrenMap.get(child.getName());
		if(list==null){
			list = new ArrayList<Node>();
			childrenMap.put(child.getName(), list);
		}
		list.add(child);
	}
}
