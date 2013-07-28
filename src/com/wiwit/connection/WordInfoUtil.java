package com.wiwit.connection;

public class WordInfoUtil {

	private int newWord;
	private int oldWord;
	private int deleted;

	public WordInfoUtil() {
		oldWord = 0;
		newWord = 0;
		setDeleted(0);
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

	public int getDeleted() {
		return deleted;
	}

	public void setDeleted(int deleted) {
		this.deleted = deleted;
	}

	public void incrementOld() {
		oldWord++;
	}

	public void incrementNew() {
		newWord++;
	}

	public void incrementDelete() {
		deleted++;
	}

	public int getAllWord() {
		return this.deleted + this.newWord + this.oldWord;
	}
}
