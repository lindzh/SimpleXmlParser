package com.linda.xmlparser;

import com.linda.xmlparser.core.DefaultHtmlParser;
import com.linda.xmlparser.listener.Node;
import com.linda.xmlparser.listener.NodeListener;
import com.linda.xmlparser.utils.FileUtils;

public class HtmlInnerTest {
	
	public static void main(String[] args) {
		String string = FileUtils.toString("d:\\inner.html");
		DefaultHtmlParser parser = new DefaultHtmlParser();
		parser.addNodeListener("p", new NodeListener(){
			public void onNode(Node node) {
				String txt = node.getTxt();
				System.out.println(txt);
			}

			public void setDocContext(Object doc) {}
		});
		
		parser.parse(string);
		
		
		
		
	}

}
