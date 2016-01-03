package com.linda.xmlparser.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.linda.xmlparser.content.ContentParser;
import com.linda.xmlparser.index.EndIndex;
import com.linda.xmlparser.info.PreInfo;
import com.linda.xmlparser.listener.NodeListener;
import com.linda.xmlparser.param.ParamParser;

public abstract class XmlParser {

	protected List<NodeListener> listeners = new LinkedList<NodeListener>();

	protected Map<String, NodeListener> mapListeners = new HashMap<String, NodeListener>();

	protected ParamParser paramParser;

	protected ContentParser contentParser;

	protected EndIndex endIndex;

	protected List<Pair<String, String>> escapeValues = new LinkedList<Pair<String, String>>();

	private PreInfo info;

	public void setInfo(PreInfo info) {
		this.info = info;
	}

	public void addNodeListener(String type, NodeListener listener) {
		mapListeners.put(type, listener);
	}

	public void addListener(NodeListener listener) {
		listeners.add(listener);
	}

	public void executeParse(String content) {
		content = preParse(content);
		if (info != null) {
			info.onInfo(content);
		}
		parse(content);
	}

	public void setParamParser(ParamParser paramParser) {
		this.paramParser = paramParser;
	}

	public void setContentParser(ContentParser contentParser) {
		this.contentParser = contentParser;
	}

	public void addEscape(Pair<String, String> pair) {
		this.escapeValues.add(pair);
	}

	protected abstract void parse(String content);

	protected abstract String preParse(String content);

	public void fireNodeListener(String type, Map<String, String> params, String content) {
		for (NodeListener listener : listeners) {
			listener.onNode(type, params, content);
		}
		NodeListener nodeListener = mapListeners.get(type);
		if (nodeListener != null) {
			nodeListener.onNode(type, params, content);
		}
	}

	public void setEndIndex(EndIndex endIndex) {
		this.endIndex = endIndex;
	}

	public Collection<NodeListener> getMapListeners() {
		return mapListeners.values();
	}

}
