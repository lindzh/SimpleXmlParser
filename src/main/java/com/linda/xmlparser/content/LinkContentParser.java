package com.linda.xmlparser.content;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linda.xmlparser.param.DefaultParamParser;
import com.linda.xmlparser.param.ParamParser;

public class LinkContentParser implements ContentParser {

	private static String startPrefix = "<";
	private static String endPrefix = ">";

	private ContentHandler handler = new PostContentHandler();

	private ParamParser paramHandler = new DefaultParamParser();

	private void addContent(StringBuilder sb, String str) {
		if (StringUtils.isNotBlank(str)) {
			sb.append(str.trim());
			sb.append(" ");
		}
	}

	public String parserContent(String content) {
		if (content != null) {
			int from = 0;
			StringBuilder sb = new StringBuilder();
			while (from < content.length()) {
				int startIndex = content.indexOf(startPrefix, from);
				int endIndex = content.indexOf(endPrefix, startIndex);
				if (endIndex > startIndex && startIndex >= 0) {
					String sub = content.substring(from, startIndex);
					addContent(sb,sub);
					String element = content.substring(startIndex, endIndex);
					if (element.startsWith("<img")) {
						String image = content.substring(startIndex + 4, endIndex);
						Map<String, String> params = paramHandler.parseParams(image);
						if (params != null) {
							String cc = params.get("title") != null ? params.get("title") : params.get("alt");
							addContent(sb,cc);
						}
					}
					from = endIndex + 1;
				} else if (from >= 0 && from < content.length()) {
					String sub = content.substring(from);
					addContent(sb,sub);
					from = content.length() + 1;
				}
			}
			return handler.handle(sb.toString().trim()).trim();
		}
		return content;
	}

}
