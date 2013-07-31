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

	public boolean canRandomWord() {
//		DebugHelper.debug("canRandomWord");
//		DebugHelper.debug(getWords().size() > 2);
//		DebugHelper.debug(getWords().size() > (getWords().size() / 2));
		if (getWords().size() > 2
				|| getWords().size() > (getWords().size() / 2)) {
			return true;
		}
		return false;
	}

	public Word getRandoWords() {
		if (!canRandomWord()) {
			return null;
		}
		return this.words.get((int) (Math.random() * (this.words.size())));
	}

	private void findAndUpdate(Word word, String from) {
//		DebugHelper.debug("findAndUpdate");
		if (wordState.equals(WordUtil.NEW.toString())) {
			DebugHelper.debug("NEW");
			if (from.equals(NEXT)) {
				DebugHelper.debug("NEXT");
				word.setHaveReadNew(true);
			} else if (from.equals(DONE)) {
				DebugHelper.debug("DONE");
				word.setHaveReadNew(false);
				word.setState(WordUtil.OLD.toString());
			}
//			DebugHelper.debug("ready to update");
			word.update(sqLiteDatabase, word.getEnglishWord());
			remove(word);
		} else if (wordState.equals(WordUtil.OLD.toString())) {
			DebugHelper.debug("OLD");
			if (from.equals(NEXT)) {
				DebugHelper.debug("NEXT");
				word.setHaveReadOld(true);
			} else if (from.equals(DONE)) {
				DebugHelper.debug("DONE");
				word.setHaveReadOld(false);
				word.setState(WordUtil.DELETE.toString());
			}
//			DebugHelper.debug("ready to update");
			word.update(sqLiteDatabase, word.getEnglishWord());
			remove(word);
		}
	}

	protected void remove(Word word) {
		int temp = 0;
		for (int i = 0; i < words.size(); i++) {
			if (words.get(i).getEnglishWord().equals(word.getEnglishWord())) {
				temp = i;
				break;
			}
		}
		words.remove(temp);
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
			boolean alredyRead = true;
			if (word.getState().equals(state)) {
				if (state.equals(WordUtil.NEW.toString())) {
					alredyRead = word.isHaveReadNew();
				} else if (state.equals(WordUtil.OLD.toString())) {
					alredyRead = word.isHaveReadOld();
				}
//				DebugHelper.debug("each row iterate " + word.getEnglishWord()
//						+ " => " + word.isHaveReadNew() + " | "
//						+ word.isHaveReadOld());
//				DebugHelper.debug(!alredyRead);
				if (!alredyRead) {
					result.add(word);
				}
			}
		}
		WordEngine engine = new WordEngine();
		engine.setSqLiteDatabase(sqLiteDatabase);
		engine.setWordState(state);
		engine.setWords(result);
//		DebugHelper.debug("after generate engine");
//		DebugHelper.debug("size : " + engine.words.size());
		return engine;
	}
}
