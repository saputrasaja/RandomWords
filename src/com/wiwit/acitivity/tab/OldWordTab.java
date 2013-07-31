package com.wiwit.acitivity.tab;

import com.wiwit.all.R;
import com.wiwit.connection.Word;
import com.wiwit.util.WordEngine;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class OldWordTab extends Activity {
	protected TextView englishWord;
	protected TextView indonesianWord;
	protected Button show;
	protected Button next;
	protected Button edit;
	protected Button moveToOld;
	protected Button start;
	protected ToggleButton toggleOldWord;
	protected boolean readyToStart = false;
	protected WordEngine engine;
	protected Word word;
	protected AlertDialog.Builder restartDialog;
	protected AlertDialog.Builder moveToNewDialog;
	protected AlertDialog.Builder moveToDelDialog;
	protected TextView moveToTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.old_word_layout);
		englishWord = (TextView) findViewById(R.id.english_old);
		indonesianWord = (TextView) findViewById(R.id.indonesian_old);
		show = (Button) findViewById(R.id.show_old);
		next = (Button) findViewById(R.id.next_old);
		edit = (Button) findViewById(R.id.edit_old);
		moveToOld = (Button) findViewById(R.id.move_to_old);
		moveToTextView = (TextView) findViewById(R.id.move_to_tv_in_old);
		start = (Button) findViewById(R.id.start_old);
		toggleOldWord = (ToggleButton) findViewById(R.id.toggle_old);
		restartDialog = new AlertDialog.Builder(this);
		moveToNewDialog = new AlertDialog.Builder(this);
		moveToDelDialog = new AlertDialog.Builder(this);
	}
}