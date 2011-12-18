package me.eighth.suitcase.config;

import java.io.File;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseLog.ErrorType;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
// import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SuitcaseMessage {

	// define variables
	private File messagesFile;
	
	
	// set up messages
	public static class messages {
		
		public static class help { // command info in plugin help
			public String header = " --- Suitcase commands ---"; // TODO: Colors and indentation in minecraft
			public String help   = "/sc help command ... >> Show command help."; // TODO: Save as list
			public String info   = "/sc info ........... >> About this plugin.";
			public String rate   = "/sc rate name rating >> Rate a player.";
			public String warn   = "/sc warn name ...... >> Warn a player.";
			public String reload = "/sc reload ......... >> Reload Suitcase.";
			public String blabla = "All arguments are optional!";
		}
		
		public static class info { // TODO: convert to a List<String>
			public String line1  = " --- About Suitcase ---";
			public String line2  = "Version: Leather Suitcase 0.1";
			public String line3  = "Authors: eighth, HavaTequila";
			public String line4  = "Website: gamez-pla.net";
			public String line5  = "Source: github.com/eighth/Suitcase";
		}
		
		public static class vote { // general system messages or commands
			public String line1  = " --- Rate command ---";
			public String line2  = "Usage: /suitcase rate name rating";
			public String line3  = "rate .... >> Shows current score";
			public String line4  = "name .... >> Get rating for a player";
			public String line5  = "rating .. >> Rates a player positive +, negative - or neutral /.";
		} // TODO: Continue for all commands.
		
		// TODO: Add custom event messages!
	}
	
	// get messages file
	public void initConfig() {
		// read file
		messagesFile = new File(Suitcase.plugin.getDataFolder(), "messages.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(messagesFile);
		// TODO: copy code from SuitcaseConfig..
		Suitcase.messagesKeys = conf.getValues(true);
	}
	
	// parses all {variables}
	private String parseMessage(Player player, String message, String...addition) {
		String[] split = message.split("{");
		String var = "";
		String result = split[0];
		for (int i = 1; i < split.length; i++) {
			result += " ";
			var = split[i].split("}", 2)[0].toLowerCase();
			// some generated stuff
			if (var == "time") result += String.valueOf(player.getWorld().getTime() / 1000) + ":" + String.valueOf(player.getWorld().getTime() % 1000 * 6 / 100);
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
		// send header -> send whole list
		sender.sendMessage(parseMessage(p, (String)Suitcase.messagesKeys.get("messages.help.line1"), "/suitcase help"));
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
}
