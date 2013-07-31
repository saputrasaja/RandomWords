package com.wiwit.acitivity.tab;

import com.wiwit.all.R;
import com.wiwit.connection.Word;
import com.wiwit.connection.WordUtil;
import com.wiwit.util.DebugHelper;
import com.wiwit.util.MyApp;
import com.wiwit.util.WordEngine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
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
	protected AlertDialog.Builder restartDialog;

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
		toggleNewWord = (ToggleButton) findViewById(R.id.toggle_new);
		restartDialog = new AlertDialog.Builder(this);
		setListener();
		start.setVisibility(View.INVISIBLE);
	}

	protected void setListener() {
		restartDialog.setMessage("Wanna restart all new words ?");
		DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					changeVisibilityElements(false);
					start.setVisibility(View.INVISIBLE);
					readyToStart = false;
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					toggleNewWord.setChecked(true);
					break;
				}
			}
		};
		restartDialog.setPositiveButton("Yes", dialogListener);
		restartDialog.setNegativeButton("No", dialogListener);
		toggleNewWord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleNewWord.isChecked()) {
					start.setVisibility(View.VISIBLE);
					readyToStart = true;
				} else {
					restartDialog.show();
				}
			}
		});
		start.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (readyToStart) {
					generateEngine();
					start.setVisibility(View.INVISIBLE);
					doNexOrDone();
				}
			}
		});
		show.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (engine.canRandomWord()) {
					next.setVisibility(View.VISIBLE);
					edit.setVisibility(View.VISIBLE);
					done.setVisibility(View.VISIBLE);
					indonesianWord.setVisibility(View.VISIBLE);
					show.setVisibility(View.INVISIBLE);
				}
			}
		});
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				engine.nextWord(word);
				doNexOrDone();
			}
		});
		done.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				engine.doneWord(word);
				doNexOrDone();
			}
		});
	}

	protected void doNexOrDone() {
		next.setVisibility(View.INVISIBLE);
		edit.setVisibility(View.INVISIBLE);
		done.setVisibility(View.INVISIBLE);
		show.setVisibility(View.INVISIBLE);
		DebugHelper.debug("doNexOrDone : " + engine.canRandomWord());
		if (engine.canRandomWord()) {
			randomWordProcess();
		} else {
			englishWord.setText("F I N I S H");
			indonesianWord
					.setText("please restart or manage your vocab list :)");
			englishWord.setVisibility(View.VISIBLE);
			indonesianWord.setVisibility(View.VISIBLE);
		}
	}

	protected void randomWordProcess() {
		word = engine.getRandoWords();
		englishWord.setText(word.getEnglishWord());
		indonesianWord.setText(word.getIndonesianWord());
		indonesianWord.setVisibility(View.INVISIBLE);
		englishWord.setVisibility(View.VISIBLE);
		show.setVisibility(View.VISIBLE);
	}

	protected void generateEngine() {
		getAppState().setAllRow(Word.getAllRow(getSQLite()));
		engine = WordEngine.getWordsWithState(getAppState().getAllRow(),
				WordUtil.NEW.toString(), getAppState().getSd());
	}

	protected void changeVisibilityElements(boolean visibility) {
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

	protected MyApp getAppState() {
		return ((MyApp) this.getApplicationContext());
	}

	protected SQLiteDatabase getSQLite() {
		return getAppState().getSd();
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								NewWordTab.this.finish();
							}
						}).setNegativeButton("No", null).show();
	}
}
