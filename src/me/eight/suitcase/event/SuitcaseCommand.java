package me.eight.suitcase.event;

import me.eight.suitcase.Suitcase;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SuitcaseCommand implements CommandExecutor {
	public static Suitcase plugin;

	public SuitcaseCommand(Suitcase instance) {
		plugin = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		String cmd = args[0];
		String arg = "";
		if (args.length > 0) arg = args[1];
		
		if (cmd == "vote" || cmd == "v") vote(sender, cmd, arg); // Vote a player
		else if (cmd == "warn" || cmd == "w") warn(sender, cmd, arg); // Warn a player
		else if (cmd == "info" || cmd == "i") info(); // Show plugin info
		else if (cmd == "reload" || cmd == "r") plugin.reload(); // Reload plugin
		else help(sender, cmd); // Help or command not found
		
		return true;
	}

	private void info() {
		
	}

	private void warn(CommandSender sender, String cmd, String arg) {
		
	}

	private void vote(CommandSender sender, String cmd, String arg) {

		
	}

	private void help(CommandSender sender, String cmd) {
		if (!cmd.isEmpty() || !(cmd.toLowerCase() == "?") || !(cmd.toLowerCase() == "help")) {
			sender.sendMessage("Command " + cmd + " not found."); // Send message when using unknown command
		}
		
		
	}
}
