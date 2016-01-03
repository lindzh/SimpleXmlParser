package com.linda.xmlparser.listener;

import java.util.Map;

public interface NodeListener {

	public void onNode(String name,Map<String,String> params,String content);
	
	public void setDocContext(Object doc);
	
}
