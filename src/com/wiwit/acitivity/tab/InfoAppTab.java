package com.wiwit.acitivity.tab;

import com.wiwit.all.R;
import com.wiwit.util.DebugHelper;
import com.wiwit.util.MyApp;

import android.app.Activity;
import android.os.Bundle;

public class InfoAppTab extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_layout);
		
		MyApp appState = ((MyApp)getApplicationContext());
		DebugHelper.debug(appState.getTest());
	}
}
