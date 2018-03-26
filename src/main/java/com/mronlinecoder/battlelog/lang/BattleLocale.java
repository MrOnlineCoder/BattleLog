package com.mronlinecoder.battlelog.lang;

import java.util.HashMap;

public class BattleLocale {
	HashMap<String, String> translations = new HashMap<>();
	
	protected void add(String key, String trans) {
		translations.put(key, trans);
	}
	
	public String tr(String key) {
		return translations.get(key);
	}
}
