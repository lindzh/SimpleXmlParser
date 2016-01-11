package com.linda.xmlparser.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linda.xmlparser.param.DefaultParamParser;
import com.linda.xmlparser.param.ParamParser;
import com.linda.xmlparser.utils.JSONUtils;

/**
 * div[*]{ddd=""ddd}>div[8888]{class="" fff=""}.fffff
 * @author lindezhi
 * 2016年1月5日 上午11:32:07
 */
public class ScriptParser {
	
	private ParamParser parser = new DefaultParamParser();
	
	public XmlScript parse(String script){
		ScriptNode node = null;
		ScriptNode next = null;
		int idx = 0;
		int gt = 0;
		int dot = 0;
		while(gt>=0||dot>=0){
			ScriptNode current = new ScriptNode();
			gt = script.indexOf(">",idx);
			dot = script.indexOf(".",idx);
			int min = 0;
			if(gt>=0&&dot>=0){
				min = Math.min(gt, dot);
			}else{
				min = Math.max(gt, dot);
			}
			int subIdx = script.indexOf("[", idx);
//			if(min>idx){
			if(subIdx>0){
				String str = null;
				if(min>idx){
					str = script.substring(idx, min);
				}else{
					str = script.substring(idx);
				}
				
				if(StringUtils.isNotBlank(str)){
					//>{}>  >{}.
					current.setScript(str);
					int idxStart = str.indexOf("[",0);
					int idxEnd = str.indexOf("]", 0);
					if(idxStart<idxEnd&&idxStart>0){
						String type = str.substring(0, idxStart);
						if(StringUtils.isNotBlank(type)){
							current.setType(type);
						}else{
							throw new RuntimeException("invalid script:"+script);
						}
						//a[0]   []
						String idxScript = str.substring(idxStart+1, idxEnd);
						NodeIndex nodeIndex = this.parseScriptIndex(idxScript);
						current.setIndexes(nodeIndex);
						
						//a[0]{}  {} attribute条件提取
						int attStart = str.indexOf("{", idxEnd);
						int attEnd = str.indexOf("}", idxEnd);
						if(attEnd>attStart&&attStart>0){
							String attScript = str.substring(attStart+1, attEnd);
							if(StringUtils.isNotBlank(attScript)){
								Map<String, String> attr = parser.parseParams(attScript);
								current.setAttributes(attr);
							}else{
								current.setAttributes(new HashMap<String,String>());
							}
							//a[0]{}()  ()//()相对条件提取
							int conStart = str.indexOf("(", attEnd);
							int conEnd = str.indexOf(")", attEnd);
							if(conStart>0&&conEnd>0){
								current.setConditions(new ArrayList<ScriptNodeCondition>());
								String subConditions = str.substring(conStart+1, conEnd);
								if(StringUtils.isNotBlank(str)){
									String[] conditions = subConditions.split(",");
									for(String condition:conditions){
										if(StringUtils.isNotBlank(condition)){
											ScriptNodeCondition scriptNodeCondition = this.parseCondition(condition);
											current.getConditions().add(scriptNodeCondition);
										}
									}
								}else{
									current.setConditions(new ArrayList<ScriptNodeCondition>());
								}
							}else if(conStart<0&&conEnd<0){
								current.setConditions(new ArrayList<ScriptNodeCondition>());
							}else{
								throw new RuntimeException("invalid script:"+script);
							}
						}else{
							if(attStart<0&&attEnd<0){
								current.setAttributes(new HashMap<String,String>());
							}else{
								throw new RuntimeException("invalid script:"+script);
							}
						}
					}else{
						throw new RuntimeException("invalid script:"+script);
					}
				}else{
					throw new RuntimeException("invalid script:"+script);
				}
				
				if(node==null){
					node = current;
					next = node;
				}else{
					next.setNext(current);
					next = current;
					current = null;
				}
				if(min>=0){
					idx = min+1;
				}else{
					break;
				}
			}else{
				//a[0].href  href
				String str = script.substring(idx);
				if(StringUtils.isNotBlank(str)){
					current.setScript(str);
					current.setValue(true);
				}else{
					throw new RuntimeException("invalid script:"+script);
				}
				if(node==null){
					node = current;
					next = node;
				}else{
					next.setNext(current);
					next = current;
					current = null;
				}
				break;
			}
		}
		return new XmlScript(script, node);
	}
	
	/**
	 * span[0]{class=\"\"}?\"\"
	 * @param condition
	 * @return
	 */
	private ScriptNodeCondition parseCondition(String condition){
		ScriptNodeCondition nodeCondition = new ScriptNodeCondition();
		nodeCondition.setScript(condition);
		int start = condition.indexOf("[");
		int end = condition.indexOf("]");
		if(start>0&&end>0){
			String type = condition.substring(0, start);
			nodeCondition.setType(type);
			//[]
			String indexSrc = condition.substring(start+1, end);
			NodeIndex index = this.parseScriptIndex(indexSrc);
			nodeCondition.setIndex(index);
			
			//{}
			int attStart = condition.indexOf("{", end);
			int attEnd = condition.indexOf("}", end);
			if(attStart>0&&attEnd>attStart){
				String attScript = condition.substring(attStart+1, attEnd);
				if(StringUtils.isNotBlank(attScript)){
					Map<String, String> attr = parser.parseParams(attScript);
					nodeCondition.setAttributes(attr);
				}else{
					nodeCondition.setAttributes(new HashMap<String,String>());
				}
			}else{
				if(attStart<0&&attEnd<0){
					nodeCondition.setAttributes(new HashMap<String,String>());
				}else{
					throw new RuntimeException("invalid condition:"+condition);
				}
			}
			int contentIdx = -1;
			if(attEnd>0){
				contentIdx = condition.indexOf("?", attEnd);
			}else{
				contentIdx = condition.indexOf("?", end);
			}
			if(contentIdx>0){
				String content = condition.substring(contentIdx+1).trim();
				if(content.startsWith("\"")&&content.endsWith("\"")){
					content = content.substring(1, content.length()-1);
					nodeCondition.setContent(content);
				}else{
					throw new RuntimeException("invalid condition:"+condition);
				}
			}
		}else{
			throw new RuntimeException("invalid condition:"+condition);
		}
		return nodeCondition;
	}
	
	private NodeIndex parseScriptIndex(String script){
		NodeIndex index = new NodeIndex();
		index.setScript(script);
		if(script.contains("*")){
			index.setAll(true);
		}else{
			String[] split = script.split(",");
			if(split!=null){
				for(String str:split){
					if(StringUtils.isNotBlank(str)){
						if(str.contains("-")){
							String[] split2 = str.split("-");
							if(split2.length==2){
								if(StringUtils.isBlank(split2[0])){
									int idx = Integer.parseInt(str);
									if(idx<0){
										index.setLast(true);
									}else{
										index.getIndexes().add(idx);
									}
								}else{
									int start = Integer.parseInt(split2[0]);
									int end = Integer.parseInt(split2[1]);
									if(start>end){
										for(int i=start;i<=end;i++){
											index.getIndexes().add(i);
										}
									}else{
										throw new RuntimeException("invalid index script:"+script);
									}
								}
							}else{
								throw new RuntimeException("invalid index script:"+script);
							}
						}else{
							int idx = Integer.parseInt(str);
							if(idx<0){
								index.setLast(true);
							}else{
								index.getIndexes().add(idx);
							}
						}
					}else{
						throw new RuntimeException("invalid index script:"+script);
					}
				}
			}else{
				throw new RuntimeException("invalid index script:"+script);
			}
		}
		return index;
	}
	
	public static void main(String[] args) {
		String src = "li[*]{class=\"list_item\"}>a[0]>img[0].src";
		ScriptParser scriptParser = new ScriptParser();
		XmlScript parse = scriptParser.parse(src);
		System.out.println(JSONUtils.toJSON(parse));
	}
}
