package com.linda.xmlparser.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang.StringUtils;


/**
 * 
 * @author lindezhi
 * 2016年1月5日 下午4:31:34
 */
public class JSONUtils {

	private static final JsonConfig config = new JsonConfig();


	@SuppressWarnings("rawtypes")
	private static void toCollections(JSONObject obj, Map map) {
		Set set = obj.keySet();
		for (Object key : set) {
			String keyStr = (String) key;
			Object value = obj.get(key);
			if (value instanceof JSONObject) {
				Map objectValue = new HashMap();
				toCollections((JSONObject) value, objectValue);
				map.put(keyStr, objectValue);
			} else if (value instanceof JSONArray) {
				ArrayList list = new ArrayList();
				toCollections((JSONArray) value, list);
				map.put(keyStr, list);
			} else {
				map.put(keyStr, value);
			}
		}
	}

	private static void toCollections(JSONArray array, List list) {
		if (array != null) {
			Iterator it = array.iterator();
			while (it.hasNext()) {
				Object value = it.next();
				if (value instanceof JSONObject) {
					Map objectValue = new HashMap();
					toCollections((JSONObject) value, objectValue);
					list.add(objectValue);
				} else if (value instanceof JSONArray) {
					ArrayList valueList = new ArrayList();
					toCollections((JSONArray) value, valueList);
					list.add(valueList);
				} else {
					list.add(value);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static Object fromJSON(String jsonString) {
		if (StringUtils.isBlank(jsonString)) {
			return null;
		}
		jsonString = jsonString.trim();
		if (jsonString.startsWith("{")) {
			JSONObject object = JSONObject.fromObject(jsonString, config);
			HashMap map = new HashMap();
			toCollections(object, map);
			return map;
		} else if (jsonString.startsWith("[")) {
			ArrayList list = new ArrayList();
			toCollections(JSONArray.fromObject(jsonString, config), list);
			return list;
		} else {
			throw new JSONException("not standard json");
		}
	}

	public static String toJSON(Object obj) {
		if (obj instanceof Collection||obj.getClass().isArray()) {
			JSONArray jsonArray = JSONArray.fromObject(obj);
			return jsonArray.toString();
		}else{
			JSONObject jsonObject = JSONObject.fromObject(obj);
			return jsonObject.toString();
		}
	}
}
