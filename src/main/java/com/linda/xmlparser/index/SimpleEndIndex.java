package com.linda.xmlparser.index;

import com.linda.xmlparser.utils.FileUtils;

/**
 * html或者xml结束
 * @author lindezhi
 * 2016年1月4日 下午10:52:05
 */
public class SimpleEndIndex implements EndIndex {

	public int getEnd(String type, String txt, int from, int end) {
		String endStr = "</" + type;
		String startStr = "<" + type;
		String endFix = "/>";
		int endIndex = -1;
		int startIndex = from;
		int endFixIndex = from;
		int stack = 1;
		while (stack > 0) {
			if (startIndex <= from && startIndex >= 0) {
				startIndex = txt.indexOf(startStr, from);
			}
			if (endFixIndex <= from && endFixIndex >= 0) {
				endFixIndex = txt.indexOf(endFix, from);
			}
			if (endIndex <= from) {
				endIndex = txt.indexOf(endStr, from);
				if (endIndex < 0) {
					return -1;
				}
			}
			if (startIndex < endIndex && startIndex >=0) {
				stack++;
				from = startIndex + 1;
				if (endFixIndex > startIndex && endFixIndex < endIndex) {
					if (hasNode(txt, type, startIndex + type.length(), endFixIndex)) {
						from = startIndex + type.length();
					} else {
						stack--;
						from = endFixIndex + 1;
					}
				}
			} else {
				stack--;
				from = endIndex + type.length() + 2;
			}
			if (from >= end) {
				break;
			}
		}
		return endIndex;
	}

	private boolean hasNode(String txt, String type, int from, int end) {
		int startIndex = txt.indexOf("<" + type, from);
		if (startIndex < 0 || startIndex > end) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		String str = FileUtils.toString("D:\\test\\xml\\xml.txt");
		SimpleEndIndex index = new SimpleEndIndex();
		System.out.println(index.getEnd("project", str, 0, str.length()));
	}
}
