package ru.nekit.androidsamplelist.utils;

import java.util.Hashtable;

public class Translit {

	final static int NEUTRAL = 0;

	final static int UPPER = 1;

	final static int LOWER = 2;

	final static Hashtable<Character, String> map = makeTranslitMap();

	private static Hashtable<Character, String> makeTranslitMap() {
		Hashtable<Character, String> map = new Hashtable<Character, String>();
		map.put(new Character('�'), "a");
		map.put(new Character('�'), "b");
		map.put(new Character('�'), "v");
		map.put(new Character('�'), "g");
		map.put(new Character('�'), "d");
		map.put(new Character('�'), "e");
		map.put(new Character('�'), "yo");
		map.put(new Character('�'), "zh");
		map.put(new Character('�'), "z");
		map.put(new Character('�'), "i");
		map.put(new Character('�'), "j");
		map.put(new Character('�'), "k");
		map.put(new Character('�'), "l");
		map.put(new Character('�'), "m");
		map.put(new Character('�'), "n");
		map.put(new Character('�'), "o");
		map.put(new Character('�'), "p");
		map.put(new Character('�'), "r");
		map.put(new Character('�'), "s");
		map.put(new Character('�'), "t");
		map.put(new Character('�'), "u");
		map.put(new Character('�'), "f");
		map.put(new Character('�'), "h");
		map.put(new Character('�'), "ts");
		map.put(new Character('�'), "ch");
		map.put(new Character('�'), "sh");
		map.put(new Character('�'), "sh'");
		map.put(new Character('�'), "`");
		map.put(new Character('�'), "y");
		map.put(new Character('�'), "'");
		map.put(new Character('�'), "e");
		map.put(new Character('�'), "yu");
		map.put(new Character('�'), "ya");
		map.put(new Character('�'), "\"");
		map.put(new Character('�'), "\"");
		map.put(new Character('�'), "No");
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