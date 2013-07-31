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
	private boolean haveReadNew;
	private boolean haveReadOld;

	public static String COLUMN_1 = "english_word";
	public static String COLUMN_2 = "indonesian_word";
	public static String COLUMN_3 = "state";
	public static String COLUMN_4 = "have_read_new";
	public static String COLUMN_5 = "have_read_old";

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

	public boolean isHaveReadNew() {
		return haveReadNew;
	}

	public void setHaveReadNew(boolean haveReadNew) {
		this.haveReadNew = haveReadNew;
	}

	public boolean isHaveReadOld() {
		return haveReadOld;
	}

	public void setHaveReadOld(boolean haveReadOld) {
		this.haveReadOld = haveReadOld;
	}

	public static String getCreatedTableStatment() {
		String sql = "CREATE TABLE IF NOT EXISTS " + "`words`(`" + COLUMN_1
				+ "` varchar(32) NOT NULL,  `" + COLUMN_2
				+ "` varchar(128) NOT NULL,  `" + COLUMN_3
				+ "` varchar(16) NOT NULL, `" + COLUMN_4
				+ "` tinyint(1) NOT NULL, `" + COLUMN_5
				+ "` tinyint(1) NOT NULL, PRIMARY KEY (`" + COLUMN_1 + "`))";
		DebugHelper.debug(sql);
		return sql;
	}

	public void insert(SQLiteDatabase sd) {
		try {
			String insertSql2 = "INSERT INTO '" + TABLE_NAME + "'";
			insertSql2 = insertSql2 + " (`" + COLUMN_1 + "`, `" + COLUMN_2
					+ "`, `" + COLUMN_3 + "`, `" + COLUMN_4 + "`, `" + COLUMN_5
					+ "`) VALUES ";
			insertSql2 = insertSql2 + "('" + getEnglishWord() + "', '"
					+ getIndonesianWord() + "', '" + getState() + "','"
					+ (isHaveReadNew() ? 1 : 0) + "','"
					+ (isHaveReadOld() ? 1 : 0) + "');";
			DebugHelper.debug(insertSql2);
			sd.execSQL(insertSql2);
		} catch (Exception e) {
			DebugHelper.exception("can't input "+getEnglishWord(), e);
			try {
				update(sd, getEnglishWord());
			} catch (Exception ee) {
				DebugHelper.exception("can't update "+getEnglishWord(), ee);
			}
		}
	}

	public void update(SQLiteDatabase sd, String key) {
		String sql2 = "UPDATE `" + TABLE_NAME + "` SET ";
		sql2 = sql2 + "`" + COLUMN_1 + "` = '" + getEnglishWord() + "', ";
		sql2 = sql2 + "`" + COLUMN_2 + "` = '" + getIndonesianWord() + "', ";
		sql2 = sql2 + "`" + COLUMN_3 + "` = '" + getState() + "', ";
		sql2 = sql2 + "`" + COLUMN_4 + "` = '" + (isHaveReadNew() ? 1 : 0)
				+ "', ";
		sql2 = sql2 + "`" + COLUMN_5 + "` = '" + (isHaveReadOld() ? 1 : 0)
				+ "'";
		sql2 = sql2 + " WHERE `" + TABLE_NAME + "`.`english_word` = '" + key
				+ "'";
		try {
			DebugHelper.debug(sql2);
			sd.execSQL(sql2);

		} catch (Exception e) {
			e.printStackTrace();
			DebugHelper.exception(e);
		}

	}

	public static void fillIt(Word w, String variable, String value) {
		if (variable.equals(COLUMN_1)) {
			w.setEnglishWord(value);
		} else if (variable.equals(COLUMN_2)) {
			w.setIndonesianWord(value);
		} else if (variable.equals(COLUMN_3)) {
			w.setState(value);
		} else if (variable.equals(COLUMN_4)) {
			w.setHaveReadNew(value.equals("0") ? false : true);
		} else if (variable.equals(COLUMN_5)) {
			w.setHaveReadOld(value.equals("0") ? false : true);
		}
	}
	
	public static void updateReadableAllRow(String state){
		
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
