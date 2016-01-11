package com.linda.xmlparser.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.linda.xmlparser.content.DefaultContentParser;
import com.linda.xmlparser.index.SimpleEndIndex;
import com.linda.xmlparser.listener.Node;
import com.linda.xmlparser.param.DefaultParamParser;
import com.linda.xmlparser.utils.FileUtils;

/**
 * html parser
 * @author lindezhi
 * 2016年1月4日 下午10:53:06
 */
public class DefaultHtmlParser extends XmlParser {

	public DefaultHtmlParser(){
		setEndIndex(new SimpleEndIndex());
		setParamParser(new DefaultParamParser());
		setContentParser(new DefaultContentParser());
		addEscape(new Pair<String, String>("<!DOCTYPE", ">"));
		addEscape(new Pair<String, String>("<?", "?>"));
		addEscape(new Pair<String, String>("<!--", "-->"));
		addEscape(new Pair<String, String>("<style >", "</style>"));
		addEscape(new Pair<String, String>("<style>", "</style>"));
		addEscape(new Pair<String, String>("<style ", "</style>"));
		addEscape(new Pair<String, String>("<script >", "</script>"));
		addEscape(new Pair<String, String>("<script>", "</script>"));
		addEscape(new Pair<String, String>("<script", "</script>"));
	}
	
	public DefaultHtmlParser(List<Pair<String, String>> escapeValues){
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
			content = this.preParse(content);
			parseTxt(content, 0, content.length(),null);
		}
	}

	private void parseTxt(String txt, int from, int end,Node node) {
		int preFrom = txt.indexOf("<", from);
		int preEnd = txt.indexOf(">", from);
		while (preFrom >= from && preEnd >= from && preEnd > preFrom && preEnd < end && from < end) {
			String type = null;
			Map<String, String> paramMap = new HashMap<String,String>();
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
					from = preEnd + 1;
				}
			}
			Node nn = new Node();
			nn.setName(type);
			nn.setAttributes(paramMap);
			nn.setParent(node);
			if(node!=null){
				node.addChild(nn);
			}
			nn.setContent(content);
			if (isXml(content)) {
				parseTxt(content, 0, content.length(),nn);
			}else{
				if(this.contentParser!=null){
					nn.setContent(this.contentParser.parserContent(nn.getContent()));
				}
			}
			super.fireNodeListener(nn);
			if (from < end) {
				preFrom = txt.indexOf("<", from);
				preEnd = txt.indexOf(">", from);
			} else {
				break;
			}
		}
	}

	private boolean isXml(String content) {
		if (content != null && content.indexOf("<")>=0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected String preParse(String content) {
		if (escapeValues != null && escapeValues.size() > 0 && content != null) {
			StringBuffer buffer = new StringBuffer(content);
			for (Pair<String, String> pair : escapeValues) {
				int start = buffer.indexOf(pair.getStart());
				int end = buffer.indexOf(pair.getEnd(),start);
				while (start >= 0 && end > 0) {
					buffer = buffer.replace(start, end + pair.getEnd().length(), "");
					start = buffer.indexOf(pair.getStart());
					end = buffer.indexOf(pair.getEnd(),start);
				}
			}
			content = buffer.toString();
		}
		return content;
	}
}
