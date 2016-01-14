package com.linda.xmlparser;

import java.util.List;

import com.linda.xmlparser.core.DefaultHtmlParser;
import com.linda.xmlparser.script.ScriptParser;
import com.linda.xmlparser.script.XmlScript;
import com.linda.xmlparser.script.XmlScriptParser;
import com.linda.xmlparser.utils.FileUtils;
import com.linda.xmlparser.utils.JSONUtils;

public class ScriptTest {
	
	public static void main(String[] args) {
		XmlScriptParser parser = new XmlScriptParser();
		parser.setParser(new DefaultHtmlParser());
		
		String file = FileUtils.toString("d:\\anjuke.html");
//		String script = "a[*]{class=\"aNxt\"}.href";
		String script = "li[*]{class=\"cl_item\"}>span[*]{class=\"con\"}(span[0]{class=\"tit\"}?contains\"区域\")>a[*]{}?ncontains\"不限\"";
		ScriptParser scriptParser = new ScriptParser();
		XmlScript xmlScript = scriptParser.parse(script);
		System.out.println(JSONUtils.toJSON(xmlScript));
		List<String> txt = parser.parseList(xmlScript, file);
		System.out.println(JSONUtils.toJSON(txt));
	}
}
