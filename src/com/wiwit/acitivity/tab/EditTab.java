package com.wiwit.acitivity.tab;

import com.wiwit.all.R;
import com.wiwit.connection.Word;
import com.wiwit.connection.WordUtil;
import com.wiwit.util.DebugHelper;
import com.wiwit.util.MyApp;
import com.wiwit.util.StaticData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.Toast;

public class EditTab extends Activity {
	protected EditText findWord;
	protected EditText english;
	protected EditText indonesian;
	protected RadioButton newState;
	protected RadioButton oldState;
	protected RadioButton delState;
	protected RadioGroup stateGroup;
	protected CheckBox haveReadNew;
	protected CheckBox haveReadOld;
	protected Button save;
	protected Button search;
	protected Button clear;

	// tabblerow
	protected TableRow rowFindEdit;
	protected TableRow rowSearchEdit;
	protected TableRow rowStateTV;
	protected TableRow rowStateRadio;
	protected TableRow rowReadableTV;
	protected TableRow rowReadableCheck;
	// dialog
	protected AlertDialog.Builder saveDialog;
	//
	public static String FROM_NEW = "FROM_NEW";
	public static String FROM_OLD = "FROM_OLD";
	protected String from;
	protected String lastState;
	protected String oldKey;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_word_layout);
		// init id
		findWord = (EditText) findViewById(R.id.find_word_edit);
		english = (EditText) findViewById(R.id.english_edit);
		indonesian = (EditText) findViewById(R.id.indonesian_edit);
		newState = (RadioButton) findViewById(R.id.state_new_edit);
		oldState = (RadioButton) findViewById(R.id.state_old_edit);
		delState = (RadioButton) findViewById(R.id.state_del_edit);
		stateGroup = (RadioGroup) findViewById(R.id.state_edit);
		haveReadOld = (CheckBox) findViewById(R.id.have_read_old_edit);
		haveReadNew = (CheckBox) findViewById(R.id.have_read_new_edit);
		save = (Button) findViewById(R.id.save_edit);
		search = (Button) findViewById(R.id.search_edit);
		clear = (Button) findViewById(R.id.clear_edit);
		// row
		rowFindEdit = (TableRow) findViewById(R.id.row_find_edit);
		rowSearchEdit = (TableRow) findViewById(R.id.row_search_edit);
		rowStateTV = (TableRow) findViewById(R.id.row_state_tv_edit);
		rowStateRadio = (TableRow) findViewById(R.id.row_state_radio_edit);
		rowReadableTV = (TableRow) findViewById(R.id.row_readable_tv_edit);
		rowReadableCheck = (TableRow) findViewById(R.id.row_readable_check_edit);
		// dialog
		saveDialog = new AlertDialog.Builder(this);
		initListener();
		getAppState().editTab = this;
	}

	public void editWord(Word word, String from) {
		this.from = from;
		setElements(word);
		english.setEnabled(false);
		rowFindEdit.setVisibility(View.INVISIBLE);
		rowSearchEdit.setVisibility(View.INVISIBLE);
		rowStateTV.setVisibility(View.INVISIBLE);
		rowStateRadio.setVisibility(View.INVISIBLE);
		rowReadableTV.setVisibility(View.INVISIBLE);
		rowReadableCheck.setVisibility(View.INVISIBLE);
	}

	protected void setElements(Word word) {
		english.setText(word.getEnglishWord());
		indonesian.setText(word.getIndonesianWord());
		haveReadNew.setChecked(word.isHasReadNew());
		haveReadOld.setChecked(word.isHasReadOld());
		if (WordUtil.NEW.toString().equals(word.getState())) {
			newState.setChecked(true);
		} else if (WordUtil.OLD.toString().equals(word.getState())) {
			oldState.setChecked(true);
		} else if (WordUtil.DELETE.toString().equals(word.getState())) {
			delState.setChecked(true);
		}
	}

	protected Word getWordFromElements() {
		int selectedId = stateGroup.getCheckedRadioButtonId();
		RadioButton radioButton = (RadioButton) findViewById(selectedId);
		String state = radioButton.getText().toString().toUpperCase();
		Word w = new Word();
		w.setEnglishWord(english.getText().toString());
		w.setIndonesianWord(indonesian.getText().toString());
		w.setHasReadNew(haveReadNew.isChecked());
		w.setHasReadOld(haveReadOld.isChecked());
		w.setState(state);
		return w;
	}

	protected void initDefaultElement() {
		rowFindEdit.setVisibility(View.VISIBLE);
		rowSearchEdit.setVisibility(View.VISIBLE);
		rowStateTV.setVisibility(View.VISIBLE);
		rowStateRadio.setVisibility(View.VISIBLE);
		rowReadableTV.setVisibility(View.VISIBLE);
		rowReadableCheck.setVisibility(View.VISIBLE);
		english.setText("");
		indonesian.setText("");
		haveReadNew.setChecked(false);
		haveReadOld.setChecked(false);
		oldState.setChecked(false);
		delState.setChecked(false);
		newState.setChecked(true);
	}

	protected void initListener() {
		DialogInterface.OnClickListener saveDialogListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Word word = getWordFromElements();
					word.update(getAppState().getSd(), word.getEnglishWord());
					english.setEnabled(true);
					if (from != null) {
						if (from.equals(FROM_NEW)) {
							switchTab(1);
							getAppState().newWordTab.setElement(word);
						} else if (from.equals(FROM_OLD)) {
							switchTab(2);
							getAppState().oldWordTab.setElement(word);
						}
					} else {
						// from == null, must have lastState and oldKey
						if (lastState.equals(WordUtil.NEW.toString())) {
							getAppState().newWordTab.updateWord(word, oldKey);
						} else if (lastState.equals(WordUtil.OLD.toString())) {
							getAppState().oldWordTab.updateWord(word, oldKey);
						}
						toast("Success update data");
						lastState = null;
						oldKey = null;
					}
					initDefaultElement();
					break;
				case DialogInterface.BUTTON_NEGATIVE:
					break;
				}
			}
		};
		saveDialog.setMessage("Are you sure want to save ?");
		saveDialog.setPositiveButton("Yes", saveDialogListener);
		saveDialog.setNegativeButton("No", saveDialogListener);
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				saveDialog.show();
			}
		});
		clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initDefaultElement();
				if (from != null) {
					if (from.equals(FROM_NEW)) {
						switchTab(1);
					} else if (from.equals(FROM_OLD)) {
						switchTab(2);
					}
				}
			}
		});
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Word w = Word.findWord(getAppState().getSd(), findWord
						.getText().toString());
				if (w == null) {
					String message = "Can't find "
							+ findWord.getText().toString();
					toast(message);
				} else {
					setElements(w);
					lastState = w.getState();
					oldKey = w.getEnglishWord();
				}
			}
		});
	}

	protected void toast(String message) {
		Toast toast = Toast.makeText(EditTab.this, message,
				StaticData.TOAST_DURATION);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	protected void switchTab(int tabId) {
		MainTab ParentActivity = (MainTab) this.getParent();
		ParentActivity.setTransactionID(tabId);
		ParentActivity.switchTabSpecial(tabId);
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
								EditTab.this.finish();
							}
						}).setNegativeButton("No", null).show();
	}
}
