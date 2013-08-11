package com.wiwit.acitivity.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wiwit.all.R;
import com.wiwit.connection.Setting;
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

public class NewWordTab extends Activity {
	protected TextView englishWord;
	protected TextView indonesianWord;
	protected Button show;
	protected Button next;
	protected Button edit;
	protected Button moveToOld;
	protected Button start;
	protected ToggleButton toggleNewWord;
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
		setContentView(R.layout.new_word_layout);
		// findBViewById
		englishWord = (TextView) findViewById(R.id.english_new);
		indonesianWord = (TextView) findViewById(R.id.indonesian_new);
		show = (Button) findViewById(R.id.show_new);
		next = (Button) findViewById(R.id.next_new);
		edit = (Button) findViewById(R.id.edit_new);
		moveToOld = (Button) findViewById(R.id.move_to_old);
		moveToTextView = (TextView) findViewById(R.id.move_to_tv_in_new);
		start = (Button) findViewById(R.id.start_new);
		toggleNewWord = (ToggleButton) findViewById(R.id.toggle_new);
		totalWords = (TextView) findViewById(R.id.total_new);
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
		getAppState().newWordTab = this;
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
					moveToTextView.setVisibility(View.INVISIBLE);
					totalWords.setVisibility(View.INVISIBLE);
					start.setVisibility(View.INVISIBLE);
					englishWord.setVisibility(View.INVISIBLE);
					indonesianWord.setVisibility(View.INVISIBLE);
					show.setVisibility(View.INVISIBLE);
					readyToStart = false;
					// start
					HashMap<String, Word> mapWords = Word
							.getAllRow(getSQLite());
					engine = WordEngine.generateWordEngine(mapWords,
							WordUtil.NEW.toString(), getAppState().getSd());
					forceGenerate(engine.getWords(), engine);
					// end
					toast("Lets play again");
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					toggleNewWord.setChecked(true);
					break;
				}
			}
		};
		restartDialog.setMessage("Wanna restart all new words ?");
		restartDialog.setPositiveButton("Yes", restartdialogListener);
		restartDialog.setNegativeButton("No", restartdialogListener);
		moveToOldDialog.setMessage("Move to OLD state for this word ?");
		DialogInterface.OnClickListener moveToOldDialogListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					engine.downState(word);
					removeWordFromSetting(word);
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
					preDoOrNext();
					totalWords.setVisibility(View.VISIBLE);
				}
			}
		});
		show.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				next.setVisibility(View.VISIBLE);
				edit.setVisibility(View.VISIBLE);
				indonesianWord.setVisibility(View.VISIBLE);
				moveToTextView.setVisibility(View.VISIBLE);
				moveToOld.setVisibility(View.VISIBLE);
				show.setVisibility(View.INVISIBLE);
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
				getAppState().editTab.editWord(word, EditTab.FROM_NEW);
			}
		});
	}

	protected void removeWordFromSetting(Word word) {
		getSetting().removeWordFromSetting(word, getAppState().getSd());
	}

	protected Setting getSetting() {
		Setting setting = Setting.findSetting(getAppState().getSd(),
				Setting.KEY_NEW_WORD_LIST);
		if (setting == null) {
			return new Setting();
		}
		return setting;
	}

	protected void randomWordProcess() {
		word = engine.getRandoWords();
		englishWord.setText(word.getEnglishWord());
		indonesianWord.setText(word.getIndonesianWord());
		indonesianWord.setVisibility(View.INVISIBLE);
		englishWord.setVisibility(View.VISIBLE);
		show.setVisibility(View.VISIBLE);
	}

	protected boolean canRandom(int size) {
		if (size >= StaticData.NEW_WORDS_SIZE / 2) {
			return true;
		}
		return false;
	}

	protected void preDoOrNext() {
		Setting setting = getSetting();
		next.setVisibility(View.INVISIBLE);
		edit.setVisibility(View.INVISIBLE);
		moveToOld.setVisibility(View.INVISIBLE);
		moveToTextView.setVisibility(View.INVISIBLE);
		show.setVisibility(View.INVISIBLE);
		if (canRandom(engine.getWords().size())) {
			randomWordProcess();
			totalWords.setText("" + engine.getWords().size());
		} else {
			toast("Horaaay, start from begining again");
			if (canRandom(setting.getNewWordsValue().size())) {
				HashMap<String, Word> mapWords = Word.getAllRow(getSQLite());
				List<Word> words = new ArrayList<Word>();
				for (String word : getSetting().getNewWordsValue()) {
					words.add(mapWords.get(word));
				}
				engine.setWords(words);
				totalWords.setText("" + engine.getWords().size());
				randomWordProcess();
			} else {
				englishWord.setText("F I N I S H");
				indonesianWord
						.setText("please restart or manage your vocab list :)");
				englishWord.setVisibility(View.VISIBLE);
				indonesianWord.setVisibility(View.VISIBLE);
				totalWords.setText("b_d");
			}
		}
	}

	// all important function under this comment
	protected void toast(String message) {
		Toast toast = Toast.makeText(NewWordTab.this, message,
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

	protected void forceGenerate(List<Word> allNewWords, WordEngine engine) {
		List<Word> newWords = new ArrayList<Word>();
		if (allNewWords.size() > StaticData.NEW_WORDS_SIZE) {
			while (newWords.size() < StaticData.NEW_WORDS_SIZE) {
				int location = (int) (Math.random() * (allNewWords.size()));
				newWords.add(allNewWords.get(location));
				allNewWords.remove(location);
			}
		} else {
			for (Word word : allNewWords) {
				newWords.add(word);
			}
		}
		engine.setWords(newWords);
		engine.setWordState(WordUtil.NEW.toString());
		// setting for newWords
		Setting setting = getSetting();
		setting.generateNewWordsValue(newWords, getAppState().getSd());
		setting.insert(getAppState().getSd());
	}

	protected void generateEngine() {
		Setting setting = getSetting();
		HashMap<String, Word> mapWords = Word.getAllRow(getSQLite());
		engine = WordEngine.generateWordEngine(Word.getAllRow(getSQLite()),
				WordUtil.NEW.toString(), getAppState().getSd());
		boolean canIn = true;
		if (setting != null && setting.isHasNewWord() && canIn) {
			List<Word> newWords = new ArrayList<Word>();
			for (String key : setting.getNewWordsValue()) {
				if (mapWords.get(key) != null
						&& !mapWords.get(key).isHasReadDel()) {
					newWords.add(mapWords.get(key));
				}
			}
			engine.setWordState(WordUtil.NEW.toString());
			engine.setWords(newWords);
			getAppState().setAllRow(mapWords);
		} else {
			forceGenerate(engine.getWords(), engine);
		}
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
