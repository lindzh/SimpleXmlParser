package com.linda.xmlparser.script;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.linda.xmlparser.core.XmlParser;
import com.linda.xmlparser.exception.XmlException;
import com.linda.xmlparser.listener.Node;
import com.linda.xmlparser.listener.NodeListener;
import com.linda.xmlparser.utils.JSONUtils;

/**
 * 
 * 
 {
next:a[*]{class="aNxt"}.href
beans:[
	{
price li[*]{class="list_item"}>a[0]>img[0]
name li[*]{class="list_item"}>div[1]{class="details"}>ddd[0]>a[0]
address li[*]{class="list_item"}>div[1]{class="details"}>ddd[1]>p[0]
address li[*]{class="list_item"}>div[1]{class="details"}>ddd[*]>p[*]
date li[*]{class="list_item"}>div[1]{class="details"}>ddd[2]>p[0]
price li[*]{class="list_item"}>span[2]{class="price"}>span[0]
	}
]
}

 * @author lindezhi
 * 2016年1月5日 下午10:19:04
 */
public class XmlScriptParser {
	
	private XmlParser parser;
	
	public String parse(String content,String schema){
		Object json = JSONUtils.fromJSON(schema);
		if(json instanceof List){
			List li = (List)json;
			ArrayList<Object> list = new ArrayList<Object>();
			for(Object item:li){
				Map<String,Object> i = (Map<String,Object>)item;
				
				
				
				
				
				
			}
		}else{
			
			
		}
		return "";
	}
	
	/**
	 * 取值，结果为列表
	 * @param script
	 * @param content
	 * @return
	 */
	private List<String> parseList(XmlScript script,String content){
		final ArrayList<String> results = new ArrayList<String>();
		
		final ScriptNode srcnode = script.getScript();
		String type = srcnode.getType();
		parser.addNodeListener(type, new NodeListener() {
			public void setDocContext(Object doc) {}
			@Override
			public void onNode(Node node) {
				Map<String, String> attributes = node.getAttributes();
				boolean matchAttributes = matchAttributes(node,srcnode);
				if(!matchAttributes){
					return;
				}
				if(srcnode.isContent()){
					results.add(node.getContent());
				}
				
				ScriptNode next = srcnode.getNext();
				
				if(next.isValue()){
					results.add(node.getAttribute(next.getScript()));
				}else{
					List<Node> children = node.getChildren(next.getType());
					List<String> values = genScriptValue(next,children);
					results.addAll(values);
				}
			}
		});
		parser.parse(content);
		return results;
	}
	
	/**
	 * 取值，结果为一个
	 * @param script
	 * @param content
	 * @return
	 */
	private String parseTxt(XmlScript script,String content){
		List<String> results = this.parseList(script, content);
		if(results!=null&&results.size()>0){
			return results.get(0);
		}
		return null;
	}

	/**
	 * 取第一个div且第一个div的class=details的
	 * div[1]{class="details"}>ddd[0]>a[0]
	 * 获取节点值列表
	 * @param node
	 * @param elements
	 * @return
	 */
	private List<String> genScriptValue(ScriptNode node,List<Node> elements){
		ArrayList<String> results = new ArrayList<String>();
		if(elements!=null){
			if(node.isValue()){
				for(Node ele:elements){
					results.add(this.genValue(node, ele));
				}
			}else{
				List<Node> matchNodes = this.matchAttrAndIndex(node, elements);
				if(node.isContent()){
					for(Node ele:matchNodes){
						results.add(this.genValue(node, ele));
					}
				}else{
					ScriptNode next = node.getNext();
					for(Node mele:matchNodes){
						List<Node> children = mele.getChildren(next.getType());
						List<String> list = this.genScriptValue(next, children);
						results.addAll(list);
					}
				}
			}
		}
		return results;
	}
	
	/**
	 * 返回匹配本节点的列表
	 * @param node
	 * @param elements
	 * @return
	 */
	private List<Node> matchAttrAndIndex(ScriptNode node,List<Node> elements){
		ArrayList<Node> nodes = new ArrayList<Node>();
		if(elements!=null){
			NodeIndex index = node.getIndexes();
			Set<Node> fixNodes = new HashSet<Node>();
			if(index.isAll()){
				fixNodes.addAll(elements);
			}else{
				List<Integer> indexes = index.getIndexes();
				boolean last = index.isLast();
				for(int idx:indexes){
					if(elements.size()>idx){
						Node node2 = elements.get(idx);
						fixNodes.add(node2);
					}
				}
				if(last&&elements.size()>0){
					fixNodes.add(elements.get(elements.size()-1));
				}
			}
			
			return this.matchNodes(fixNodes, node);
		}
		return nodes;
	}
	
	/**
	 * 是否可取值
	 * @param node
	 * @return
	 */
	private boolean isValue(ScriptNode node){
		return node.isContent()||node.isValue();
	}
	
	/**
	 * 取值
	 * @param srcNode
	 * @param node
	 * @return
	 */
	private String genValue(ScriptNode srcNode,Node node){
		if(srcNode.isContent()){
			return node.getContent();
		}
		if(srcNode.isValue()){
			return node.getAttribute(srcNode.getScript());
		}
		throw new XmlException("unknown value to get");
	}

	private List<Node> matchNodes(Collection<Node> nodes,ScriptNode srcNode){
		ArrayList<Node> result = new ArrayList<Node>();
		for(Node node:nodes){
			if(this.matchAttributes(node, srcNode)){
				result.add(node);
			}
		}
		return result;
	}
	
	/**
	 * 查看attribute是否匹配
	 * @param elem
	 * @param node
	 * @return
	 */
	private boolean matchAttributes(Node elem,ScriptNode node){
		Map<String, String> attributes = node.getAttributes();
		//attribute 检查
		if(attributes!=null&&attributes.size()>0){
			Map<String, String> nodeAttr = elem.getAttributes();
			if(nodeAttr!=null&&nodeAttr.size()>=attributes.size()){
				Set<String> keys = attributes.keySet();
				for(String key:keys){
					String value = attributes.get(key);
					String nodeValue = nodeAttr.get(key);
					if(nodeValue!=null){
						if(!nodeValue.equals(value)){
							return false;
						}
					}else{
						return false;
					}
				}
			}else{
				return false;
			}
		}
		return true;
	} 
}
