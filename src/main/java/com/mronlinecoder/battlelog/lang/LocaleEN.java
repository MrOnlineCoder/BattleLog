package com.mronlinecoder.battlelog.lang;

public class LocaleEN extends BattleLocale {
	public LocaleEN() {
		add("battle_start", "You have entered the battle! Do not logout!");
		add("battle_end", "You are out of battle. Now you can logout");
		add("battle_info_false", "You are not in a battle.");
		add("battle_info_true", "You will be in battle for % more sec.");
		add("battle_cmd_deny", "Commands are disabled during the battle!");
		add("battle_punish", "You were punished for escape from the battlefield! ");
	}
}
