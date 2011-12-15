package me.eight.suitcase.event;

import me.eight.suitcase.Suitcase;
import me.eight.suitcase.config.SuitcaseColor.ElementType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SuitcaseCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// split arguments
		String cmd = args[0];
		String arg = "";
		if (args.length > 0) arg = args[1];
		
		// execute commands
		if (cmd == "vote" || cmd == "v") vote(sender, cmd, arg); // rate players
		else if (cmd == "warn" || cmd == "w") warn(sender, cmd, arg); // warn players
		else if (cmd == "info" || cmd == "i") info(); // show plugin info
		else if (cmd == "reload" || cmd == "r") Suitcase.plugin.reload(); // reload plugin
		else if (cmd == "help" || cmd == "h" || cmd == "?") help(sender, cmd); // show help message
		else sender.sendMessage(Suitcase.scColor.getColor(ElementType.ERROR) + "Command " + command + " " + cmd + " not found. Type /suitcase help"); // command not found
		return true;
	}
	
	private void info() {
		
	}

	private void warn(CommandSender sender, String cmd, String arg) {
		
	}

	private void vote(CommandSender sender, String cmd, String arg) {

		
	}

	private void help(CommandSender sender, String cmd) {
		
		
	}
}
