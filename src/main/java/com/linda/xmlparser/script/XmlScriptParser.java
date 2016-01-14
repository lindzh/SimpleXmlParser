package com.linda.xmlparser.script;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

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
	public List<String> parseList(XmlScript script,String content){
		final ArrayList<String> results = new ArrayList<String>();
		
		final ScriptNode srcnode = script.getScript();
		String type = srcnode.getType();
		parser.addNodeListener(type, new NodeListener() {
			public void setDocContext(Object doc) {}
			@Override
			public void onNode(Node node) {
				boolean matchAttributes = matchAttributes(node,srcnode.getAttributes());
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
					List<String> values = genScriptValue(next,children,node);
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
	public String parseTxt(XmlScript script,String content){
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
	private List<String> genScriptValue(ScriptNode node,List<Node> elements,Node parent){
		ArrayList<String> results = new ArrayList<String>();
		if(elements!=null){
			if(node.isValue()){
				for(Node ele:elements){
					results.add(this.genValue(node, ele));
				}
			}else{
				List<Node> matchNodes = this.matchAttrAndIndexAndConditions(node, elements,parent);
				if(node.isContent()){
					for(Node ele:matchNodes){
						results.add(this.genValue(node, ele));
					}
				}else{
					ScriptNode next = node.getNext();
					if(next.isValue()){
						for(Node mele:matchNodes){
							String value = this.genValue(next, mele);
							results.add(value);
						}
					}else{
						for(Node mele:matchNodes){
							List<Node> children = mele.getChildren(next.getType());
							List<String> list = this.genScriptValue(next, children,mele);
							results.addAll(list);
						}
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
	private List<Node> matchAttrAndIndexAndConditions(ScriptNode node,List<Node> elements,Node parent){
		List<Node> fixNodes = this.fixIndex(node.getIndexes(), elements);
		List<Node> nodes = this.matchAttributes(fixNodes, node.getAttributes());
		if(this.matchConditions(node, parent)){
			nodes = this.filterNodeCondition(node, nodes);
			return nodes;
		}else{
			return Collections.emptyList();
		}
	}
	
	private List<Node> filterNodeCondition(ScriptNode node,List<Node> elements){
		if(node==null||StringUtils.isNotBlank(node.getContent())||CollectionUtils.isEmpty(elements)){
			return elements;
		}
		ArrayList<Node> nodes = new ArrayList<Node>();
		ScriptMatcher matcher = ScriptMatcherRegister.getMatcher(node.getOperate());
		for(Node ele:elements){
			if(matcher.match(ele.getContent(), node.getContent())){
				nodes.add(ele);
			}
		}
		return nodes;
	}
	
	private List<Node> fixIndex(NodeIndex index,List<Node> elements){
		ArrayList<Node> nodes = new ArrayList<Node>();
		if(elements!=null&&index!=null){
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
			nodes.addAll(fixNodes);
		}
		return nodes;
	}
	
	private int size(List<Node> nodes,NodeIndex index){
		if(nodes==null||nodes.size()==0||index==null){
			return 0;
		}
		if(index.isAll()){
			return nodes.size();
		}
		HashSet<Integer> set = new HashSet<Integer>();
		if(index.isLast()){
			set.add(nodes.size()-1);
		}
		List<Integer> idxes = index.getIndexes();
		if(idxes!=null){
			set.addAll(idxes);
		}
		return set.size();
	}
	
	private boolean matchConditions(ScriptNode node,Node parent){
		List<ScriptNodeCondition> conditions = node.getConditions();
		if(conditions!=null){
			for(ScriptNodeCondition condition:conditions){
				List<Node> nodes = parent.getChildren(condition.getType());
				List<Node> fixNodes = this.fixIndex(condition.getIndex(), nodes);
				if(nodes!=null){
					int size = this.size(nodes, condition.getIndex());
					if(size!=fixNodes.size()){
						return false;
					}
					List<Node> fixAtts = this.matchAttributes(fixNodes, condition.getAttributes());
					if(fixAtts.size()!=fixNodes.size()){
						return false;
					}
					String content = condition.getContent();
					if(StringUtils.isNotBlank(content)){
						ScriptMatcher matcher = ScriptMatcherRegister.getMatcher(condition.getOperate());
						for(Node fix:fixAtts){
							String cc = fix.getContent();
							if(StringUtils.isNotBlank(cc)){
								if(!matcher.match(cc, content)){
									return false;
								}
							}else{
								return false;
							}
						}
					}
				}else{
					return false;
				}
			}
		}
		return true;
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

	private List<Node> matchAttributes(Collection<Node> nodes,Map<String,String> att){
		ArrayList<Node> result = new ArrayList<Node>();
		if(nodes!=null&&att!=null&&att.size()>0){
			for(Node node:nodes){
				if(this.matchAttributes(node, att)){
					result.add(node);
				}
			}
		}else if(att==null||att.size()<1){
			if(nodes!=null&&nodes.size()>0){
				result.addAll(nodes);
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
	private boolean matchAttributes(Node elem,Map<String, String> attributes){
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

	public XmlParser getParser() {
		return parser;
	}

	public void setParser(XmlParser parser) {
		this.parser = parser;
	} 
}
