package com.mronlinecoder.battlelog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.mronlinecoder.battlelog.lang.LocaleFR;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;

import com.mronlinecoder.battlelog.lang.BattleLocale;
import com.mronlinecoder.battlelog.lang.LocaleEN;
import com.mronlinecoder.battlelog.lang.LocaleRU;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin( id = "battlelog", name = "BattleLog", version = "1.0")
public class BattleLog {

	@Inject
	private Logger logger;

	@Inject
	@DefaultConfig(sharedRoot = false)
	private File configFile;
	
	@Inject
	@DefaultConfig(sharedRoot = false)
	ConfigurationLoader<CommentedConfigurationNode> cfgloader;
	
	HashMap<String, Integer> fighters;
	ArrayList<String> dead;
	
	BattleConfig config;
	BattleLocale locale;

	public void createCommands() {		
		CommandSpec shopCmd = CommandSpec.builder()
				.description(Text.of("Check your current battle log status"))
				.executor(new BattleLogCommand(this))
				.build();

		Sponge.getCommandManager().register(this, shopCmd, "battlelog", "bl");
		logger.info("Created /bl command");
	}

	@Listener
	public void onReload(GameReloadEvent e) {
		config.loadConfig();
		config.parseConfig();
		
		loadLocale();
		
		logger.info("BattleLog reloaded.");
	}
	
	public void loadLocale() {
		String lang = config.getLang();
		if (lang.equalsIgnoreCase("EN")) {
			locale = new LocaleEN();
		} else if (lang.equalsIgnoreCase("RU")) {
			locale = new LocaleRU();
		} else if(lang.equalsIgnoreCase("FR")) {
			locale = new LocaleFR();
		} else{
			locale = new LocaleEN();
		}
	}
	
	public void enterRoutines(Player pl) {
		if (fighters.get(pl.getName()) == null) {
			pl.offer(Keys.GAME_MODE, GameModes.SURVIVAL);
			pl.offer(Keys.IS_FLYING, false);
			pl.sendMessage(Text.of(TextColors.DARK_RED, "[BattleLog] ", TextColors.GRAY, locale.tr("battle_start")));
		}
	}

	@Listener
	public void onStop(GameStoppingEvent event) {

		logger.info("BattleLog stop routines have been executed.");
	}

	public void tick() {
		 for(Iterator<Map.Entry<String, Integer>> it = fighters.entrySet().iterator(); it.hasNext(); ) {
		 	Map.Entry<String, Integer> entry = it.next();
		     
		 	fighters.put(entry.getKey(), entry.getValue()-1);

		     if(config.actionbar){
				 Optional<Player> pl = Sponge.getServer().getPlayer(entry.getKey());
				 pl.ifPresent(player ->{
					 player.sendMessages(ChatTypes.ACTION_BAR, Text.of(TextColors.DARK_RED, locale.tr("battle_actionbar").replace("%s", String.valueOf(entry.getValue()-1))));
				 });
			 }

		 	if (entry.getValue() - 1 <= 0) {
				sendOutMessage(entry.getKey());
				it.remove();
			}
		 }
		
	}
	
	public void sendOutMessage(String key) {
		Optional<Player> pl = Sponge.getServer().getPlayer(key);
		
		if (!pl.isPresent()) return;

		if(config.actionbar){
			pl.get().sendMessage(ChatTypes.ACTION_BAR, Text.of(TextColors.DARK_RED, locale.tr("battle_end")));
		}

		pl.get().sendMessage(Text.of(TextColors.DARK_RED, "[BattleLog] ", TextColors.GRAY, locale.tr("battle_end")));
	}

	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		logger.info("=== BattleLog Starting ===");
		logger.info("Created by MrOnlineCoder");

		fighters = new HashMap<>();
		dead = new ArrayList<>();
		
		logger.info("Loading configuration...");
		config = new BattleConfig(this);
		config.setup(configFile, cfgloader);
		
		logger.info("Loading locale "+config.getLang());
		loadLocale();
		
		logger.info("Creating commands...");
		createCommands();
		
		logger.info("Setting listeners...");
		Sponge.getEventManager().registerListeners(this, new BattleListener(this));
		
		logger.info("Adding Scheduler task");
		Task.Builder taskBuilder = Task.builder();
		taskBuilder.execute(this::tick).interval(1, TimeUnit.SECONDS).delay(1, TimeUnit.SECONDS).name("BattleLog TickTask").submit(this);

		logger.info("! BattleLog loaded and started !");
	}

	public void punish(Player pl) {	
		Optional<ItemStack> stack = pl.getInventory().poll();
		while (stack.isPresent()) {
			 Entity optItem = pl.getLocation().getExtent().createEntity(EntityTypes.ITEM, pl.getLocation().getPosition());
			Item item = (Item) optItem;
			item.offer(Keys.REPRESENTED_ITEM, stack.get().createSnapshot());
			pl.getWorld().spawnEntity(item);

			stack = pl.getInventory().poll();
		}
		pl.getInventory().clear();


		config.getPunishCommand().forEach(cmd ->{
			if (cmd.length() == 0) return;
			cmd = cmd.replaceAll("%", pl.getName());
			Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmd);
		});

	}
	
	public Logger getLogger() {
		return logger;
	}
	
	public BattleConfig getConfig() {
		return config;
	}
	
	public BattleLocale getLocale() {
		return locale;
	}
}
