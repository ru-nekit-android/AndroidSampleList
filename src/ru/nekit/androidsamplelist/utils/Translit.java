package ru.nekit.androidsamplelist.utils;

import java.util.Hashtable;

public class Translit {

	final static int NEUTRAL = 0;

	final static int UPPER = 1;

	final static int LOWER = 2;

	final static Hashtable<Character, String> map = makeTranslitMap();

	private static Hashtable<Character, String> makeTranslitMap() {
		Hashtable<Character, String> map = new Hashtable<Character, String>();
		map.put(new Character('à'), "a");
		map.put(new Character('á'), "b");
		map.put(new Character('â'), "v");
		map.put(new Character('ã'), "g");
		map.put(new Character('ä'), "d");
		map.put(new Character('å'), "e");
		map.put(new Character('Þ'), "yo");
		map.put(new Character('æ'), "zh");
		map.put(new Character('ç'), "z");
		map.put(new Character('è'), "i");
		map.put(new Character('é'), "j");
		map.put(new Character('ê'), "k");
		map.put(new Character('ë'), "l");
		map.put(new Character('ì'), "m");
		map.put(new Character('í'), "n");
		map.put(new Character('î'), "o");
		map.put(new Character('ï'), "p");
		map.put(new Character('ð'), "r");
		map.put(new Character('ñ'), "s");
		map.put(new Character('ò'), "t");
		map.put(new Character('ó'), "u");
		map.put(new Character('ô'), "f");
		map.put(new Character('õ'), "h");
		map.put(new Character('ö'), "ts");
		map.put(new Character('÷'), "ch");
		map.put(new Character('ø'), "sh");
		map.put(new Character('ù'), "sh'");
		map.put(new Character('ú'), "`");
		map.put(new Character('û'), "y");
		map.put(new Character('ü'), "'");
		map.put(new Character('ý'), "e");
		map.put(new Character('þ'), "yu");
		map.put(new Character('ß'), "ya");
		map.put(new Character('Ç'), "\"");
		map.put(new Character('È'), "\"");
		map.put(new Character('Ü'), "No");
		return map;
	}

	private static int charClass(char c) {
		if (Character.isLowerCase(c))
			return LOWER;
		if (Character.isUpperCase(c))
			return UPPER;
		return NEUTRAL;
	}

	public static String translit(String text) {
		int len = text.length();
		if (len == 0)
			return text;
		StringBuffer sb = new StringBuffer();
		int pc = NEUTRAL;
		char c = text.charAt(0);
		int cc = charClass(c);
		for (int i = 1; i <= len; i++) {
			char nextChar = (i < len ? text.charAt(i) : ' ');
			int nc = charClass(nextChar);
			Character co = new Character(Character.toLowerCase(c));
			String tr = (String) map.get(co);
			if (tr == null) {
				sb.append(c);
			} else {
				switch (cc) {
				case LOWER:
				case NEUTRAL:
					sb.append(tr);
					break;
				case UPPER:
					if (nc == LOWER || (nc == NEUTRAL && pc != UPPER)) {
						sb.append(Character.toUpperCase(tr.charAt(0)));
						if (tr.length() > 0) {
							sb.append(tr.substring(1));
						}
					} else {
						sb.append(tr.toUpperCase());
					}
				}
			}
			c = nextChar;
			pc = cc;
			cc = nc;
		}
		return sb.toString();
	}
}