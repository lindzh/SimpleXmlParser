package com.linda.xmlparser.core;

import java.util.List;
import java.util.Map;

import com.linda.xmlparser.content.DefaultContentParser;
import com.linda.xmlparser.exception.XmlException;
import com.linda.xmlparser.index.SimpleEndIndex;
import com.linda.xmlparser.param.DefaultParamParser;
import com.linda.xmlparser.utils.FileUtils;

public class DefaultXmlParser extends XmlParser {

	public DefaultXmlParser(){
		setEndIndex(new SimpleEndIndex());
		setParamParser(new DefaultParamParser());
		setContentParser(new DefaultContentParser());
		addEscape(new Pair<String,String>("<?xml","?>"));
		addEscape(new Pair<String,String>("<!--","-->"));
		addEscape(new Pair<String,String>("<#--","-->"));
	}
	
	public DefaultXmlParser(List<Pair<String, String>> escapeValues){
		this();
		if(escapeValues!=null&&escapeValues.size()>0){
			for(Pair<String, String> pair:escapeValues){
				addEscape(pair);
			}
		}
	}
	
	@Override
	public void parse(String content) {
		if (content != null) {
			parseTxt(content, 0, content.length());
		}
	}

	private void parseTxt(String txt, int from, int end) {
		int preFrom = txt.indexOf("<", from);
		int preEnd = txt.indexOf(">", from);
		while (preFrom >= from && preEnd >= from && preEnd > preFrom && preEnd < end && from < end) {
			String type = null;
			Map<String, String> paramMap = null;
			String content = null;
			int blankIndex = txt.indexOf(" ", preFrom);
			int paramEnd = preEnd;
			boolean hasParam = false;
			if (blankIndex > preFrom && blankIndex < preEnd) {
				hasParam = true;
				paramEnd = preEnd - 1;
				String param = txt.substring(blankIndex, paramEnd + 1);
				paramMap = paramParser.parseParams(param);
				type = txt.substring(preFrom + 1, blankIndex);
			}
			char ch = txt.charAt(preEnd - 1);
			if (ch == '/') {
				if (!hasParam) {
					type = txt.substring(preFrom + 1, preEnd - 1);
				}
				from = preEnd + 1;
			} else if (ch != '/') {
				if (!hasParam) {
					type = txt.substring(preFrom + 1, preEnd);
				}
				// 得到内容
				int contentEnd = endIndex.getEnd(type, txt, preEnd + 1, end);
				if (contentEnd > 0) {
					content = txt.substring(preEnd + 1, contentEnd);
					from = contentEnd + type.length() + 3;
				} else {
					throw new XmlException("not standard xml!");
				}
			}
			super.fireNodeListener(type, paramMap, content);
			if (isXml(content)) {
				parseTxt(content, 0, content.length());
			}
			if (from < end) {
				preFrom = txt.indexOf("<", from);
				preEnd = txt.indexOf(">", from);
			} else {
				break;
			}
		}
	}

	private boolean isXml(String content) {
		if (content != null && content.indexOf("<") > 0) {
			return true;
		}else{
			return false;
		}
	}

	@Override
	public String preParse(String content) {
		if (escapeValues != null && escapeValues.size() > 0 && content != null) {
			for (Pair<String, String> pair : escapeValues) {
				StringBuffer buffer = new StringBuffer(content);
				int start = buffer.indexOf(pair.getStart());
				int end = buffer.indexOf(pair.getEnd());
				while (start >= 0 && end > 0) {
					if (start > end) {
						throw new XmlException("escape not supported from:" + pair.getStart() + " end:" + pair.getEnd());
					}
					buffer = buffer.replace(start, end + pair.getEnd().length(), "");
					start = buffer.indexOf(pair.getStart());
					end = buffer.indexOf(pair.getEnd());
				}
				content = buffer.toString();
			}
		}
		return content;
	}

	public static void main(String[] args) {
		String body = FileUtils.toString("D:\\test\\xml\\escape.txt");
		XmlParser parser = new DefaultXmlParser();
		parser.addEscape(new Pair<String, String>("<!--", "-->"));
		parser.addEscape(new Pair<String, String>("<?", "?>"));
		parser.addEscape(new Pair<String, String>("<#--", "-->"));
		String content = parser.preParse(body);
		System.out.println(content);
	}
}
