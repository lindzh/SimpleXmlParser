package com.linda.xmlparser.script;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author lindezhi
 * 2016年1月14日 下午12:05:17
 */
public class ScriptMatcherRegister {
	
	private static Map<String,ScriptMatcher> operateMatchers = new HashMap<String,ScriptMatcher>();
	
	static{
		operateMatchers.put("contains", new ContainMatcher());
		operateMatchers.put("ncontains", new NotContainMatcher());
		operateMatchers.put("regex", new RegexMatcher());
	}
	
	public static ScriptMatcher getMatcher(String operate){
		ScriptMatcher matcher = operateMatchers.get(operate);
		if(matcher!=null){
			return matcher;
		}else{
			throw new RuntimeException("invalid script operate:"+operate);
		}
	}

	private static class ContainMatcher implements ScriptMatcher{
		@Override
		public boolean match(String content, String exp) {
			if(StringUtils.isNotBlank(content)&&StringUtils.isNotBlank(exp)){
				return content.contains(exp);
			}
			return false;
		}
	}
	
	private static class NotContainMatcher implements ScriptMatcher{
		@Override
		public boolean match(String content, String exp) {
			if(StringUtils.isNotBlank(content)&&StringUtils.isNotBlank(exp)){
				return !content.contains(exp);
			}
			return false;
		}
	}
	
	private static class RegexMatcher implements ScriptMatcher{
		
		@Override
		public boolean match(String content, String exp) {
			if(StringUtils.isNotBlank(content)&&StringUtils.isNotBlank(exp)){
				Pattern pattern = Pattern.compile(exp);
				Matcher matcher = pattern.matcher(content);
				return matcher.matches();
			}
			return false;
		}
	}
	
}
