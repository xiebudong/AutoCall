/**
* @Title: 		com.targetv.tools.StringUtils.java 
* @Description: the String utility. 
* @author 		Ken Lee
* @date 		2012-11-29 上午11:36:25
*/
package com.fgc.autocall.Tools;


import android.content.Context;

public class StringTools {
	private StringTools(){};
	
	static public String getFileNameFromFileUrl(String url){
		if(url == null || -1 == url.lastIndexOf("."))
			return null;
		
		int pos = url.lastIndexOf("/");
		if(pos == -1)
			return null;
		return new String(url.substring(pos+1));
	}
	static public String getFileNamePostFixFromFileUrl(String url){
		if(url == null || -1 == url.lastIndexOf("."))
			return null;

		int pos = url.lastIndexOf("/");
		if(pos == -1)
			return null;
		
		String fileName = url.substring(pos+1);
		pos = fileName.lastIndexOf(".");
		if(pos == -1)
			return null;
		return new String(fileName.substring(pos+1));
	}
	
	static public boolean IsEmpty(String str){
		if(str == null || str.length() <= 0)
			return true;
		
		return false;
	}
	
}
