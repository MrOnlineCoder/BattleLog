package com.mronlinecoder.battlelog.lang;

public class LocaleRU extends BattleLocale {
	public LocaleRU() {
		add("battle_start", "Вы вошли в режим боя! Отключение приведет к потере вещей!");
		add("battle_end", "Вы вышли из боя.");
		add("battle_info_false", "Вы сейчас не в бою.");
		add("battle_info_true", "Вы будете в бою еще % сек.");
		add("battle_cmd_deny", "Команды отключены во время боя!");
		add("battle_punish", "Вы были наказаны за побег с поля боя!");
		add("battle_actionbar", "Боевой режим: % секунд осталось");
	}
}
