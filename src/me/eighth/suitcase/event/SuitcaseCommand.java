package me.eighth.suitcase.event;

import java.util.Arrays;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseLog.actionType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuitcaseCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// execute commands
		// args[0] -> sub command (help, info etc.)
		if (Arrays.asList(Suitcase.configKeys.get("commands.help")).contains(args[0])) executeHelp(sender, args);
		else if (Arrays.asList(Suitcase.configKeys.get("commands.info")).contains(args[0])) executeInfo(sender, args);
		else if (Arrays.asList(Suitcase.configKeys.get("commands.rate")).contains(args[0])) executeRate(sender, args);
		else if (Arrays.asList(Suitcase.configKeys.get("commands.warn")).contains(args[0])) executeWarn(sender, args);
		else if (Arrays.asList(Suitcase.configKeys.get("commands.reload")).contains(args[0])) executeReload(sender, args);
		else executeUnknown(sender, args);
		return true;
	}
	
	private void executeHelp(CommandSender sender, String...args) {
		if (Suitcase.utPermission.hasPermission(sender, "suitcase.help")) {
			Suitcase.cfMessage.sendHelp(sender, args);
		}
		else {
			Suitcase.cfMessage.sendDeny(sender, args);
		}
	}
	
	private void executeInfo(CommandSender sender, String...args) {
		if (Suitcase.utPermission.hasPermission(sender, "suitcase.info")) {
			Suitcase.cfMessage.sendHelp(sender, args);
		}
		else {
			Suitcase.cfMessage.sendDeny(sender, args);
		}
	}
	
	private void executeRate(CommandSender sender, String...args) {
		// check if args[1] == null ...
		if (!(sender instanceof Player)) {
			// player is console
			executeConsole(sender, args);
			return;
		}
		
		// targeted player
		Player target = (Player) Suitcase.plugin.getServer().getPlayer(args[1]);
		
		if (target == null) {
			// can't find player
			Suitcase.cfMessage.sendPlayer(sender, args);
			Suitcase.utLog.logAction(actionType.PLAYER_COMMAND_ERROR, sender.getName(), "/suitcase rate " + args[1] + " " + args[2], "playerNotFound");
			return;
		}
		
		if (Suitcase.utPermission.hasPermission(sender, "suitcase.rate")) {
			
			// view rating
			if (args[2] == null) {
				Suitcase.cfMessage.sendRate(sender, args);
				Suitcase.utLog.logAction(actionType.PLAYER_COMMAND_EXECUTE, sender.getName(), "/suitcase rate " + args[1]);
			}
			// positive rating
			else if (Arrays.asList(Suitcase.configKeys.get("commands.rate-positive")).contains(args[2])) {
				Suitcase.lgFile.addRating(sender.getName(), 1);
				Suitcase.cfMessage.sendRate(sender, args);
				Suitcase.utLog.logAction(actionType.PLAYER_COMMAND_EXECUTE, sender.getName(), "/suitcase rate " + args[1] + " positive");
			}
			// neutral rating
			else if (Arrays.asList(Suitcase.configKeys.get("commands.rate-positive")).contains(args[2])) {
				Suitcase.lgFile.addRating(sender.getName(), 0);
				Suitcase.cfMessage.sendRate(sender, args);
				Suitcase.utLog.logAction(actionType.PLAYER_COMMAND_EXECUTE, sender.getName(), "/suitcase rate " + args[1] + " neutral");
			}
			// negative rating
			else if (Arrays.asList(Suitcase.configKeys.get("commands.rate-positive")).contains(args[2])) {
				Suitcase.lgFile.addRating(sender.getName(), -1);
				Suitcase.cfMessage.sendRate(sender, args);
				Suitcase.utLog.logAction(actionType.PLAYER_COMMAND_EXECUTE, sender.getName(), "/suitcase rate " + args[1] + " negative");
			}
		}
		else {
			Suitcase.cfMessage.sendDeny(sender, args);
			Suitcase.utLog.logAction(actionType.PLAYER_COMMAND_DENY, sender.getName(), "/suitcase rate" + args[1] + " " + args[2]);
		}
	}

	private void executeWarn(CommandSender sender, String...args) {
		if (!(sender instanceof Player)) {
			// player is console
			executeConsole(sender, args);
			return;
		}
		
		// targeted player
		Player target = (Player) Suitcase.plugin.getServer().getPlayer(args[1]);
		
		if (target == null) {
			// can't find player
			Suitcase.cfMessage.sendPlayer(sender, args);
			Suitcase.utLog.logAction(actionType.PLAYER_COMMAND_ERROR, sender.getName(), "/suitcase " + args[0] + " " + args[1], "playerNotFound");
			return;
		}
		
		if (Suitcase.utPermission.hasPermission(sender, "suitcase.rate")) {
				
			// warn
			if (Arrays.asList(Suitcase.configKeys.get("commands.warn")).contains(args[0])) {
				Suitcase.lgFile.addWarning(sender.getName(), 1);
				Suitcase.cfMessage.sendRate(sender, args);
				Suitcase.utLog.logAction(actionType.PLAYER_COMMAND_EXECUTE, sender.getName(), "/suitcase " + args[0] + " " + args[1]);
			}
			// forgive
			else if (Arrays.asList(Suitcase.configKeys.get("commands.warn-forgive")).contains(args[0])) {
				Suitcase.lgFile.addWarning(sender.getName(), -1);
				Suitcase.cfMessage.sendRate(sender, args);
				Suitcase.utLog.logAction(actionType.PLAYER_COMMAND_EXECUTE, sender.getName(), "/suitcase " + args[0] + " " + args[1]);
			}
		}
		else {
			Suitcase.cfMessage.sendDeny(sender, args);
			Suitcase.utLog.logAction(actionType.PLAYER_COMMAND_DENY, sender.getName(), "/suitcase " + args[0] + " " + args[1]);
		}
	}
	
	private void executeReload(CommandSender sender, String...args) {
		if (Suitcase.utPermission.hasPermission(sender, "suitcase.reload")) {
			Suitcase.cfMessage.sendHelp(sender, args);
		}
		else {
			Suitcase.cfMessage.sendDeny(sender, args);
		}
	}
	
	private void executeUnknown(CommandSender sender, String...args) {
		Suitcase.cfMessage.sendUnknown(sender, args);
	}
	
	private void executeConsole(CommandSender sender, String...args) {
		Suitcase.cfMessage.sendConsole(sender, args);
	}
}
