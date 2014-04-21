package com.fgc.autocall.ui;

import com.fgc.autocall.R;

import android.os.Bundle;

public class ActivityMain extends BaseActivity {

	private static final String LOG_TAG = "ActivityMainV2";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_main);
		
		
	}


	@Override
	protected boolean ifFinishAppByBackKeyPress(){
		return true;
	}
	
}
