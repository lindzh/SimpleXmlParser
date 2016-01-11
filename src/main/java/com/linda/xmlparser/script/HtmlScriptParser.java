package com.linda.xmlparser.script;

import java.util.List;
import java.util.Map;

import com.linda.xmlparser.utils.JSONUtils;

/**
 * 
 * 
 {
next:a[*]{class="aNxt"}.href
beans:[
	{
price li[*]{class="list_item"}>a[0]>img[0]
name li[*]{class="list_item"}>div[1]{class="details"}>ddd[0]>a[0]
address li[*]{class="list_item"}>div[1]{class="details"}>ddd[1]>p[0]
address li[*]{class="list_item"}>div[1]{class="details"}>ddd[*]>p[*]
date li[*]{class="list_item"}>div[1]{class="details"}>ddd[2]>p[0]
price li[*]{class="list_item"}>span[2]{class="price"}>span[0]
	}
]
}

 * @author lindezhi
 * 2016年1月5日 下午10:19:04
 */
public class HtmlScriptParser {
	
	public String parse(String content,String schema){
		Object json = JSONUtils.fromJSON(schema);
		if(json instanceof List){
			List li = (List)json;
			for(Object item:li){
				Map<String,Object> i = (Map<String,Object>)item;
				
				
				
				
				
				
			}
		}else{
			
			
		}
		return "";
	}

}
