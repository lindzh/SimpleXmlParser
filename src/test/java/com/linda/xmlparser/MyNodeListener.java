package com.linda.xmlparser;

import java.util.Map;

import com.linda.xmlparser.listener.Node;
import com.linda.xmlparser.listener.NodeListener;

public class MyNodeListener implements NodeListener {

	public void onNode(Node node) {
		String name = node.getName();
		Map<String, String> params = node.getAttributes();
		String content = node.getContent();
		System.out.println("------------ node:" + name + " ---------------");
		if (params != null) {
			System.out.println(params.toString());
		}
		System.out.println(content);
		System.out.println("------------------------------------------------");
	}

	@Override
	public void setDocContext(Object doc) {
		// TODO Auto-generated method stub
		
	}

}
