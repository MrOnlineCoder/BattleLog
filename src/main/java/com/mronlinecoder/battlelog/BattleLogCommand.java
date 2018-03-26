package com.mronlinecoder.battlelog;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class BattleLogCommand implements CommandExecutor{

	BattleLog plugin;
	
	public BattleLogCommand(BattleLog pl) {
		plugin = pl;
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (!(src instanceof Player)) {
			src.sendMessage(Text.of(TextColors.RED, "Only for players!"));
			return CommandResult.empty();
		}
		
		Player pl = (Player) src;
		
		if (plugin.fighters.get(pl.getName()) == null) {
			pl.sendMessage(Text.of(TextColors.DARK_RED, "[BattleLog] ", TextColors.GRAY, plugin.getLocale().tr("battle_info_false")));
		} else {
			int t = plugin.fighters.get(pl.getName());
			String msg = plugin.getLocale().tr("battle_info_true");
			msg = msg.replaceAll("%", String.valueOf(t));
			pl.sendMessage(Text.of(TextColors.DARK_RED, "[BattleLog] ", TextColors.GRAY, msg));
		}
		
		return CommandResult.success();
	}
	

}
