package com.linda.xmlparser;

import java.util.Map;

import com.linda.xmlparser.listener.Node;
import com.linda.xmlparser.listener.NodeListener;

/**
 * benchmark listener
 * @author lindezhi
 * 2016年1月5日 上午10:47:14
 */
public class MyNodeListener implements NodeListener {

	int count = 0;

	@Override
	public void onNode(Node node) {
		String name = node.getName();
		Map<String, String> params = node.getAttributes();
		String content = node.getContent();
		count++;
	}

	@Override
	public void setDocContext(Object doc) {

	}

	public int getCounts() {
		return count;
	}
}
