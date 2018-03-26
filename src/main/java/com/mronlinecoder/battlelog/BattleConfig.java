package com.mronlinecoder.battlelog;

import java.io.File;
import java.io.IOException;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class BattleConfig {
	private ConfigurationLoader<CommentedConfigurationNode> configLoader;
	private CommentedConfigurationNode config;

	private BattleLog plugin;

	int time;
	String lang;
	String punishCmd;

	public BattleConfig(BattleLog pl) {
		this.plugin = pl;
	}

	public void setup(File configFile, ConfigurationLoader<CommentedConfigurationNode> configLoader) {
		this.configLoader = configLoader;

		if (!configFile.exists()) {
			try {
				configFile.createNewFile();
				loadConfig();
				config.getNode("time").setComment("Duration of the battle (in seconds)").setValue(10);
				config.getNode("lang").setComment("Plugin locale (possible values: EN, RU)").setValue("EN");
				config.getNode("punishCmd").setComment("Punish command to be executed. Leave empty to disable. Use % sign as placeholder for player name").setValue("");
				saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			loadConfig();
		}

		parseConfig();
	}

	public CommentedConfigurationNode getConfig() {
		return config;
	}

	public void saveConfig() {
		try {
			configLoader.save(config);
		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getBattleTime() {
		return time;
	}
	
	public String getLang() {
		return lang;
	}
	
	public String getPunishCommand() {
		return punishCmd;
	}

	public void parseConfig() {
		time = config.getNode("time").getInt();
		lang = config.getNode("lang").getString();
		punishCmd = config.getNode("punishCmd").getString();
	}

	public void loadConfig() {
		try {
			config = configLoader.load();
		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
