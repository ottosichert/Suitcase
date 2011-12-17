package me.eight.suitcase.event;

import me.eight.suitcase.Suitcase;

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
		if (Suitcase.scPermission.hasPermission(sender, "suitcase.help")) {
			Suitcase.scMessage.sendHelp(sender, command, argument);
		}
		else {
			Suitcase.scMessage.sendDeny(sender, command, argument);
		}
	}
	
	private void executeInfo(CommandSender sender, String command, String argument) {
		if (Suitcase.scPermission.hasPermission(sender, "suitcase.info")) {
			Suitcase.scMessage.sendHelp(sender, command, argument);
		}
		else {
			Suitcase.scMessage.sendDeny(sender, command, argument);
		}
	}
	
	private void executeVote(CommandSender sender, String command, String argument) {
		if (Suitcase.scPermission.hasPermission(sender, "suitcase.vote")) {
			Suitcase.scMessage.sendHelp(sender, command, argument);
		}
		else {
			Suitcase.scMessage.sendDeny(sender, command, argument);
		}
	}
	
	private void executeWarn(CommandSender sender, String command, String argument) {
		if (Suitcase.scPermission.hasPermission(sender, "suitcase.warn")) {
			Suitcase.scMessage.sendHelp(sender, command, argument);
		}
		else {
			Suitcase.scMessage.sendDeny(sender, command, argument);
		}
	}
	
	private void executeReload(CommandSender sender, String command, String argument) {
		if (Suitcase.scPermission.hasPermission(sender, "suitcase.reload")) {
			Suitcase.scMessage.sendHelp(sender, command, argument);
		}
		else {
			Suitcase.scMessage.sendDeny(sender, command, argument);
		}
	}
	
	private void executeUnknown(CommandSender sender, String command, String argument) {
		Suitcase.scMessage.sendUnknown(sender, command, argument);
	}
}
