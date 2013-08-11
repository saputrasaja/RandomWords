package com.wiwit.util;

import java.util.ArrayList;

import com.wiwit.connection.Word;
import com.wiwit.connection.WordUtil;

public class ConverterHelper {

	public static ArrayList<Word> convertFromString(String str) {
		// abduct@_@menculik@_@true@_@false@-@
		ArrayList<Word> words = new ArrayList<Word>();
		for (String s : str.split("@-@")) {
			Word w = new Word();
			w.setEnglishWord(s.split("@_@")[0]);
			w.setIndonesianWord(s.split("@_@")[1]);
			if (Boolean.valueOf((s.split("@_@")[3]))) {
				w.setState(WordUtil.DELETE.toString());
			} else {
				w.setState(WordUtil.OLD.toString());
				if (Boolean.valueOf((s.split("@_@")[2]))) {
					w.setState(WordUtil.NEW.toString());
				}
			}
			words.add(w);
		}
		return words;
	}

	public static ArrayList<Word> getFromLocalVariable() {
		return convertFromString(StaticData.str);
	}

}
