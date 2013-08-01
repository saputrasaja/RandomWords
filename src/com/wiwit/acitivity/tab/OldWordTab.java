package com.wiwit.acitivity.tab;

import com.wiwit.all.R;
import com.wiwit.connection.Word;
import com.wiwit.connection.WordUtil;
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

public class OldWordTab extends Activity {
	protected TextView englishWord;
	protected TextView indonesianWord;
	protected Button show;
	protected Button next;
	protected Button edit;
	protected Button moveToNew;
	protected Button moveToDel;
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
		// findById
		setContentView(R.layout.old_word_layout);
		englishWord = (TextView) findViewById(R.id.english_old);
		indonesianWord = (TextView) findViewById(R.id.indonesian_old);
		show = (Button) findViewById(R.id.show_old);
		next = (Button) findViewById(R.id.next_old);
		edit = (Button) findViewById(R.id.edit_old);
		moveToNew = (Button) findViewById(R.id.move_to_new);
		moveToDel = (Button) findViewById(R.id.move_to_del);
		moveToTextView = (TextView) findViewById(R.id.move_to_tv_in_old);
		start = (Button) findViewById(R.id.start_old);
		toggleOldWord = (ToggleButton) findViewById(R.id.toggle_old);
		// dialog
		restartDialog = new AlertDialog.Builder(this);
		moveToNewDialog = new AlertDialog.Builder(this);
		moveToDelDialog = new AlertDialog.Builder(this);
		initListener();
		// set invisible for some element
		englishWord.setVisibility(View.INVISIBLE);
		indonesianWord.setVisibility(View.INVISIBLE);
		start.setVisibility(View.INVISIBLE);
		show.setVisibility(View.INVISIBLE);
		next.setVisibility(View.INVISIBLE);
		edit.setVisibility(View.INVISIBLE);
		moveToNew.setVisibility(View.INVISIBLE);
		moveToDel.setVisibility(View.INVISIBLE);
		moveToTextView.setVisibility(View.INVISIBLE);
	}

	private void initListener() {
		restartDialog.setMessage("Wanna restart all new words ?");
		DialogInterface.OnClickListener restartDialogListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};
		restartDialog.setPositiveButton("Yes", restartDialogListener);
		restartDialog.setNegativeButton("No", restartDialogListener);
		DialogInterface.OnClickListener moveToNewListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};
		moveToNewDialog.setPositiveButton("Yes", moveToNewListener);
		moveToNewDialog.setNegativeButton("No", moveToNewListener);
		DialogInterface.OnClickListener moveToDelListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};
		moveToDelDialog.setPositiveButton("Yes", moveToDelListener);
		moveToDelDialog.setNegativeButton("No", moveToDelListener);
		toggleOldWord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleOldWord.isChecked()) {
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
					preDoOrNext();
				}
			}
		});
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				engine.nextWord(word);
				preDoOrNext();
			}
		});
		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		moveToNew.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToNewDialog.show();
			}
		});
		moveToDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}

	protected void randomWordProcess() {
		word = engine.getRandoWords();
		englishWord.setText(word.getEnglishWord());
		indonesianWord.setText(word.getIndonesianWord());
		indonesianWord.setVisibility(View.INVISIBLE);
		englishWord.setVisibility(View.VISIBLE);
		show.setVisibility(View.VISIBLE);
	}

	protected void preDoOrNext() {
		next.setVisibility(View.INVISIBLE);
		edit.setVisibility(View.INVISIBLE);
		moveToTextView.setVisibility(View.INVISIBLE);
		show.setVisibility(View.INVISIBLE);
		moveToTextView.setVisibility(View.INVISIBLE);
		moveToNew.setVisibility(View.INVISIBLE);
		moveToDel.setVisibility(View.INVISIBLE);
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

	protected void generateEngine() {
		getAppState().setAllRow(Word.getAllRow(getSQLite()));
		engine = WordEngine.generateWordEngine(getAppState().getAllRow(),
				WordUtil.OLD.toString(), getAppState().getSd());
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
								OldWordTab.this.finish();
							}
						}).setNegativeButton("No", null).show();
	}
}