package com.linda.xmlparser;

import org.junit.Test;

import com.linda.xmlparser.content.DefaultContentParser;
import com.linda.xmlparser.core.DefaultXmlParser;
import com.linda.xmlparser.core.Pair;
import com.linda.xmlparser.core.XmlParser;
import com.linda.xmlparser.index.SimpleEndIndex;
import com.linda.xmlparser.param.DefaultParamParser;
import com.linda.xmlparser.utils.FileUtils;

public class XmlTest {
	@Test
	public void testXml() {
		String file = "D:\\test\\xml\\xml.txt";
		String content = FileUtils.toString(file);
		XmlParser parser = new DefaultXmlParser();
		parser.addListener(new MyNodeListener());
		parser.parse(content);
	}

}
