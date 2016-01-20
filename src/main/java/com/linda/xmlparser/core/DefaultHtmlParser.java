package com.linda.xmlparser.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.linda.xmlparser.content.DefaultContentParser;
import com.linda.xmlparser.index.SimpleEndIndex;
import com.linda.xmlparser.listener.Node;
import com.linda.xmlparser.param.DefaultParamParser;

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
		
		int fromStartIdx = from;
		
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
				//前缀加入
				String preTxt = txt.substring(fromStartIdx, preFrom);
				if(StringUtils.isNotBlank(preTxt)){
					node.getTxtBuilder().append(preTxt.trim());
				}
				node.addChild(nn);
			}
			nn.setContent(content);
			if (isXml(content)) {
				parseTxt(content, 0, content.length(),nn);
				if(node!=null){
					node.getTxtBuilder().append(nn.getTxt());
				}
			}else{
				if(this.contentParser!=null){
					String cc = this.contentParser.parserContent(nn.getContent());
					nn.setContent(cc);
					
					if(node!=null&&cc!=null){
						node.getTxtBuilder().append(cc.trim());
					}
				}
			}
			super.fireNodeListener(nn);
			
			fromStartIdx = from;
			if (from < end) {
				preFrom = txt.indexOf("<", from);
				preEnd = txt.indexOf(">", from);
			} else {
				break;
			}
		}
		//后缀加入
		if(preEnd<end){
			String str = txt.substring(from, end);
			if(node!=null){
				node.getTxtBuilder().append(str.trim());
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
