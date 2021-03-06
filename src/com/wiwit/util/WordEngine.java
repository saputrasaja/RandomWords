package com.wiwit.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.wiwit.connection.Word;
import com.wiwit.connection.WordUtil;

public class WordEngine {
	public static final String DOWN_STATE = "DOWN_STATE";
	public static final String UP_STATE = "UP_STATE";
	public static final String ALREDY_READ = "ALREDY_READ";
	private List<Word> words;
	protected String wordState;
	private SQLiteDatabase sqLiteDatabase;

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
		if (getWords().size() > 0) {
			return true;
		}
		return false;
	}

	public Word getRandoWords() {
		if (!canRandomWord()) {
			return null;
		}
		int random= (int) (Math.random() * (this.words.size()));
		return this.words.get(random);
	}

	private void findAndUpdate(Word word, String method) {
		if (wordState.equals(WordUtil.NEW.toString())) {
			if (method.equals(ALREDY_READ)) {
				word.setHasReadNew(true);
			} else if (method.equals(DOWN_STATE)) {
				word.setHasReadNew(false);
				word.setState(WordUtil.OLD.toString());
			}
			word.update(sqLiteDatabase, word.getEnglishWord());
			remove(word);
		} else if (wordState.equals(WordUtil.OLD.toString())) {
			if (method.equals(ALREDY_READ)) {
				word.setHasReadOld(true);
			} else if (method.equals(DOWN_STATE)) {
				word.setHasReadOld(false);
				word.setState(WordUtil.DELETE.toString());
			} else if (method.equals(UP_STATE)) {
				word.setHasReadNew(false);
				word.setState(WordUtil.NEW.toString());
			}
			word.update(sqLiteDatabase, word.getEnglishWord());
			remove(word);
		} else if (wordState.equals(WordUtil.DELETE.toString())) {
			if (method.equals(ALREDY_READ)) {
				word.setHasReadDel(true);
			} else if (method.equals(UP_STATE)) {
				word.setHasReadOld(false);
				word.setState(WordUtil.OLD.toString());
			}
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
		findAndUpdate(word, ALREDY_READ);
	}

	public void downState(Word word) {
		findAndUpdate(word, DOWN_STATE);
	}

	public void upState(Word word) {
		findAndUpdate(word, UP_STATE);
	}

	public static WordEngine generateWordEngine(HashMap<String, Word> allWords,
			String state, SQLiteDatabase sqLiteDatabase) {
		List<Word> result = new ArrayList<Word>();
		for (String key : allWords.keySet()) {
			Word word = allWords.get(key);
			boolean alredyRead = true;
			if (word.getState().equals(state)) {
				if (state.equals(WordUtil.NEW.toString())) {
					alredyRead = word.isHasReadNew();
				} else if (state.equals(WordUtil.OLD.toString())) {
					alredyRead = word.isHasReadOld();
				} else if (state.equals(WordUtil.DELETE.toString())) {
					alredyRead = word.isHasReadDel();
				}
				if (!alredyRead) {
					result.add(word);
				}
			}
		}
		WordEngine engine = new WordEngine();
		engine.setSqLiteDatabase(sqLiteDatabase);
		engine.setWordState(state);
		engine.setWords(result);
		return engine;
	}

	public void restartWord() {
		HashMap<String, Word> words = Word.getAllRow(this.sqLiteDatabase);
		for (String key : words.keySet()) {
			Word word = words.get(key);
			if (word.getState().equals(this.wordState)) {
				if (word.getState().equals(WordUtil.NEW.toString())) {
					word.setHasReadNew(false);
				} else if (word.getState().equals(WordUtil.OLD.toString())) {
					word.setHasReadOld(false);
				} else if (word.getState().equals(WordUtil.DELETE.toString())) {
					word.setHasReadDel(false);
				}
				word.update(sqLiteDatabase, word.getEnglishWord());
			}
		}
		setWords(generateWordEngine(Word.getAllRow(this.sqLiteDatabase),
				this.wordState, this.sqLiteDatabase).getWords());
	}
}