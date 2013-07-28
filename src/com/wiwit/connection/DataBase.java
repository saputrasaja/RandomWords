package com.wiwit.connection;

import java.util.HashMap;

import com.wiwit.util.ConverterHelper;
import com.wiwit.util.DebugHelper;

import android.database.sqlite.SQLiteDatabase;

public class DataBase {
	public static final String DATABASE_NAME = "RandomWordsApp";

	public static void initAll(SQLiteDatabase sd) {
		sd.execSQL(Word.getCreatedTableStatment());
		 DataBase db = new DataBase();
//		 db.initWordFromLocal(sd);

		HashMap<String, Word> wordMap = Word.getAllRow(sd);
		DebugHelper.debug(wordMap.get("accuse").getIndonesianWord());
		DebugHelper.debug(wordMap.get("accuse").getEnglishWord());
		DebugHelper.debug(wordMap.get("accuse").getState());
		
//		Word w = wordMap.get("accuse");
//		w.setIndonesianWord("tuduh");
//		w.setState(WordUtil.NEW.toString());
//		w.update(sd, "accuse");
	}
	
	public void initWordFromLocal(SQLiteDatabase sd) {
		for (Word w : ConverterHelper.getFromLocalVariable()) {
			w.insert(sd, w);
		}
	}
}
