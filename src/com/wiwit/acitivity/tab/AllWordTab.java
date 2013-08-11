package com.wiwit.acitivity.tab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wiwit.all.R;
import com.wiwit.connection.Word;
import com.wiwit.util.MyApp;
import com.wiwit.util.StaticData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AllWordTab extends Activity {
	protected ListView listView;
	protected Button refresh;
	protected EditText charsSelected;
	// private final String chars[] = { "a", "b", "c", "d", "e", "f", "g", "h",
	// "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
	// "v", "w", "x", "y", "z" };
	protected Map<String, Integer> positions;
	protected String lastChar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_word_layout);
		listView = (ListView) findViewById(R.id.listView1);
		refresh = (Button) findViewById(R.id.refresh_all);
		charsSelected = (EditText) findViewById(R.id.chars_all);
		refreshListView();
		initListeners();
		getAppState().allWordTab = this;
	}

	protected void initListeners() {
		OnItemClickListener listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long id) {
				switchTab(4);
				String english = (String) listView.getItemAtPosition(position);
				getAppState().editTab.findWOrd(english);
			}
		};
		listView.setOnItemClickListener(listener);
		refresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshListView();
				toast("Success refresh list");
				listView.setSelection(500);
			}
		});
		TextWatcher textWatcher = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				if (charsSelected.getText().toString().length() == 2) {
					charsSelected.setText(String.valueOf(s.charAt(1)));
					charsSelected.setSelection(1);
				}
				Integer index = positions.get(charsSelected.getText()
						.toString());
				if (index != null) {
					listView.setSelection(index);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}
		};
		charsSelected.addTextChangedListener(textWatcher);
	}

	protected void initPosition(List<String> words) {
		positions = new HashMap<String, Integer>();
		for (int index = 0; index < words.size(); index++) {
			String s = words.get(index);
			String prefix = s.substring(0, 1);
			if (positions.get(prefix) == null) {
				positions.put(prefix, index);
			}
		}
	}

	public void refreshListView() {
		List<String> words = new ArrayList<String>();

		for (String key : getAllWord().keySet()) {
			words.add(key);
		}
		Collections.sort(words);
		initPosition(words);
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.label, words));
	}

	protected HashMap<String, Word> getAllWord() {
		return Word.getAllRow(getAppState().getSd());
	}

	// all important function under this comment
	protected void switchTab(int tabId) {
		MainTab ParentActivity = (MainTab) this.getParent();
		ParentActivity.setTransactionID(tabId);
		ParentActivity.switchTabSpecial(tabId);
	}

	protected void toast(String message) {
		Toast toast = Toast.makeText(AllWordTab.this, message,
				StaticData.TOAST_DURATION);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
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
								AllWordTab.this.finish();
							}
						}).setNegativeButton("No", null).show();
	}
}