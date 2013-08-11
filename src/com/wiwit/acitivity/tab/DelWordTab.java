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

public class DelWordTab extends Activity {
	protected TextView englishWord;
	protected TextView indonesianWord;
	protected Button show;
	protected Button next;
	protected Button edit;
	protected Button moveToOld;
	protected Button start;
	protected ToggleButton toggleDelWord;
	protected boolean readyToStart = false;
	protected WordEngine engine;
	protected Word word;
	protected AlertDialog.Builder restartDialog;
	protected AlertDialog.Builder moveToOldDialog;
	protected TextView moveToTextView;
	protected TextView totalWords;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.del_word_layout);
		// findBViewById
		englishWord = (TextView) findViewById(R.id.english_del);
		indonesianWord = (TextView) findViewById(R.id.indonesian_del);
		show = (Button) findViewById(R.id.show_del);
		next = (Button) findViewById(R.id.next_del);
		edit = (Button) findViewById(R.id.edit_del);
		moveToOld = (Button) findViewById(R.id.move_to_old_del);
		moveToTextView = (TextView) findViewById(R.id.move_to_tv_in_del);
		start = (Button) findViewById(R.id.start_del);
		toggleDelWord = (ToggleButton) findViewById(R.id.toggle_del);
		totalWords= (TextView) findViewById(R.id.total_del);
		// Set Visibility
		englishWord.setVisibility(View.INVISIBLE);
		indonesianWord.setVisibility(View.INVISIBLE);
		show.setVisibility(View.INVISIBLE);
		next.setVisibility(View.INVISIBLE);
		edit.setVisibility(View.INVISIBLE);
		moveToOld.setVisibility(View.INVISIBLE);
		moveToTextView.setVisibility(View.INVISIBLE);
		start.setVisibility(View.INVISIBLE);
		totalWords.setVisibility(View.INVISIBLE);
		// init other component
		restartDialog = new AlertDialog.Builder(this);
		moveToOldDialog = new AlertDialog.Builder(this);
		setListener();
		getAppState().delWordTab = this;
	}

	protected void setListener() {
		DialogInterface.OnClickListener restartdialogListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					next.setVisibility(View.INVISIBLE);
					edit.setVisibility(View.INVISIBLE);
					moveToOld.setVisibility(View.INVISIBLE);
					totalWords.setVisibility(View.INVISIBLE);
					moveToTextView.setVisibility(View.INVISIBLE);
					start.setVisibility(View.INVISIBLE);
					englishWord.setVisibility(View.INVISIBLE);
					indonesianWord.setVisibility(View.INVISIBLE);
					show.setVisibility(View.INVISIBLE);
					readyToStart = false;
					engine.restartWord();
					toast("Lets play again");
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					toggleDelWord.setChecked(true);
					break;
				}
			}
		};
		restartDialog.setMessage("Wanna restart all del words ?");
		restartDialog.setPositiveButton("Yes", restartdialogListener);
		restartDialog.setNegativeButton("No", restartdialogListener);
		moveToOldDialog.setMessage("Move to OLD state for this word ?");
		DialogInterface.OnClickListener moveToOldDialogListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					engine.upState(word);
					preDoOrNext();
					toast("Success move to OLD state");
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};
		moveToOldDialog.setPositiveButton("Yes", moveToOldDialogListener);
		moveToOldDialog.setNegativeButton("No", moveToOldDialogListener);
		toggleDelWord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (toggleDelWord.isChecked()) {
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
					moveToOld.setVisibility(View.VISIBLE);
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
		moveToOld.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				moveToOldDialog.show();
			}
		});
		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switchTab(4);
				getAppState().editTab.editWord(word, EditTab.FROM_DEL);
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
		moveToOld.setVisibility(View.INVISIBLE);
		moveToTextView.setVisibility(View.INVISIBLE);
		show.setVisibility(View.INVISIBLE);
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

	// all important function under this comment
	protected void toast(String message) {
		Toast toast = Toast.makeText(DelWordTab.this, message,
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

	protected void switchTab(int tabId) {
		MainTab ParentActivity = (MainTab) this.getParent();
		ParentActivity.setTransactionID(tabId);
		ParentActivity.switchTabSpecial(tabId);
	}

	public void setElement(Word word) {
		indonesianWord.setText(word.getIndonesianWord());
		this.word = word;
		toast("Success update data");
	}

	protected void generateEngine() {
		getAppState().setAllRow(Word.getAllRow(getSQLite()));
		engine = WordEngine.generateWordEngine(getAppState().getAllRow(),
				WordUtil.DELETE.toString(), getAppState().getSd());
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
								DelWordTab.this.finish();
							}
						}).setNegativeButton("No", null).show();
	}
}
