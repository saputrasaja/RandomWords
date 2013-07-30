package com.wiwit.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.wiwit.connection.Word;
import com.wiwit.connection.WordUtil;

public class WordEngine {
	private List<Word> words;
	protected String wordState;
	private SQLiteDatabase sqLiteDatabase;
	protected final static String NEXT = "NEXT";
	protected final static String DONE = "DONE";

	public WordEngine() {
	}

	public void setWordState(String state) {
		this.wordState = state;
	}

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}

	public SQLiteDatabase getSqLiteDatabase() {
		return sqLiteDatabase;
	}

	public void setSqLiteDatabase(SQLiteDatabase sqLiteDatabase) {
		this.sqLiteDatabase = sqLiteDatabase;
	}

	public Word getRandoWords() {
		return this.words.get((int) (Math.random() * (this.words.size())));
	}

	private void findAndUpdate(Word word, String from) {
		if (wordState.equals(WordUtil.NEW.toString())) {
			if (from.equals(NEXT)) {
				word.setHaveReadNew(true);
			} else if (from.equals(DONE)) {
				word.setHaveReadNew(false);
				word.setState(WordUtil.OLD.toString());
			}
			word.update(sqLiteDatabase, word.getEnglishWord());
		} else if (wordState.equals(WordUtil.OLD.toString())) {
			if (from.equals(NEXT)) {
				word.setHaveReadOld(true);
			} else if (from.equals(DONE)) {
				word.setHaveReadOld(false);
				word.setState(WordUtil.DELETE.toString());
			}
			word.update(sqLiteDatabase, word.getEnglishWord());
		}
	}

	public void nextWord(Word word) {
		findAndUpdate(word, NEXT);
	}

	public void doneWord(Word word) {
		findAndUpdate(word, DONE);
	}

	public static WordEngine getWordsWithState(HashMap<String, Word> allWords,
			String state, SQLiteDatabase sqLiteDatabase) {
		List<Word> result = new ArrayList<Word>();
		for (String key : allWords.keySet()) {
			Word word = allWords.get(key);
			if (word.getState().equals(state)) {
				result.add(word);
			}
		}
		WordEngine engine = new WordEngine();
		engine.setSqLiteDatabase(sqLiteDatabase);
		engine.setWords(result);
		return engine;
	}
}
