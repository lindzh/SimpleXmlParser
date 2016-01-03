package com.linda.xmlparser.param;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class DefaultParamParser implements ParamParser {

	public Map<String, String> parseParams(String params) {
		if (params != null) {
			params = params.trim();
			int from = 0;
			Map<String,String> map = new HashMap<String,String>();
			while(from<params.length())
			{
				int eqIndex = params.indexOf("=", from);
				if (eqIndex > 0) {
					String key = params.substring(from, eqIndex);
					int valueStart = params.indexOf("\"", eqIndex);
					int valueEnd = -1;
					if (valueStart < 0) {
						valueStart = params.indexOf("'", eqIndex);
						if (valueStart > 0) {
							valueEnd = params.indexOf("'", valueStart + 1);
						}

					} else {
						valueEnd = params.indexOf("\"", valueStart + 1);
					}
					if (valueEnd < 0 || valueStart < 0) {
						break;
					}
					String value = params.substring(valueStart+1, valueEnd);
					key = key.trim();
					value = value.trim();
					map.put(key, value);
					from = valueEnd+1;
				}else{
					break;
				}
			}
			return map;
		}
		return null;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		File file = new File("D:\\test\\xml\\params.txt");
		String params =IOUtils.toString(new FileInputStream(file));
		ParamParser paramParser = new DefaultParamParser();
		Map<String, String> map = paramParser.parseParams(params);
		System.out.println(map.toString());
	}

}
