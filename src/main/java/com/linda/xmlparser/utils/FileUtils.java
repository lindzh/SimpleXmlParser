package com.linda.xmlparser.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class FileUtils {

	private static Logger logger = Logger.getLogger(FileUtils.class);

	public static String toString(String file) {
		try {
			File fileName = new File(file);
			return IOUtils.toString(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			logger.warn("file not exist!" + file);
		} catch (IOException e) {
			logger.error("file:" + file + " io error");
		}
		return null;
	}

	public static void toFile(String filePath, String content) {
		if (filePath == null || content == null) {
			return;
		}
		File file = new File(filePath);
		try {
			FileOutputStream outputStream = new FileOutputStream(file);
			IOUtils.write(content.getBytes(), outputStream);
		} catch (FileNotFoundException e) {
			logger.warn("file not exist!" + filePath);
		} catch (IOException e) {
			logger.error("file:" + filePath + " io error");
		}
	}
	
	public static void append(String filePath, String content) {
		try {
			RandomAccessFile file = new RandomAccessFile(filePath, "rw");
			file.seek(file.length());
			file.write(content.getBytes("gbk"));
//			file.writeBytes("\r\n");
			file.close();
		} catch (FileNotFoundException e) {
			logger.warn("file not exist!" + filePath);
		} catch (IOException e) {
			logger.error("file:" + filePath + " io error");
		}
	}
}
