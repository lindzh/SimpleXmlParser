package com.linda.xmlparser.script;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linda.xmlparser.param.DefaultParamParser;
import com.linda.xmlparser.param.ParamParser;

/**
 * div[*]{ddd=""ddd}>div[8888]{class="" fff=""}.fffff
 * @author lindezhi
 * 2016年1月5日 上午11:32:07
 */
public class ScriptParser {
	
	private ParamParser parser = new DefaultParamParser();
	
	public ScriptNode parse(String script){
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
			
			if(min>idx){
				String str = script.substring(idx, min);
				if(StringUtils.isNotBlank(str)){
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
						
						String idxScript = str.substring(idxStart+1, idxEnd);
						NodeIndex nodeIndex = this.parseScriptIndex(idxScript);
						current.setIndexes(nodeIndex);
						
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
						}else{
							throw new RuntimeException("invalid script:"+script);
						}
					}else{
						throw new RuntimeException("invalid script:"+script);
					}
				}else{
					throw new RuntimeException("invalid script:"+script);
				}
				idx = min+1;
				
				if(node==null){
					node = current;
					next = node;
				}else{
					next.setNext(current);
					next = current;
					current = null;
				}
			}else{
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
		return node;
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
}
