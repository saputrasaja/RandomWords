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
import android.widget.TextView;

public class InfoAppTab extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_layout);
		initFirst();
	}

	protected MyApp getAppState() {
		return ((MyApp) this.getApplicationContext());
	}

	protected SQLiteDatabase getSQLite() {
		return getAppState().getSd();
	}

	public void initFirst() {
		TextView all = (TextView) findViewById(R.id.total_word);
		TextView newWord = (TextView) findViewById(R.id.new_word);
		TextView oldWord = (TextView) findViewById(R.id.old_word);
		TextView deleteWord = (TextView) findViewById(R.id.delete_word);
		DebugHelper.debug("size : " + getAppState().getAllRow().size());
		WordInfoUtil wiu = getAppState().generateWordInfo();
		all.setText(" : " + wiu.getAllWord());
		newWord.setText(" : " + wiu.getNewWord());
		oldWord.setText(" : " + wiu.getOldWord());
		deleteWord.setText(" : " + wiu.getDeleted());
		DebugHelper.debug("after init");
	}
}
