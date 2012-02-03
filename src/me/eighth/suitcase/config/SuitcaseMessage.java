package me.eighth.suitcase.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseConsole.Action;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SuitcaseMessage {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/** Stores default messages */
	private Map<String, Object> defaults = new HashMap<String, Object>();

	/** Allocate ~/Suitcase/message-xx_XX.yml */
	public FileConfiguration data;
	
	/** Player interface messages (supports different languages) */
	public SuitcaseMessage(Suitcase plugin) {
		this.plugin = plugin;
		
		// {variable,1,2}
		// variable: variable name, will be replaced, e.g. 'Hello! How are you?'
		// 1 (r and w will scale with amount rating/warnings): color of text, e.g. '&1Hello! How are you?'
		// 2 (not available for all variables): color of special characters, e.g. ! and ?: '&1Hello&2! &1How are you&2?'
		
		// help command
		defaults.put("help.header", " &7----- &2Suitcase {command,2} &7-----");
		defaults.put("help.usage", "&5Usage &7>> {usage,3}");
		defaults.put("help.aliases", "&5Aliases &7>> {aliases,3,7,(,)}");
		defaults.put("help.info", "{object,3} &7>> {info,6}");
		defaults.put("help.optional", "&6All arguments are optional and there are several aliases.");
		
		// info command
		defaults.put("info.header", " &7----- &2About Suitcase &7-----");
		defaults.put("info.version", "&5Version &7>> {version,6,7,( v|\\.)}");
		defaults.put("info.description", "&5Description &7>> {description,6}");
		defaults.put("info.authors", "&5Authors &7>> {authors,6,7,(, )}");
		defaults.put("info.website", "&5Website &7>> {website,6,7,([\\-\\.])}");
		
		// rate command
		defaults.put("rate.header", " &7----- {player,2,7,(\\')} &2stats &7-----");
		defaults.put("rate.rating", "&5Rating &7>> {rating,r}&7/{maxrate,2}");
		defaults.put("rate.warnings", "&5Warnings &7>> {warnings,w}&7/{maxwarn,4}");
		defaults.put("rate.no-rating", "&5Rating &7>> &6No rating.");
		defaults.put("rate.no-warnings", "&5Warnings &7>> &6No warnings.");
		
		// basic commands
		defaults.put("rate.done", "&2You have successfully rated {player,a}&2.");
		defaults.put("warn.done", "&2You have successfully warned {player,a}&2.");
		defaults.put("forgive.done", "&2You have successfully forgiven {player,a}&2.");
		defaults.put("reload.done", "&2Suitcase reloaded.");
		defaults.put("reset.done", "&2Configuration and ratings reset.");
		defaults.put("reset.confirm", "&2Re-enter this command to reset Suitcase.");
		
		// command errors
		defaults.put("error.command.deny", "&4You don't have permission to use {command,7}&4!");
		defaults.put("error.command.unknown", "&4Can't find command {command,7}&4! Try {help,7} &4instead.");
		defaults.put("error.command.console", "{command,7} &4can't be run by console!");
		defaults.put("error.command.disabled", "{command,7} &4is disabled!");
		defaults.put("error.argument.count", "&4Invalid amount of arguments!");
		defaults.put("error.argument.invalid", "&4Invalid argument {argument,7}&4!");
		defaults.put("error.argument.help", "&4Can't find help for {command,7}&4!");
		defaults.put("error.argument.rating", "&4Your entered rating {rating,7} &4is invalid!");
		defaults.put("error.player.name", "&4Can't find player {player,7}&4!");
		defaults.put("error.player.rate", "&4{player,7} &4doesn't have a rating!");
		defaults.put("error.player.warn", "&4{player,7} &4can't be warned!");
		defaults.put("error.player.self", "&4You can't rate yourself!");
		
		// broadcast
		defaults.put("broadcast.warn", "&7* {player,6} &6was warned! &7*");
		defaults.put("broadcast.forgive", "&7* {player,6} &6was forgiven! &7*");
		defaults.put("broadcast.reset", "&7* &6Suitcase has been reset. &7*");
		
		// join message(s)
		defaults.put("join", "&7* &6Welcome, {player,6}&6! &7*&n&7* &6Rating: {rating,r} &7* &6Warnings: {warnings,w} &7*");
	}
	
	/** Sends a message to a player, based on a key, chat colors and variables to be replaced */
	public void send(CommandSender sender, String key, String...arguments) {
		
		// get the message for the given key
		plugin.debug(key);
		String message = data.getString(key);
		
		String variable, replacement, result;
		char firstChar;
		String[] split;
		
		// group arguments to pairs, the name of a variable and its replacement
		for (int i = 2; i <= arguments.length; i += 2) {
			
			// get variable and its replacement
			variable = arguments[i - 2];
			replacement = arguments[i - 1];
			
			// get each possible variable and replace it
			for (String value : message.replaceAll("^[^\\{]*(?<!\\\\)\\{|(?<!\\\\)\\}[^\\}]*$", "").split("(?<!\\\\)\\}.*(?<!\\\\)\\{")) {
				
				// separate variable name and colors
				split = value.split(",", 4);
				
				// check if variable is valid
				if (split[0].equals(variable)) {
					
					// set colors and replace variable
					switch (split.length) {

					// no colors
					case 1:
						message = message.replaceAll("(?<!\\\\)\\{" + variable + "\\}", replacement);
						break;
					
					// invalid amount of arguments
					case 3:
						break;
						
					// two or four (maximum)
					default:
						// set rating color
						if (split[1].equals("r")) {
							Double rating = Double.parseDouble(replacement);
							if (rating >= 0 && rating < plugin.cfg.data.getDouble("mechanics.rating.default") * 2 / 5) {
								split[1] =  "4";
							}
							else if (rating >= plugin.cfg.data.getDouble("mechanics.rating.default") * 2 / 5 && rating < plugin.cfg.data.getDouble("mechanics.rating.default") * 4 / 5) {
								split[1] =  "c";
							}
							else if (rating >= plugin.cfg.data.getDouble("mechanics.rating.default") * 4 / 5 && rating <= (plugin.cfg.data.getDouble("mechanics.rating.maximum") - plugin.cfg.data.getDouble("mechanics.rating.default")) / 5 + plugin.cfg.data.getDouble("mechanics.rating.default")) {
								split[1] =  "e";
							}
							else if (rating > (plugin.cfg.data.getDouble("mechanics.rating.maximum") - plugin.cfg.data.getDouble("mechanics.rating.default")) / 5 + plugin.cfg.data.getDouble("mechanics.rating.default") && rating <= (plugin.cfg.data.getDouble("mechanics.rating.maximum") - plugin.cfg.data.getDouble("mechanics.rating.default")) * 3 / 5 + plugin.cfg.data.getDouble("mechanics.rating.default")) {
								split[1] =  "a";
							}
							else if (rating > (plugin.cfg.data.getDouble("mechanics.rating.maximum") - plugin.cfg.data.getDouble("mechanics.rating.default")) * 3 / 5 + plugin.cfg.data.getDouble("mechanics.rating.default") && rating <= plugin.cfg.data.getDouble("mechanics.rating.maximum")) {
								split[1] =  "2";
							}
							else {
								split[1] =  "7";
							}
						}
						
						// set warnings color
						else if (split[1].equals("w")) {
							int warnings = Integer.parseInt(replacement);
							if (warnings >= 0 && warnings < plugin.cfg.data.getInt("mechanics.warnings.maximum") * 1 / 5) {
								split[1] =  "2";
							}
							else if (warnings >=  plugin.cfg.data.getInt("mechanics.warnings.maximum") * 1 / 5 && warnings <  plugin.cfg.data.getInt("mechanics.warnings.maximum") * 2 / 5) {
								split[1] =  "a";
							}
							else if (warnings >=  plugin.cfg.data.getInt("mechanics.warnings.maximum") * 2 / 5 && warnings <=  plugin.cfg.data.getInt("mechanics.warnings.maximum") * 3 / 5) {
								split[1] =  "e";
							}
							else if (warnings >  plugin.cfg.data.getInt("mechanics.warnings.maximum") * 3 / 5 && warnings <=  plugin.cfg.data.getInt("mechanics.warnings.maximum") * 4 / 5) {
								split[1] =  "c";
							}
							else if (warnings >  plugin.cfg.data.getInt("mechanics.warnings.maximum") * 4 / 5 && warnings <=  plugin.cfg.data.getInt("mechanics.warnings.maximum")) {
								split[1] =  "4";
							}
							else {
								split[1] =  "7";
							}
						}
						
						// replace variable
						replacement = "&" + split[1] + replacement;
						if (split.length == 2) {
							message = message.replaceAll("(?<!\\\\)\\{" + variable + ",[0-9a-frw]\\}", replacement);
						}
						
						// split.length == 4
						else {
							message = message.replaceAll("(?<!\\\\)\\{" + variable + ",[0-9a-frw],[0-9a-f],\\(.*\\)}", replacement.replaceAll(split[3], "&" + split[2] + "$1&" + split[1]));
						}
						break;
					}
				}
			}
		}
		
		// replace \{ with {
		message = message.replaceAll("\\\\\\{", "{");
		
		// send message to player
		String hex = "0123456789abcdef";
		for (String line : message.split("&n")) {
			if (line.contains("&")) {
				split = line.split("&");
				result = split[0];
				for (int i = 1; i < split.length; i++) {
					if (split[i] != null) {
						firstChar = split[i].toCharArray()[0];
						if (hex.contains(String.valueOf(firstChar))) {
							result += ChatColor.getByCode(hex.indexOf(firstChar)) + split[i].substring(1);
						}
						else {
							result += "&" + split[i];
						}
					}
					else {
						result += "&";
					}
				}
			}
			else {
				result = line;
			}
			sender.sendMessage(result);
		}
	}
	
	public void reset() {
		File dataFile = new File("plugins/Suitcase/message-" + plugin.cfg.data.getString("mechanics.locale") + ".yml");
		dataFile.delete();
		if (plugin.file.load("plugins/Suitcase/message-" + plugin.cfg.data.getString("mechanics.locale") + ".yml", defaults, true)) {
			data = YamlConfiguration.loadConfiguration(dataFile);
		}
		else {
			plugin.console.log(Action.FILE_SAVE_ERROR, dataFile.getName(), "FileNotLoaded");
		}
	}

	// get message file
	public boolean init() {
		if (plugin.file.load("plugins/Suitcase/message-" + plugin.cfg.data.getString("mechanics.locale") + ".yml", defaults, false)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/message-" + plugin.cfg.data.getString("mechanics.locale") + ".yml"));
			return true;
		}
		else {
			plugin.console.log(Action.INIT_ERROR, "SuitcaseMessage", "FileNotLoaded");
			return false;
		}
	}

	public boolean free() {
		data = null;
		return true;
	}

	public boolean reload() {
		if (free() && init()) {
			return true;
		}
		else {
			return false;
		}
	}
}
