package com.wiwit.connection;

import java.util.HashMap;

import com.wiwit.util.ConverterHelper;
import com.wiwit.util.DebugHelper;
import com.wiwit.util.MyApp;

import android.database.sqlite.SQLiteDatabase;

public class DataBase {
	public static final String DATABASE_NAME = "RandomWordsApp3";

	public static void initWordFromLocal(SQLiteDatabase sd) {
		sd.execSQL(Word.getCreatedTableStatment());
		for (Word w : ConverterHelper.getFromLocalVariable()) {
			w.insert(sd);
		}
	}
}
