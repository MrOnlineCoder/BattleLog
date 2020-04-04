package com.mronlinecoder.battlelog;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class BattleConfig {
	private ConfigurationLoader<CommentedConfigurationNode> configLoader;
	private CommentedConfigurationNode config;

	private BattleLog plugin;

	int time;
	String lang;
	List<String> punishCmds;
	boolean actionbar;

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
				config.getNode("lang").setComment("Plugin locale (possible values: EN, RU, FR)").setValue("EN");
				config.getNode("actionbar").setComment("Enable combat actionbar").setValue(true);
				config.getNode("punishCmds").setComment("Punish commands to be executed. Leave empty to disable. Use % sign as placeholder for player name").setValue(Collections.singletonList(""));
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
	
	public List<String> getPunishCommand() {
		return punishCmds;
	}

	public void parseConfig() {
		time = config.getNode("time").getInt();
		lang = config.getNode("lang").getString();
		try {
			punishCmds = config.getNode("punishCmd").getList(TypeToken.of(String.class));
		} catch (ObjectMappingException e) {
			e.printStackTrace();
		}
		actionbar = config.getNode("actionbar").getBoolean();
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
