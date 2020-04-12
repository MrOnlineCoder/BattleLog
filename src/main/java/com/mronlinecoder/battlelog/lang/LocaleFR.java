package com.mronlinecoder.battlelog.lang;

public class LocaleFR extends BattleLocale {

    public LocaleFR() {
        add("battle_start", "Vous êtes en combat ! Ne déconnectez pas !");
        add("battle_end", "Vous n'êtes plus en combat. Vous pouvez déconnecter.");
        add("battle_info_false", "Vous n'êtes pas en combat.");
        add("battle_info_true", "Vous êtes en combat pendant encore % secondes.");
        add("battle_cmd_deny", "Cette commande est désactivée en combat !");
        add("battle_punish", "Vous avez été puni pour avoir quitté le combat !");
        add("battle_actionbar", "Mode combat : % secondes restantes");
    }

}
