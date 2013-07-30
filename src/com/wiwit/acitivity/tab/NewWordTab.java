package com.wiwit.acitivity.tab;

import com.wiwit.all.R;
import com.wiwit.connection.Word;
import com.wiwit.connection.WordUtil;
import com.wiwit.util.MyApp;
import com.wiwit.util.WordEngine;

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
	protected Button show;
	protected Button next;
	protected Button edit;
	protected Button done;
	protected Button start;
	protected ToggleButton toggleNewWord;
	protected boolean readyToStart = false;
	protected WordEngine engine;
	protected Word word;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_word_layout);
		englishWord = (TextView) findViewById(R.id.english_new);
		indonesianWord = (TextView) findViewById(R.id.indonesian_new);
		show = (Button) findViewById(R.id.show_new);
		next = (Button) findViewById(R.id.next_new);
		edit = (Button) findViewById(R.id.edit_new);
		done = (Button) findViewById(R.id.done_new);
		start = (Button) findViewById(R.id.start_new);
		changeVisibilityElements(false);
		start.setVisibility(View.INVISIBLE);
		toggleNewWord = (ToggleButton) findViewById(R.id.toggle_new);
		setListener();
	}

	protected void setListener() {
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
					// generateMapNewWord();
					show.setVisibility(View.VISIBLE);
				}
			}
		});
		show.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				next.setVisibility(View.VISIBLE);
				edit.setVisibility(View.VISIBLE);
				done.setVisibility(View.VISIBLE);
			}
		});
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				next.setVisibility(View.INVISIBLE);
				edit.setVisibility(View.INVISIBLE);
				done.setVisibility(View.INVISIBLE);
			}
		});
		done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				next.setVisibility(View.INVISIBLE);
				edit.setVisibility(View.INVISIBLE);
				done.setVisibility(View.INVISIBLE);
			}
		});

	}

	private void generateMapNewWord() {
		MyApp appState = ((MyApp) this.getApplicationContext());
		engine = WordEngine.getWordsWithState(appState.getAllRow(),
				WordUtil.NEW.toString(), appState.getSd());
	}

	private void getRandomWord() {
		if (engine.getWords().size() > 2
				|| engine.getWords().size() > (engine.getWords().size() / 2)) {
			word = engine.getRandoWords();
		} else {
			// TO DO
		}

	}

	private void changeVisibilityElements(boolean visibility) {
		int visible = View.INVISIBLE;
		next.setVisibility(visible);
		edit.setVisibility(visible);
		done.setVisibility(visible);
		if (visibility) {
			visible = View.VISIBLE;
		}
		englishWord.setVisibility(visible);
		indonesianWord.setVisibility(visible);
		show.setVisibility(visible);

	}
}
