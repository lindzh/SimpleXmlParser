package com.linda.xmlparser;

import java.util.Map;

import org.apache.commons.lang.time.StopWatch;

import com.linda.xmlparser.core.DefaultHtmlParser;
import com.linda.xmlparser.core.XmlParser;
import com.linda.xmlparser.listener.Node;
import com.linda.xmlparser.listener.NodeListener;
import com.linda.xmlparser.param.ParamParser;
import com.linda.xmlparser.utils.FileUtils;

/**
 * benchmark
 * @author lindezhi
 * 2016年1月5日 上午10:47:04
 */
public class MyHtmlParserTest implements NodeListener{
	
	int count = 0;
	
	public static void main(String[] args) {
		MyHtmlParserTest parserTest = new MyHtmlParserTest();
		String file = "D:\\test\\html\\new163.htm";
		String content = FileUtils.toString(file);
		XmlParser parser = new DefaultHtmlParser();
		parser.setParamParser(new ParamParser(){

			@Override
			public Map<String, String> parseParams(String params) {
				return null;
			}});
		parser.addListener(parserTest);
		StopWatch watch = new StopWatch();
		watch.start();
		parser.parse(content);
		watch.stop();
		System.out.println("my cost:"+watch.getTime());
		System.out.println("count:"+parserTest.getCount());
	}

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
	
	public int getCount(){
		return count;
	}
}
