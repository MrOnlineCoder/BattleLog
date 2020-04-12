package com.mronlinecoder.battlelog;

import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.command.SendCommandEvent;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class BattleListener {
	BattleLog plugin;
	
	public BattleListener(BattleLog pl) {
		plugin = pl;
	}
	
	@Listener
	public void onDamage(DamageEntityEvent ev) {

		if (ev.getTargetEntity().getType() != EntityTypes.PLAYER) {
			return;
		}
		
		if (!(ev.getSource() instanceof EntityDamageSource)) return;
		
		EntityDamageSource dmg = (EntityDamageSource) ev.getSource();
	
		if (!(dmg.getSource() instanceof Player)) return;
		
		Player pl = (Player) dmg.getSource();
		Player target = (Player) ev.getTargetEntity();

		plugin.enterRoutines(pl);
		plugin.enterRoutines(target);
		
		plugin.fighters.put(pl.getName(), plugin.getConfig().getBattleTime());
		plugin.fighters.put(target.getName(), plugin.getConfig().getBattleTime());
	}
	
	
	@Listener
	public void onCmd(SendCommandEvent ev) {

		if (!(ev.getSource() instanceof Player)) return;
		
		Player pl = (Player) ev.getSource();
		
		if (ev.getCommand().equalsIgnoreCase("bl") || ev.getCommand().equalsIgnoreCase("battlelog")) return;
		if(plugin.config.whitelistedCommands.contains(ev.getCommand()) && !plugin.getConfig().blacklist
		|| !plugin.config.whitelistedCommands.contains(ev.getCommand()) && plugin.getConfig().blacklist)
			return;
		
		if (plugin.fighters.get(pl.getName()) != null) {
			ev.setCancelled(true);
			pl.sendMessage(Text.of(TextColors.DARK_RED, "[BattleLog] ", TextColors.GRAY, plugin.getLocale().tr("battle_cmd_deny")));
		}
	}
	
	@Listener
	public void onJoin(ClientConnectionEvent.Join ev) {

		Player pl = ev.getTargetEntity();
		
		if (plugin.dead.contains(pl.getName())) {
			plugin.dead.remove(pl.getName());
			
			pl.sendMessage(Text.of(TextColors.DARK_RED, "[BattleLog] ", TextColors.GRAY, plugin.getLocale().tr("battle_punish")));
		}
		
	}
	
	@Listener
	public void onQuit(ClientConnectionEvent.Disconnect ev) {
		Player pl = ev.getTargetEntity();
		
		if (plugin.fighters.get(pl.getName()) != null) {
			plugin.punish(pl);
			plugin.fighters.remove(pl.getName());
			plugin.dead.add(pl.getName());
		}
	}
}
