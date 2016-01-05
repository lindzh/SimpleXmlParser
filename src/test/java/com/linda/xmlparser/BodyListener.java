package com.linda.xmlparser;

import java.util.Map;

import com.linda.xmlparser.content.ContentParser;
import com.linda.xmlparser.content.DefaultContentParser;
import com.linda.xmlparser.listener.Node;
import com.linda.xmlparser.listener.NodeListener;
import com.linda.xmlparser.utils.FileUtils;

public class BodyListener implements NodeListener {


	public void onNode(Node node) {
		String name = node.getName();
		Map<String, String> params = node.getAttributes();
		String body = node.getContent();
		if (body != null) {
			System.out.println(body);
			FileUtils.toFile(XmlPathConfig.BASE+XmlPathConfig.BODY, body);
		}
	}

	@Override
	public void setDocContext(Object doc) {
		// TODO Auto-generated method stub
		
	}
}
