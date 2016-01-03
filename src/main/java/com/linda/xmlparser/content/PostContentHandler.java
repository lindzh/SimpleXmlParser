package com.linda.xmlparser.content;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.linda.xmlparser.utils.FileUtils;

/**
 * 处理特殊字符
 * 
 * @author hzlindzh
 * 
 */
public class PostContentHandler implements ContentHandler {

	private Map<String, String> charMap = new HashMap<String, String>();
	private boolean inited = false;

	private void init() {
		// 常用字符
		charMap.put("&#34;", "\"");
		charMap.put("&quot;", "\"");
		charMap.put("&#38;", "&");
		charMap.put("&amp;", "&");
		charMap.put("&#60;", "<");
		charMap.put("&lt;", "<");
		charMap.put("&#62;", ">");
		charMap.put("&gt;", ">");
		charMap.put("&#160;", " ");
		charMap.put("&nbsp;", " ");
		charMap.put("\r\n", " ");
		charMap.put("     ", "");
		inited = true;
		// TODO其他字符处理
	}

	public String handle(String content) {
		if (!inited) {
			init();
		}
		if (content != null) {
			Set<String> keys = charMap.keySet();
			for (String key : keys) {
				content = content.replaceAll(key, charMap.get(key));
			}
		}
		return content;
	}

	public static void main(String[] args) {
		String body = FileUtils.toString("D:\\test\\xml\\char.txt");
		ContentHandler handler = new PostContentHandler();
		System.out.println(handler.handle(body));
	}

}
