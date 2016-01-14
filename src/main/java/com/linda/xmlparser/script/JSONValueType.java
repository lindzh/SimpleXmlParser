package com.linda.xmlparser.script;

/**
 * 
 * @author lindezhi
 * 2016年1月14日 下午2:43:24
 */
public enum JSONValueType {
	
	String,
	//"next":"a[*]{class=\"aNxt\"}?contains\"下一页\".href",
	
	List_String,
	//"nexts":["a[*]{class=\"aNxt\"}?contains\"下一页\".href"],
	
	Object,
//	{
//		"image":"li[*]{class=\"list_item\"}>a[0]{class=\"img_content\"}>img[0]{class=\"thumbnail\"}.src",
//		"name":"li[*]{class=\"list_item\"}>div[0]{class=\"details\"}>div[0]{class=\"t_b\"}>a[0]{class=\"t\"}",
//		"url":"li[*]{class=\"list_item\"}>div[0]{class=\"details\"}>div[0]{class=\"t_b\"}>a[0]{class=\"t\"}.href",
//		"address":"li[*]{class=\"list_item\"}>div[0]{class=\"details\"}>p[0]",
//		"finishDate":"li[*]{class=\"list_item\"}>div[0]{class=\"details\"}>p[1]",
//		"location":"li[*]{class=\"list_item\"}>div[0]{class=\"details\"}>p[2]>a[0]?contains\"查看地图\".href",
//		"price":"li[*]{class=\"list_item\"}>span[0]{class=\"price\"}>span[0]{class=\"sp1\"}"
//	}
	
	List_Object
//	"areas":[
//	 		{
//	 			"name":"li[*]{class=\"cl_item\"}>span[*]{class=\"con\"}(span[0]{class=\"tit\"}?contains\"区域\")>a[*]{}?ncontains\"不限\"",
//	 			"url":"li[*]{class=\"cl_item\"}>span[*]{class=\"con\"}(span[0]{class=\"tit\"}?contains\"区域\")>a[*]{}?ncontains\"不限\".href"
//	 		}
//	 	]

}
