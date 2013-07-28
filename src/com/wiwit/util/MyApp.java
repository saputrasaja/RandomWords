package com.wiwit.util;

import java.util.HashMap;

import com.wiwit.connection.Word;
import com.wiwit.connection.WordInfoUtil;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class MyApp extends Application {

	private SQLiteDatabase sd;
	private HashMap<String, Word> allRow;

	public MyApp() {
	}

	public SQLiteDatabase getSd() {
		return sd;
	}

	public void setSd(SQLiteDatabase sd) {
		this.sd = sd;
	}

	public HashMap<String, Word> getAllRow() {
		return allRow;
	}

	public void setAllRow(HashMap<String, Word> allRow) {
		this.allRow =  new HashMap<String, Word>();
		this.allRow = allRow;
	}

	public WordInfoUtil generateWordInfo() {
		WordInfoUtil wordInfoUtil = new WordInfoUtil();
		for (String engLishWord : getAllRow().keySet()) {
			
		}
		return wordInfoUtil;
	}
}
