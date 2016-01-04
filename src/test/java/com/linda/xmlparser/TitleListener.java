package com.linda.xmlparser;

import java.util.Map;

import com.linda.xmlparser.listener.Node;
import com.linda.xmlparser.listener.NodeListener;
import com.linda.xmlparser.utils.FileUtils;

public class TitleListener implements NodeListener {

	public void onNode(Node node) {
		String name = node.getName();
		Map<String, String> params = node.getAttributes();
		String content = node.getContent();
		FileUtils.toFile(XmlPathConfig.BASE+XmlPathConfig.TITLE, "title:"+content);
	}

	@Override
	public void setDocContext(Object doc) {
		// TODO Auto-generated method stub
		
	}

}
