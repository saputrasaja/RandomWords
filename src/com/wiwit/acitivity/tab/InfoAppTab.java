package com.wiwit.acitivity.tab;

import java.util.HashMap;

import com.wiwit.all.R;
import com.wiwit.connection.Word;
import com.wiwit.connection.WordInfoUtil;
import com.wiwit.connection.WordUtil;
import com.wiwit.util.DebugHelper;
import com.wiwit.util.MyApp;
import com.wiwit.util.StaticData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Debug;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class InfoAppTab extends Activity {
	protected TextView all;
	protected TextView newWord;
	protected TextView oldWord;
	protected TextView deleteWord;
	protected Button btnRefresh;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_layout);
		all = (TextView) findViewById(R.id.total_word);
		newWord = (TextView) findViewById(R.id.new_word);
		oldWord = (TextView) findViewById(R.id.old_word);
		deleteWord = (TextView) findViewById(R.id.delete_word);
		btnRefresh = (Button) findViewById(R.id.info_btn_refresh);
		btnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshInfo();
			}
		});
		refreshInfo();
	}

	protected MyApp getAppState() {
		return ((MyApp) this.getApplicationContext());
	}

	protected SQLiteDatabase getSQLite() {
		return getAppState().getSd();
	}

	protected WordInfoUtil getInfo() {
		WordInfoUtil wordInfoUtil = new WordInfoUtil();
		HashMap<String, Word> map = Word.getAllRow(getSQLite());
		for (String engLishWord : map.keySet()) {
			String state = map.get(engLishWord).getState();
			if (state.equals(WordUtil.NEW.toString())) {
				wordInfoUtil.incrementNew();
			} else if (state.equals(WordUtil.OLD.toString())) {
				wordInfoUtil.incrementOld();
			} else if (state.equals(WordUtil.DELETE.toString())) {
				wordInfoUtil.incrementDelete();
			}
		}
		return wordInfoUtil;
	}

	public void refreshInfo() {
		try {
			WordInfoUtil wiu = getInfo();
			all.setText(" : " + wiu.getAllWord());
			newWord.setText(" : " + wiu.getNewWord());
			oldWord.setText(" : " + wiu.getOldWord());
			deleteWord.setText(" : " + wiu.getDeleted());
			toast("Success refresh info");
		} catch (Exception e) {
			DebugHelper.debug("can't refreshInfo");
			DebugHelper.exception(this, e);
			toast(e.getMessage());
		}
	}

	protected void toast(String message) {
		Toast toast = Toast.makeText(InfoAppTab.this, message,
				StaticData.TOAST_DURATION);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								InfoAppTab.this.finish();
							}
						}).setNegativeButton("No", null).show();
	}
}
