package com.linda.xmlparser.content;

import org.apache.commons.lang.StringUtils;

import com.linda.xmlparser.utils.FileUtils;

/**
 * 内容抓取
 * @author lindezhi
 * 2016年1月4日 下午10:54:21
 */
public class DefaultContentParser implements ContentParser {

	private static String startPrefix = "<";
	private static String endPrefix = ">";

	private ContentHandler handler = new PostContentHandler();

	public String parserContent(String content) {
		if (content != null) {
			int from = 0;
			StringBuilder sb = new StringBuilder();
			while (from < content.length()) {
				int startIndex = content.indexOf(startPrefix, from);
				int endIndex = content.indexOf(endPrefix, startIndex);
				if (endIndex > startIndex && startIndex >= 0) {
					String sub = content.substring(from, startIndex);
					if(StringUtils.isNotBlank(sub)){
						sb.append(sub.trim());
						sb.append(" ");
					}
					from = endIndex + 1;
				} else if (from >= 0 && from < content.length()) {
					String sub = content.substring(from);
					if(StringUtils.isNotBlank(sub)){
						sb.append(sub.trim());
						sb.append(" ");
					}
					from = content.length() + 1;
				}
			}
			return handler.handle(sb.toString().trim()).trim();
		}
		return content;
	}

	private String parse(String content) {
		if (content != null) {
			int from = 0;
			StringBuilder sb = new StringBuilder();
			int startIndex = content.indexOf(startPrefix, from);
			int endIndex = 0;
			if (startIndex >= 0) {
				endIndex = content.indexOf(endPrefix, startIndex);
				while (from < content.length() && startIndex >= 0) {
					String trim = content.substring(from, startIndex).trim();
					if (StringUtils.isNotBlank(trim)) {
						sb.append(trim);
						sb.append(" ");
					}
					if (endIndex > startIndex) {
						from = endIndex + 1;
					}
					startIndex = content.indexOf(startPrefix, from);
					endIndex = content.indexOf(endPrefix, from);
				}
			} else {
				sb.append(content);
			}
			return handler.handle(sb.toString().trim());
		}
		return content;
	}

	public static void main(String[] args) {
		String body = FileUtils.toString("D:\\test\\xml\\content.txt");
		ContentParser parser = new DefaultContentParser();
		String content = parser.parserContent(body);
		System.out.println(content);
	}
}
