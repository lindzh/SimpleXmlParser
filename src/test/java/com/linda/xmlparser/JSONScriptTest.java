package com.linda.xmlparser;

import com.linda.xmlparser.script.JSONScript;
import com.linda.xmlparser.script.JSONScriptParser;
import com.linda.xmlparser.utils.FileUtils;
import com.linda.xmlparser.utils.JSONUtils;

public class JSONScriptTest {

	public static void main(String[] args) {
		JSONScriptParser parser = new JSONScriptParser();
		String schema = FileUtils.toString("d:\\jsonHtml.json");
		System.out.println(schema);
		JSONScript script = parser.parse(schema);
		System.out.println(JSONUtils.toJSON(script));
	}
	
}
