package com.linda.xmlparser;

import java.util.Map;

import com.linda.xmlparser.content.ContentParser;
import com.linda.xmlparser.content.LinkContentParser;
import com.linda.xmlparser.listener.Node;
import com.linda.xmlparser.listener.NodeListener;
import com.linda.xmlparser.utils.FileUtils;

public class LinkListener implements NodeListener {

	private ContentParser parser = new LinkContentParser();

	public void onNode(Node node) {
		String name = node.getName();
		Map<String, String> params = node.getAttributes();
		String content = node.getContent();
		if (params != null) {
			String linkName = parser.parserContent(content);
			if (linkName == null) {
				linkName = params.get("title");
			}
			String href = params.get("href");
			if (linkName != null && href != null) {
				FileUtils.append(XmlPathConfig.BASE + XmlPathConfig.LINK, "href:" + href + " name:" + linkName);
			}
		}
	}

	@Override
	public void setDocContext(Object doc) {
		// TODO Auto-generated method stub
		
	}

}
