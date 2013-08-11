package com.wiwit.util;

import java.util.HashMap;

import com.wiwit.acitivity.tab.AllWordTab;
import com.wiwit.acitivity.tab.DelWordTab;
import com.wiwit.acitivity.tab.EditTab;
import com.wiwit.acitivity.tab.NewWordTab;
import com.wiwit.acitivity.tab.OldWordTab;
import com.wiwit.connection.Word;
import com.wiwit.connection.WordInfoUtil;
import com.wiwit.connection.WordUtil;

import android.app.Activity;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class MyApp extends Application {

	private SQLiteDatabase sd;
	private HashMap<String, Word> allRow;
	public EditTab editTab;
	public NewWordTab newWordTab;
	public OldWordTab oldWordTab;
	public AllWordTab allWordTab;
	public DelWordTab delWordTab;

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
		this.allRow = new HashMap<String, Word>();
		this.allRow = allRow;
	}

	public boolean isNewTabIsNull() {
		return (newWordTab == null);
	}

	public boolean isOldTabIsNull() {
		return (oldWordTab == null);
	}
}
