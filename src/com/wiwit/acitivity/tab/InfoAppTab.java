package com.wiwit.acitivity.tab;

import com.wiwit.all.R;
import com.wiwit.util.DebugHelper;
import com.wiwit.util.MyApp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoAppTab extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_layout);
		initFirst();
	}

	public void initFirst() {
		TextView all = (TextView) findViewById(R.id.info_total_word);
		TextView newWord = (TextView) findViewById(R.id.info_new_word);
		TextView oldWord = (TextView) findViewById(R.id.old_word);
		
	}
}
