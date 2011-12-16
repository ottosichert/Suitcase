package me.eight.suitcase.event;

import me.eight.suitcase.Suitcase;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SuitcaseCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// split arguments
		String cmd = "", arg = "";
		if (args.length > 0) cmd = args[0];
		if (args.length > 1) arg = args[1];
		
		// execute commands
		if (cmd == "help" || cmd == "h" || cmd == "?") help(sender, cmd); // show help message
		else if (cmd == "info" || cmd == "i" || cmd == "about" || cmd == "a") info(sender); // show plugin info
		else if (cmd == "vote" || cmd == "v") vote(sender, cmd, arg); // rate players
		else if (cmd == "warn" || cmd == "w") warn(sender, cmd, arg); // warn players
		else if (cmd == "reload" || cmd == "r") reload(sender); // reload plugin
		else unknown(sender, "/" + label + " " + cmd + " " + arg); // command not found
		return true;
	}
	
	private void help(CommandSender sender, String cmd) {
		// check permission for help command
		if (Suitcase.scPermission.hasPermission(sender, "suitcase.help")) {
			if (!Suitcase.cfAppearance.full_help || Suitcase.scPermission.hasPermission(sender, "suitcase.info")) {
				return;
			}
			else {
				
			}
		}
		else {
			// msPermission.send(USER_NOT_ALLOWED); blah blah.
		}
	}
	private void info(CommandSender sender) {
		
	}

	private void vote(CommandSender sender, String cmd, String arg) {
		
	}

	private void warn(CommandSender sender, String cmd, String arg) {
		
	}

	private void reload(CommandSender sender) {
		
	}
	
	private void unknown(CommandSender sender, String command) {
		
	}
}
