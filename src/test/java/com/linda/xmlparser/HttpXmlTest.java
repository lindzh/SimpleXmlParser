package com.linda.xmlparser;

import com.linda.xmlparser.content.DefaultContentParser;
import com.linda.xmlparser.core.DefaultHtmlParser;
import com.linda.xmlparser.core.Pair;
import com.linda.xmlparser.core.XmlParser;
import com.linda.xmlparser.index.SimpleEndIndex;
import com.linda.xmlparser.info.SimpleInfo;
import com.linda.xmlparser.param.DefaultParamParser;

public class HttpXmlTest {

	private static XmlParser parser;

	static {
		parser = createHtmlParser();
	}

//	public void testHttpAndXml() {
//		String url = "news.163.com";
//		System.out.println("============start==============");
//		HttpResponseMeta responseMeta = WebHttpUtils.httpGet(url, null, null);
//		if (responseMeta.getCode() == 200) {
//			String content = responseMeta.getResponseAsString();
//			FileUtils.toFile(XmlPathConfig.BASE+XmlPathConfig.HTML, content);
//			parser.executeParse(content);
//		}
//	}

	public void testContentParser() {
		DefaultContentParser contentParser = new DefaultContentParser();
	}

	private static XmlParser createHtmlParser() {
		XmlParser parser = new DefaultHtmlParser();
		parser.setContentParser(new DefaultContentParser());
		parser.setEndIndex(new SimpleEndIndex());
		parser.setParamParser(new DefaultParamParser());
		parser.addNodeListener("title", new TitleListener());
		parser.addNodeListener("body", new BodyListener());
		parser.addNodeListener("a", new LinkListener());
		parser.addEscape(new Pair<String, String>("<!DOCTYPE", ">"));
		parser.addEscape(new Pair<String, String>("<?", "?>"));
		parser.addEscape(new Pair<String, String>("<!--", "-->"));
		parser.addEscape(new Pair<String, String>("<style >", "</style>"));
		parser.addEscape(new Pair<String, String>("<style>", "</style>"));
		parser.addEscape(new Pair<String, String>("<style ", "</style>"));
		parser.addEscape(new Pair<String, String>("<script >", "</script>"));
		parser.addEscape(new Pair<String, String>("<script>", "</script>"));
		parser.addEscape(new Pair<String, String>("<script", "</script>"));
		parser.setInfo(new SimpleInfo());
		return parser;
	}

	public static void main(String[] args) {
		HttpXmlTest test = new HttpXmlTest();
//		test.testHttpAndXml();
	}
}
