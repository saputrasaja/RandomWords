package com.wiwit.acitivity.tab;

import com.wiwit.all.R;
import com.wiwit.connection.WordInfoUtil;
import com.wiwit.connection.WordUtil;
import com.wiwit.util.DebugHelper;
import com.wiwit.util.MyApp;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Debug;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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

	public void refreshInfo() {
		try {
			getAppState().viewAllData();
			DebugHelper.debug("refreshInfo");
			DebugHelper.debug("size : " + getAppState().getAllRow().size());
			WordInfoUtil wiu = getAppState().generateWordInfo();
			all.setText(" : " + wiu.getAllWord());
			newWord.setText(" : " + wiu.getNewWord());
			oldWord.setText(" : " + wiu.getOldWord());
			deleteWord.setText(" : " + wiu.getDeleted());
			DebugHelper.debug("after init");
		} catch (Exception e) {
			DebugHelper.debug("can't refreshInfo");
			DebugHelper.exception(this, e);
		}
	}
}
