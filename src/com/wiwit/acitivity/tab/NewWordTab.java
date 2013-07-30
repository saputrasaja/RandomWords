package com.wiwit.acitivity.tab;

import com.wiwit.all.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class NewWordTab extends Activity {
	TextView englishWord;
	TextView indonesianWord;
	Button pass;
	Button next;
	Button edit;
	Button done;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_word_layout);
	}
}