package com.wiwit.connection;

import java.util.HashMap;

import com.wiwit.util.DebugHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Word {
	private static final String TABLE_NAME = "words";
	private String englishWord;
	private String indonesianWord;
	private String state;

	public static String COLUMN_1 = "english_word";
	public static String COLUMN_2 = "indonesian_word";
	public static String COLUMN_3 = "state";

	public Word() {
	}

	public String getEnglishWord() {
		return englishWord;
	}

	public void setEnglishWord(String englishWord) {
		this.englishWord = englishWord;
	}

	public String getIndonesianWord() {
		return indonesianWord;
	}

	public void setIndonesianWord(String indonesianWord) {
		this.indonesianWord = indonesianWord;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public static void deleteTable(SQLiteDatabase sd) {
		sd.execSQL("DROP TABLE " + TABLE_NAME);
	}

	public static String getCreatedTableStatment() {
		return "CREATE TABLE IF NOT EXISTS `words`(`" + COLUMN_1
				+ "` varchar(32) NOT NULL,  `" + COLUMN_2
				+ "` varchar(128) NOT NULL,  `" + COLUMN_3
				+ "` varchar(16) NOT NULL,  PRIMARY KEY (`english_word`))";
	}

	public void insert(SQLiteDatabase sd, Word w) {
		try {
			String countSql = "SELECT count(*) FROM `words` where english_word ='"
					+ w.getEnglishWord() + "'";
			String insertSql = "INSERT INTO " + TABLE_NAME
					+ " (`english_word` ,`indonesian_word` ,`state`)VALUES ('"
					+ w.getEnglishWord() + "', '" + w.getIndonesianWord()
					+ "', '" + w.getState() + "');";
			DebugHelper.debug(insertSql);
			sd.execSQL(insertSql);
		} catch (Exception e) {
			DebugHelper.exception(this, e);
		}
	}

	public void update(SQLiteDatabase sd, String key) {
		String sql = "UPDATE " + TABLE_NAME + " SET " + COLUMN_1 + " = '"
				+ getEnglishWord() + "', " + COLUMN_2 + " = '" + indonesianWord
				+ "', " + COLUMN_3 + " ='" + getState() + "' WHERE " + COLUMN_1
				+ " ='" + key + "';";
		sd.execSQL(sql);
	}

	public static void fillIt(Word w, String variable, String value) {
		if (variable.equals(COLUMN_1)) {
			w.setEnglishWord(value);
		} else if (variable.equals(COLUMN_2)) {
			w.setIndonesianWord(value);
		} else if (variable.equals(COLUMN_3)) {
			w.setState(value);
		}
	}

	public static HashMap<String, Word> getAllRow(SQLiteDatabase sd) {
		try {
			Cursor cursor = sd.rawQuery("SELECT * FROM `" + TABLE_NAME + "`",
					null);
			HashMap<String, Word> result = new HashMap<String, Word>();
			if (cursor.moveToFirst()) {
				do {
					Word w = new Word();
					for (int i = 0; i < cursor.getColumnCount(); i++) {
						fillIt(w, cursor.getColumnName(i), cursor.getString(i));
					}
					result.put(w.getEnglishWord(), w);
				} while (cursor.moveToNext());
			}
			return result;
		} catch (Exception e) {
			DebugHelper.exception(e);
		}
		return null;
	}

	public static void updateWord(HashMap<String, Word> wordMap, Word word,
			String key, SQLiteDatabase sd) {
		try {
			word.update(sd, key);
			wordMap.remove(key);
			wordMap.put(key, word);
		} catch (Exception e) {
			DebugHelper.exception(e);
		}
	}
}
