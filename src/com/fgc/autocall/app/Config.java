/**
* @Title: 		com.targetv.client.app.Config.java 
* @Description: TODO
* @author 		Ken Lee
* @date 		2012-12-7 下午4:22:46
*/
package com.fgc.autocall.app;

import android.content.Context;

public class Config {
	static public final String CONFIG_GROUP = "FGC";
	// define keys
	static public final String CONFIG_KEY_CALL_INTERNAL = "call_internal";
	static public final String CONFIG_KEY_START_CALL_TIME = "start_call_time";
	static public final String CONFIG_KEY_SIM_CARD_TIME_LENGTH = "sim_card_time_length";
	static public final String CONFIG_KEY_WARNNING_PHONE_NUMBER = "warnning_phone_number";
	
	public static void initConfig(Context context){
		if(FileConfig.Instance() == null)
			new FileConfig(context);
	}
	
	public static void setValue(String name, String value) {
		FileConfig.Instance().setValue(CONFIG_GROUP, name, value);
	}

	public static String getValue(String name, String defaultValue) {

		return FileConfig.Instance().getValue(CONFIG_GROUP, name, defaultValue);
	}
	public static void deleteValue(String name)
	{
		FileConfig.Instance().unsetValue(CONFIG_GROUP, name);
	}
	
}
