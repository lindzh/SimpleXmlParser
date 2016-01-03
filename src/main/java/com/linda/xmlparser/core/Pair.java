package com.linda.xmlparser.core;

public class Pair<A, B> {
	private A start;
	private B end;

	public Pair(A start, B end) {
		this.start = start;
		this.end = end;
	}

	public A getStart() {
		return start;
	}

	public void setStart(A start) {
		this.start = start;
	}

	public B getEnd() {
		return end;
	}

	public void setEnd(B end) {
		this.end = end;
	}

}
