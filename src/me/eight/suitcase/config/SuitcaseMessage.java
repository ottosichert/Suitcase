package me.eight.suitcase.config;

import me.eight.suitcase.Suitcase;
import me.eight.suitcase.util.SuitcaseLog.ErrorType;

import org.bukkit.command.CommandSender;
// import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SuitcaseMessage {

	// define variables
	/*
	private FileConfiguration messagesYML;
	TODO: read messages.yml
	*/
	
	// set up messages
	public static class messages {
		
		public static class info { // command info in plugin help
			public String layout = "{command_color}{command_name} {frame_color}- {text_color}{command_info}";
			public String help = "Displays this help.";
			public String info = "About this plugin.";
			public String vote = "Rate a player or show his rating level.";
			public String warn = "Warn a player.";
			public String reload = "Reloads configuration.";
		}
		
		public static class header { // first line of a command
			public String layout = "{frame_color}--- {info_color}{header} {frame_color}---";
			public String help = "Command help";
			public String info = "About Suitcase";
			public String vote = "Vote";
			public String warn = "Warn";
		}
		
		public static class command { // general system messages or commands
			public String vote = "{text_color}{target} {info_color}has been rated.";
			public String warn = "{info_color}You have warned {text_color}{target} {info_color}!";
			public String deny = "You don't have permission for {command}!";
			public String unknown = "Can't find command {command}. Type {command_color} /suitcase help{text_color}.";
			public String error = "Error: {error}!";
		}
		
		// TODO: Add custom event messages!
	}
	
	// parses {variables}
	private String parseMessage(Player player, String message, String...addition) {
		String[] split = message.split("{");
		String var = "";
		String result = split[0];
		for (int i = 1; i < split.length; i++) {
			result += " ";
			var = split[i].split("}", 2)[0].toLowerCase();
			// colors
			if (var == "header_color") result += Suitcase.cfColor.header;
			else if (var == "frame_color") result += Suitcase.cfColor.frame;
			else if (var == "text_color") result += Suitcase.cfColor.text;
			else if (var == "info_color") result += Suitcase.cfColor.info;
			else if (var == "command_color") result += Suitcase.cfColor.command;
			else if (var == "error_color") result += Suitcase.cfColor.error;
			// some generated stuff
			else if (var == "time") result += String.valueOf(player.getWorld().getTime() / 1000) + ":" + String.valueOf(player.getWorld().getTime() % 1000 * 6 / 100);
			else if (var == "name") result += player.getName();
			// passed information
			// TODO: handle multiple additions
			else if (var == "command_name") result += addition[0];
			else if (var == "command_info") result += addition[1]; // won't work here..
			else if (var == "error") result += addition;
			else { // TODO: move this section to plugin startup
				Suitcase.scLogger.sendError(ErrorType.VARIABLE_NOT_FOUND, "{" + var + "}");
				result += "{" + var + "}";
			}
			
			if (split[i].contains("}")) { // therefore split[i].split(blah) returns two strings
				result += split[i].split("}", 2)[1];
			}
		}
		return result.trim();
	}
	
	
	// sends help with header and available commands
	public void sendHelp(CommandSender sender, String command, String argument) {
		Player p = null;
		if (sender instanceof Player) {
			p = (Player)sender;
		}
		// send header
		sender.sendMessage(parseMessage(p, Suitcase.msHeader.help, "/suitcase help"));
		// TODO: check permissions and full_help
	}
	
	// gives information about plugin and authors
	public void sendInfo(CommandSender sender, String command, String argument) {
		// TODO: provide plugin information and authors
	}
	
	// shows information about current rating
	public void sendVote(CommandSender sender, String command, String argument) {
		
	}
	
	// allows admins to warn players easily
	public void sendWarn(CommandSender sender, String command, String argument) {
		
	}
	
	// returns a message to the player, when this plugin finished reloading
	public void sendReload(CommandSender sender, String command, String argument) {
		
	}
	
	// informs the player about not being allowed to execute that command
	public void sendDeny(CommandSender sender, String command, String argument) {
		
	}
	
	// unknown command
	public void sendUnknown(CommandSender sender, String command, String argument) {
		
	}
	
	/*
	private String commandInfoName(String argument) {
		if (argument == "help") return Suitcase.msInfo.help;
		else if (argument == "info") return Suitcase.msInfo.info;
		else if (argument == "vote") return Suitcase.msInfo.vote;
		else if (argument == "warn") return Suitcase.msInfo.warn;
		else if (argument == "reload") return Suitcase.msInfo.reload;
		else Suitcase.scLogger.sendError(ErrorType.COMMAND_INFO_NOT_FOUND, argument);
		return null;
	}
	*/
}
