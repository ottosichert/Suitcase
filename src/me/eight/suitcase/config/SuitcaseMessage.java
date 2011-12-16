package me.eight.suitcase.config;

import me.eight.suitcase.Suitcase;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class SuitcaseMessage {

	// define variables
	private FileConfiguration messagesYML;
	
	// set up messages
	public class messages {
		
		public class player { // messages players will get when voting etc.
			public String vote = "You have rated {player} successfully.";
			public String warn = "You have warned {player} successfully.";
		}
		
		public class info { // command info in plugin help
			public String help = "Displays this help.";
			public String info = "About this plugin.";
			public String vote = "Rate a player or show his rating level.";
			public String warn = "Warn a player.";
			public String reload = "Reloads configuration. ";
		}
		
		public class header { // first line of a command
			public String help = "Command help";
			public String info = "About Suitcase";
			public String vote = "Vote";
			public String warn = "Warn";
		}
		
		public class system { // general system messages or other elements
			public String frame = "---";
			public String dash = "-";
		}
		
		// TODO: Add custom event messages!
	}
	
	public void sendHelp(CommandSender sender) {
		sender.sendMessage(getHeader(Suitcase.msHeader.help));
	}
	
	private String getHeader(String text) {
		return Suitcase.cfColor.frame + Suitcase.msSystem.frame + Suitcase.cfColor.header + text + Suitcase.cfColor.frame + Suitcase.msSystem.frame;
	}
}
