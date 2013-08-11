package com.wiwit.acitivity.tab;

import com.wiwit.all.R;
import com.wiwit.connection.Word;
import com.wiwit.connection.WordUtil;
import com.wiwit.util.MyApp;
import com.wiwit.util.StaticData;
import com.wiwit.util.WordEngine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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
	protected TextView totalWords;

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
		totalWords= (TextView) findViewById(R.id.total_old);
		// set visibility
		englishWord.setVisibility(View.INVISIBLE);
		indonesianWord.setVisibility(View.INVISIBLE);
		start.setVisibility(View.INVISIBLE);
		show.setVisibility(View.INVISIBLE);
		next.setVisibility(View.INVISIBLE);
		edit.setVisibility(View.INVISIBLE);
		moveToNew.setVisibility(View.INVISIBLE);
		moveToDel.setVisibility(View.INVISIBLE);
		moveToTextView.setVisibility(View.INVISIBLE);
		totalWords.setVisibility(View.INVISIBLE);
		// dialog
		restartDialog = new AlertDialog.Builder(this);
		moveToNewDialog = new AlertDialog.Builder(this);
		moveToDelDialog = new AlertDialog.Builder(this);
		initListener();
		getAppState().oldWordTab = this;
	}

	private void initListener() {
		DialogInterface.OnClickListener restartDialogListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					next.setVisibility(View.INVISIBLE);
					edit.setVisibility(View.INVISIBLE);
					moveToNew.setVisibility(View.INVISIBLE);
					moveToDel.setVisibility(View.INVISIBLE);
					moveToTextView.setVisibility(View.INVISIBLE);
					totalWords.setVisibility(View.INVISIBLE);
					start.setVisibility(View.INVISIBLE);
					englishWord.setVisibility(View.INVISIBLE);
					indonesianWord.setVisibility(View.INVISIBLE);
					show.setVisibility(View.INVISIBLE);
					readyToStart = false;
					engine.restartWord();
					toast("Lets play again");
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					toggleOldWord.setChecked(true);
					break;
				}
			}
		};
		restartDialog.setMessage("Wanna restart all old words ?");
		restartDialog.setPositiveButton("Yes", restartDialogListener);
		restartDialog.setNegativeButton("No", restartDialogListener);
		DialogInterface.OnClickListener moveToNewListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					engine.upState(word);
					preDoOrNext();
					toast("Success move to NEW state");
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};
		moveToNewDialog.setMessage("Move to NEW state for this word ?");
		moveToNewDialog.setPositiveButton("Yes", moveToNewListener);
		moveToNewDialog.setNegativeButton("No", moveToNewListener);
		DialogInterface.OnClickListener moveToDelListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					engine.downState(word);
					preDoOrNext();
					toast("Success move to DELETE state");
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};
		moveToDelDialog.setMessage("Move to DELETE state for this word ?");
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
					totalWords.setVisibility(View.VISIBLE);
				}
			}
		});
		show.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (engine.canRandomWord()) {
					next.setVisibility(View.VISIBLE);
					edit.setVisibility(View.VISIBLE);
					indonesianWord.setVisibility(View.VISIBLE);
					moveToTextView.setVisibility(View.VISIBLE);
					moveToNew.setVisibility(View.VISIBLE);
					moveToDel.setVisibility(View.VISIBLE);
					show.setVisibility(View.INVISIBLE);
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
		moveToNew.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToNewDialog.show();
			}
		});
		moveToDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToDelDialog.show();
			}
		});
		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchTab(4);
				getAppState().editTab.editWord(word, EditTab.FROM_OLD);
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
			totalWords.setText(""+engine.getWords().size());
		} else {
			englishWord.setText("F I N I S H");
			indonesianWord
					.setText("please restart or manage your vocab list :)");
			englishWord.setVisibility(View.VISIBLE);
			indonesianWord.setVisibility(View.VISIBLE);
			totalWords.setText("b_d");
		}
		
	}

	// all importan fungsions undder this comment
	protected void toast(String message) {
		Toast toast = Toast.makeText(OldWordTab.this, message,
				StaticData.TOAST_DURATION);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void updateWord(Word word, String oldKey) {
		if (this.word != null && this.word.equals(oldKey)) {
			this.word.updateElement(word);
		} else if (this.engine != null) {
			for (Word w : engine.getWords()) {
				if (w.equals(oldKey)) {
					w.updateElement(word);
				}
			}
		}
	}

	public void setElement(Word word) {
		indonesianWord.setText(word.getIndonesianWord());
		this.word = word;
		toast("Success update data");
	}

	protected void switchTab(int tabId) {
		MainTab ParentActivity = (MainTab) this.getParent();
		ParentActivity.setTransactionID(tabId);
		ParentActivity.switchTabSpecial(tabId);
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