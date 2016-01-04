package com.linda.xmlparser;

import com.linda.xmlparser.info.PreInfo;
import com.linda.xmlparser.utils.FileUtils;

public class SimpleInfo2 implements PreInfo{

	public void onInfo(String preContent) {
		FileUtils.toFile(XmlPathConfig.BASE+"pre.htm", preContent);
	}

}
