package com.wiwit.acitivity.tab;

import com.wiwit.all.R;
import com.wiwit.util.MyApp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class NewWordTab extends Activity {
	protected TextView englishWord;
	protected TextView indonesianWord;
	protected Button viewIndonesian;
	protected Button next;
	protected Button edit;
	protected Button done;
	protected Button start;
	protected ToggleButton toggleNewWord;
	protected boolean readyToStart = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_word_layout);
		englishWord = (TextView) findViewById(R.id.english_new);
		indonesianWord = (TextView) findViewById(R.id.indonesian_new);
		viewIndonesian = (Button) findViewById(R.id.pass_new);
		next = (Button) findViewById(R.id.next_new);
		edit = (Button) findViewById(R.id.edit_new);
		done = (Button) findViewById(R.id.done_new);
		start = (Button) findViewById(R.id.start_new);
		changeVisibilityElements(false);
		start.setVisibility(View.INVISIBLE);
		toggleNewWord = (ToggleButton) findViewById(R.id.toggle_new);
		toggleNewWord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleNewWord.isChecked()) {
					start.setVisibility(View.VISIBLE);
					readyToStart = true;
				} else {
					changeVisibilityElements(false);
					start.setVisibility(View.INVISIBLE);
					readyToStart = false;
				}
			}
		});
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (readyToStart) {
					changeVisibilityElements(true);
					start.setVisibility(View.INVISIBLE);
					generateMapNewWord();
				}
			}
		});
	}

	public void generateMapNewWord() {
		MyApp appState = ((MyApp) this.getApplicationContext());
		
	}

	public void changeVisibilityElements(boolean visibility) {
		int visible = 4;
		if (visibility) {
			visible = 1;
		}
		englishWord.setVisibility(visible);
		indonesianWord.setVisibility(visible);
		viewIndonesian.setVisibility(visible);
		next.setVisibility(visible);
		edit.setVisibility(visible);
		done.setVisibility(visible);
	}
}
