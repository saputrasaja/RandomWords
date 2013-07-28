package com.wiwit.connection;

public class WordInfoUtil {

	private int all;
	private int newWord;
	private int oldWord;

	public WordInfoUtil() {
		all = 0;
		oldWord = 0;
		newWord = 0;
	}

	public int getAll() {
		return all;
	}

	public void setAll(int all) {
		this.all = all;
	}

	public int getNewWord() {
		return newWord;
	}

	public void setNewWord(int newWord) {
		this.newWord = newWord;
	}

	public int getOldWord() {
		return oldWord;
	}

	public void setOldWord(int oldWord) {
		this.oldWord = oldWord;
	}

	public void incrementAll() {
		all++;
	}

	public void incrementOld() {
		oldWord++;
	}

	public void incrementNew() {
		newWord++;
	}
}
