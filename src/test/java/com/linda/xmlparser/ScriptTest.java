package com.linda.xmlparser;

import java.util.List;

import com.linda.xmlparser.core.DefaultHtmlParser;
import com.linda.xmlparser.script.JSONScriptParser;
import com.linda.xmlparser.script.ScriptParser;
import com.linda.xmlparser.script.XmlScript;
import com.linda.xmlparser.script.XmlScriptParser;
import com.linda.xmlparser.utils.FileUtils;
import com.linda.xmlparser.utils.JSONUtils;

public class ScriptTest {
	
	public static void haha(String[] args) {
		XmlScriptParser parser = new XmlScriptParser();
		parser.setParser(new DefaultHtmlParser());
		
		String file = FileUtils.toString("d:\\anjuke.html");
//		String script = "a[*]{class=\"aNxt\"}.href";
		String script = "li[*]{class=\"list_item\"}>div[0]{class=\"details\"}>p[1]?contains\"竣工日期\"";
		ScriptParser scriptParser = new ScriptParser();
		XmlScript xmlScript = scriptParser.parse(script);
		System.out.println(JSONUtils.toJSON(xmlScript));
		List<String> txt = parser.parseList(xmlScript, file);
		System.out.println(JSONUtils.toJSON(txt));
	}
	
	public static  void main(String[] args){
		String file = FileUtils.toString("d:\\news.html");
		String schema = FileUtils.toString("d:\\news.json");
		XmlScriptParser parser = new XmlScriptParser();
		parser.setParser(new DefaultHtmlParser());
		parser.setJsonScriptParser(new JSONScriptParser());
		String parse = parser.parse(file, schema);
		System.out.println(parse);
	}
	
	public static void anjuke(String[] args) {
		String file = FileUtils.toString("d:\\anjuke.html");
		String schema = FileUtils.toString("d:\\jsonHtml.json");
		XmlScriptParser parser = new XmlScriptParser();
		parser.setParser(new DefaultHtmlParser());
		parser.setJsonScriptParser(new JSONScriptParser());
		String parse = parser.parse(file, schema);
		System.out.println(parse);
	}
}
