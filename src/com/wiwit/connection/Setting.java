package com.wiwit.connection;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wiwit.util.DebugHelper;
import com.wiwit.util.StaticData;

public class Setting {
	private static final String TABLE_NAME = "setting";
	public static String KEY_NEW_WORD_LIST = "NEW_WORD_LIST";
	public static String KEY_NEW_WORD_LIST_FINAL = "NEW_WORD_LIST_FINAL";
	private String key;
	private String value;
	protected final static String NEW_WORDS_SPLITER = "@_@";

	public static String COLUMN_1 = "key";
	public static String COLUMN_2 = "value";

	public Setting() {
		value = "";
		key = "";
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isHasNewWord() {
		if (value != null
				&& value.split(NEW_WORDS_SPLITER).length < StaticData.NEW_WORDS_SIZE / 2) {
			return true;
		}
		return false;
	}

	public List<String> getNewWordsValue() {
		List<String> words = new ArrayList<String>();
		String[] arrStr = value.split(NEW_WORDS_SPLITER);
		for (int i = 0; i < arrStr.length; i++) {
			words.add(arrStr[i]);
		}
		return words;
	}

	public void removeWordFromSetting(Word word, SQLiteDatabase sqLiteDatabase) {
		String temp = "";
		for (String str : value.split(NEW_WORDS_SPLITER)) {
			if (!word.getEnglishWord().equals(str)) {
				temp = temp + str + NEW_WORDS_SPLITER;
			}
		}
		this.value = temp;
		this.key = KEY_NEW_WORD_LIST;
		update(sqLiteDatabase, KEY_NEW_WORD_LIST);
	}

	public void generateNewWordsValue(List<Word> words,
			SQLiteDatabase sqLiteDatabase) {
		this.value = "";
		for (Word w : words) {
			this.value = this.value + w.getEnglishWord() + NEW_WORDS_SPLITER;
		}
		this.key = KEY_NEW_WORD_LIST;
		update(sqLiteDatabase, KEY_NEW_WORD_LIST);
	}

	public static String getCreatedTableStatment() {
		String sql = "CREATE TABLE IF NOT EXISTS " + "`setting`(`" + COLUMN_1
				+ "` varchar(32) NOT NULL,  `" + COLUMN_2 + "` text NOT NULL "
				+ ",PRIMARY KEY (`" + COLUMN_1 + "`))";
		return sql;
	}

	public void insert(SQLiteDatabase sd) {
		try {
			String insertSql2 = "INSERT INTO '" + TABLE_NAME + "'";
			insertSql2 = insertSql2 + " (`" + COLUMN_1 + "`, `" + COLUMN_2
					+ "`) VALUES ";
			insertSql2 = insertSql2 + "('" + getKey() + "', '" + getValue()
					+ "');";
			// DebugHelper.debug(insertSql2);
			sd.execSQL(insertSql2);
		} catch (Exception e) {
			DebugHelper.exception("can't input " + getKey(), e);
			try {
				update(sd, getKey());
			} catch (Exception ee) {
				DebugHelper.exception("can't update " + getKey(), ee);
			}
		}
	}

	public void update(SQLiteDatabase sd, String key) {
		String sql2 = "UPDATE `" + TABLE_NAME + "` SET ";
		sql2 = sql2 + "`" + COLUMN_1 + "` = '" + getKey() + "', ";
		sql2 = sql2 + "`" + COLUMN_2 + "` = '" + getValue() + "' ";
		sql2 = sql2 + " WHERE `" + TABLE_NAME + "`.`" + COLUMN_1 + "` = '"
				+ key + "'";
		try {
			// DebugHelper.debug(sql2);
			sd.execSQL(sql2);
			DebugHelper.debug("SUCCESS UPDATE SETTING NEW WORDS");
		} catch (Exception e) {
			e.printStackTrace();
			DebugHelper.exception(e);
		}
	}

	public static void fillIt(Setting setting, String variable, String value) {
		if (variable.equals(COLUMN_1)) {
			setting.setKey(value);
		} else if (variable.equals(COLUMN_2)) {
			setting.setValue(value);
		}
	}

	public static Setting findSetting(SQLiteDatabase sd, String key) {
		try {
			Cursor cursor = sd.rawQuery("SELECT * FROM `" + TABLE_NAME + "`",
					null);
			if (cursor.moveToFirst()) {
				do {
					Setting setting = new Setting();
					for (int i = 0; i < cursor.getColumnCount(); i++) {
						fillIt(setting, cursor.getColumnName(i),
								cursor.getString(i));
					}
					if (setting.getKey().equalsIgnoreCase(key)) {
						return setting;
					}
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			DebugHelper.exception(e);
		}
		return null;
	}
}
