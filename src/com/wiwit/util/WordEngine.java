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
		// DebugHelper.debug("canRandomWord");
		// DebugHelper.debug(getWords().size() > 2);
		// DebugHelper.debug(getWords().size() > (getWords().size() / 2));
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

	private void findAndUpdate(Word word, String method) {
		if (wordState.equals(WordUtil.NEW.toString())) {
			if (method.equals(ALREDY_READ)) {
				word.setHaveReadNew(true);
			} else if (method.equals(DOWN_STATE)) {
				word.setHaveReadNew(false);
				word.setState(WordUtil.OLD.toString());
			}
			word.update(sqLiteDatabase, word.getEnglishWord());
			remove(word);
		} else if (wordState.equals(WordUtil.OLD.toString())) {
			if (method.equals(ALREDY_READ)) {
				word.setHaveReadOld(true);
			} else if (method.equals(DOWN_STATE)) {
				word.setHaveReadOld(false);
				word.setState(WordUtil.DELETE.toString());
			} else if (method.equals(UP_STATE)) {
				word.setHaveReadNew(false);
				word.setState(WordUtil.NEW.toString());
			}
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
					alredyRead = word.isHaveReadNew();
				} else if (state.equals(WordUtil.OLD.toString())) {
					alredyRead = word.isHaveReadOld();
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
					word.setHaveReadNew(false);
				} else if (word.getState().equals(WordUtil.OLD.toString())) {
					word.setHaveReadOld(false);
				}
				word.update(sqLiteDatabase, word.getEnglishWord());
			}
		}
		setWords(generateWordEngine(Word.getAllRow(this.sqLiteDatabase),
				this.wordState, this.sqLiteDatabase).getWords());
	}
}