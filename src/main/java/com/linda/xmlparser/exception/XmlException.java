package com.linda.xmlparser.exception;

/**
 * 
 * @author lindezhi
 * 2016年1月4日 下午10:52:27
 */
public class XmlException extends RuntimeException {

	private static final long serialVersionUID = -6013000648012336299L;

	public XmlException() {

	}

	public XmlException(String message) {
		super(message);
	}

	public XmlException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	

}
