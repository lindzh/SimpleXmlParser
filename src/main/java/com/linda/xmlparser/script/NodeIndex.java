package com.linda.xmlparser.script;

import java.util.ArrayList;
import java.util.List;

/**
 * [*] [-1] [1-3,444] [4,6,7]
 * @author lindezhi
 * 2016年1月5日 上午11:58:24
 */
public class NodeIndex {
	
	private boolean isAll;
	
	private String script;
	
	private boolean last;
	
	private List<Integer> indexes = new ArrayList<Integer>();

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public List<Integer> getIndexes() {
		return indexes;
	}

	public void setIndexes(List<Integer> indexes) {
		this.indexes = indexes;
	}

	public boolean isAll() {
		return isAll;
	}

	public void setAll(boolean isAll) {
		this.isAll = isAll;
	}
}
