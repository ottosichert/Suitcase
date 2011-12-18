package me.eighth.suitcase.event;

import me.eighth.suitcase.Suitcase;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SuitcaseCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// split arguments
		String subCommand = "", arguments = "";
		if (args.length > 0) subCommand = args[0];
		if (args.length > 1) arguments = args[1];
		if (args.length > 2) arguments += " " + args[1];
		
		// execute commands
		if (subCommand == "help" || subCommand == "h" || subCommand == "?") executeHelp(sender, subCommand, arguments);
		else if (subCommand == "info" || subCommand == "i" || subCommand == "about" || subCommand == "a") executeInfo(sender, subCommand, arguments);
		else if (subCommand == "vote" || subCommand == "v") executeVote(sender, subCommand, arguments);
		else if (subCommand == "warn" || subCommand == "w") executeWarn(sender, subCommand, arguments);
		else if (subCommand == "reload" || subCommand == "r") executeReload(sender, subCommand, arguments);
		else executeUnknown(sender, subCommand, arguments);
		return true;
	}
	
	private void executeHelp(CommandSender sender, String command, String argument) {
		if (Suitcase.utPermission.hasPermission(sender, "suitcase.help")) {
			Suitcase.cfMessage.sendHelp(sender, command, argument);
		}
		else {
			Suitcase.cfMessage.sendDeny(sender, command, argument);
		}
	}
	
	private void executeInfo(CommandSender sender, String command, String argument) {
		if (Suitcase.utPermission.hasPermission(sender, "suitcase.info")) {
			Suitcase.cfMessage.sendHelp(sender, command, argument);
		}
		else {
			Suitcase.cfMessage.sendDeny(sender, command, argument);
		}
	}
	
	private void executeVote(CommandSender sender, String command, String argument) {
		if (Suitcase.utPermission.hasPermission(sender, "suitcase.vote")) {
			//Player target
			Player target = (Player) this.getServer().getPlayer(args[1]);
			//Positiv Karma
			if (args[2] == "+" || args[2] == "positive")
				//TODO Suitcase.cfEvent.positiv
			//Negativ Karma	
			else if (args[2] == "-" || args[2] == "negative")
				//TODO Suitcase.cfEvent.negativ
			//Neutral Karma	
			else if (args[2] == "/." || args[2] == "neutral")
				//TODO Suitcase.cfEvent.neutral
			else
				Suitcase.cfMessage.sendHelp(sender, command, argument);
		}
		else {
			Suitcase.cfMessage.sendDeny(sender, command, argument);
		}
	}
	
	private void executeWarn(CommandSender sender, String command, String argument) {
		if (Suitcase.utPermission.hasPermission(sender, "suitcase.warn")) {
			//Player target
			Player target = (Player) this.getServer().getPlayer(args[1]);
			if (args[2] == "forgive")
				//TODO Reset Warnings
			else if 
				//TODO Warnings +1
			else
			Suitcase.cfMessage.sendHelp(sender, command, argument);
		}
		else {
			Suitcase.cfMessage.sendDeny(sender, command, argument);
		}
	}
	
	private void executeReload(CommandSender sender, String command, String argument) {
		if (Suitcase.utPermission.hasPermission(sender, "suitcase.reload")) {
			Suitcase.cfMessage.sendHelp(sender, command, argument);
		}
		else {
			Suitcase.cfMessage.sendDeny(sender, command, argument);
		}
	}
	
	private void executeUnknown(CommandSender sender, String command, String argument) {
		Suitcase.cfMessage.sendUnknown(sender, command, argument);
	}
}
