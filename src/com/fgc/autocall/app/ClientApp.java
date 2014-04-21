package com.fgc.autocall.app;


import android.app.Application;
import android.util.Log;

public class ClientApp  extends Application {
	private static final String LOG_TAG = "ClientApp";
	
	@Override
	public void onCreate(){
		super.onCreate();
		Log.i(LOG_TAG, "onCreate");

	}
	
    @Override
    public void onTerminate() {
    	Log.i(LOG_TAG, "onTerminate");
    	
    	finishApp();
        super.onTerminate();
    }
    
	public void initApp(){
		Log.i(LOG_TAG, "initApp");
	}

	public void finishApp(){
		Log.i(LOG_TAG, "finishApp");

	}
}
