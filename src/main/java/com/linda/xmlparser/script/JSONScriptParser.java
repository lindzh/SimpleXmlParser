package com.linda.xmlparser.script;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.linda.xmlparser.utils.JSONUtils;

/**
 * 
 * @author lindezhi
 * 2016年1月14日 下午3:32:17
 */
public class JSONScriptParser {
	
	private ScriptParser parser = new ScriptParser();
	
	@SuppressWarnings("unchecked")
	public JSONScript parse(String jsonSchema){
		JSONScript script = new JSONScript();
		Object json = JSONUtils.fromJSON(jsonSchema);
		if(json instanceof List){
			List li = (List)json;
			Object object = li.get(0);
			if(object instanceof String){
				JSONNode node = this.parseListString(null, (String)object);
				JSONScript jsonScript = new JSONScript();
				jsonScript.setJson(jsonSchema);
				jsonScript.setValueType(JSONValueType.List_String);
				jsonScript.setString(node);
				return jsonScript;
			}else if(object instanceof Map){
				Map<String, JSONNode> map = this.parseObject(null, (Map<String,Object>)object);
				JSONScript jsonScript = new JSONScript();
				jsonScript.setJson(jsonSchema);
				jsonScript.setValueType(JSONValueType.List_Object);
				jsonScript.setObject(map);
				return jsonScript;
			}else{
				throw new RuntimeException("invalid item type in list:"+jsonSchema);
			}
		}else if(json instanceof Map){
			Map<String, JSONNode> map = this.parseObject(null, (Map<String,Object>)json);
			JSONScript jsonScript = new JSONScript();
			jsonScript.setJson(jsonSchema);
			jsonScript.setValueType(JSONValueType.Object);
			jsonScript.setObject(map);
			return jsonScript;
		}else{
			throw new RuntimeException("invalid schema:"+jsonSchema);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String,JSONNode> parseObject(String name,Map<String,Object> map){
		HashMap<String, JSONNode> nodeMap = new HashMap<String,JSONNode>();
		Set<String> keys = map.keySet();
		for(String key:keys){
			Object value = map.get(key);
			if(value instanceof String){
				JSONNode node = this.parseString(key, (String)value);
				nodeMap.put(key, node);
			}else if(value instanceof Map){
				Map<String, JSONNode> mapValue = this.parseObject(key, (Map<String,Object>)value);
				JSONNode node = new JSONNode();
				node.setName(key);
				node.setObject(mapValue);
				node.setValueType(JSONValueType.Object);
				nodeMap.put(key, node);
			}else if(value instanceof List){
				List li = (List)value;
				Object object = li.get(0);
				if(object instanceof String){
					JSONNode node = this.parseListString(key, (String)object);
					nodeMap.put(key, node);
				}else if(object instanceof Map){
					Map<String, JSONNode> mapValue = this.parseObject(key, (Map<String,Object>)object);
					JSONNode node = new JSONNode();
					node.setName(key);
					node.setObject(mapValue);
					node.setValueType(JSONValueType.List_Object);
					nodeMap.put(key, node);	
				}else{
					throw new RuntimeException("invalid schema");
				}
			}
		}
		return nodeMap;
	}
	
	private JSONNode parseListString(String name,String script){
		JSONNode node = this.parseString(name, script);
		node.setValueType(JSONValueType.List_String);
		return node;
	}
	
	private JSONNode parseString(String name,String script){
		JSONNode node = new JSONNode();
		node.setName(name);
		node.setValueType(JSONValueType.String);
		XmlScript xmlScript = parser.parse(script);
		node.setScript(xmlScript);
		return node;
	}
}
