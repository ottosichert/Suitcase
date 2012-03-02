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
import org.bukkit.entity.Player;

public class SuitcaseMessage {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/** Stores default messages */
	private Map<String, Object> defaults = new HashMap<String, Object>();

	/** Allocates ~/Suitcase/message-xx_XX.yml */
	private FileConfiguration data;
	
	/**
	 * Player interface messages (supports different languages)
	 * @param plugin Instance of Suitcase
	 */
	public SuitcaseMessage(Suitcase plugin) {
		this.plugin = plugin;
		
		// {variable,1,2}
		// variable: variable name, will be replaced, e.g. 'Hello! How are you?'
		// 1 (r and w will scale with amount rating/warnings): color of text, e.g. '&1Hello! How are you?'
		// 2 (not available for all variables): color of special characters, e.g. ! and ?: '&1Hello&2! &1How are you&2?'
		
		// help command
		defaults.put("help.header", " &7----- &2Suitcase {command,2} &7-----");
		defaults.put("help.usage", "&5Usage &7>> {usage,3,b,(\\[[^\\]]*\\])}");
		defaults.put("help.aliases", "&5Aliases &7>> {aliases,3,7,(,)}");
		defaults.put("help.info", "{object,3,b,(\\[[^\\]]*\\])} &7>> {info,6}");
		defaults.put("help.optional", "&6All &b[arguments] &6are optional and there are several aliases.");
		
		// info command
		defaults.put("info.header", " &7----- &2About Suitcase &7-----");
		defaults.put("info.version", "&5Version &7>> {version,6,7,( v|\\.)}");
		defaults.put("info.description", "&5Description &7>> {description,6}");
		defaults.put("info.authors", "&5Authors &7>> {authors,6,7,(, )}");
		defaults.put("info.website", "&5Website &7>> {website,6,7,([\\-\\.])}");
		
		// rate command
		defaults.put("rate.header", " &7----- {player,2,7,(\\')} &2stats &7-----");
		defaults.put("rate.rating", "&5Rating &7>> {rating,r,7,(/)}");
		defaults.put("rate.warnings", "&5Warnings &7>> {warnings,w,7,(/)}");
		
		// top command
		defaults.put("top.header", " &7----- &2Top ratings &7-----");
		defaults.put("top.stats", "&b#{rank,3} {player,b} &7>> &bR {rating,r,7,(/)} &3- &bW {warnings,w,7,(/)}");
		defaults.put("top.empty", "&6No registered players found.");
		
		// other commands
		defaults.put("rate.done", "&2You have successfully rated {player,a}&2.");
		defaults.put("warn.done", "&2You have successfully warned {player,a}&2.");
		defaults.put("forgive.done", "&2You have successfully forgiven {player,a}&2.");
		defaults.put("reload.done", "&2Suitcase reloaded.");
		defaults.put("reset.done", "&2Configuration and ratings reset.");
		defaults.put("reset.confirm", "&2Re-enter this command to reset Suitcase.");
		
		// command errors
		defaults.put("error.command.deny", "&cYou don't have permission to use {command,7}&c!");
		defaults.put("error.command.unknown", "&cCan't find command {command,7}&c! Try &7/suitcase help &cinstead.");
		defaults.put("error.command.console", "{command,7} &ccan't be run by console!");
		defaults.put("error.command.disabled", "{command,7} &cis disabled!");
		defaults.put("error.command.internal", "&cAn error occured while executing {command,7}&c! Disabling plugin...");
		defaults.put("error.argument.missing", "&cMissing argument/s {argument,7}&c!");
		defaults.put("error.argument.invalid", "&cInvalid argument/s {argument,7}&c!");
		defaults.put("error.argument.amount", "&cToo many arguments! Try &7/suitcase help {command,7} &cinstead.");
		defaults.put("error.argument.help", "&cCan't find help for {command,7}&c!");
		defaults.put("error.argument.rating", "&cYour entered rating {rating,7} &cis invalid!");
		defaults.put("error.player.name", "&cCan't find player {player,7}&c!");
		defaults.put("error.player.rate", "&c{player,7} &cdoesn't have a rating!");
		defaults.put("error.player.warn", "&c{player,7} &ccan't be warned or forgiven!");
		defaults.put("error.player.self", "&cYou can't rate yourself!");
		
		// broadcast
		defaults.put("broadcast.rate", "&7* {target,6} &6was rated {rating,r} &6by {sender,6} &7*");
		defaults.put("broadcast.warn", "&7* {target,6} &6was warned! &7*");
		defaults.put("broadcast.forgive", "&7* {target,6} &6was forgiven! &7*");
		defaults.put("broadcast.reload", "&7* &6Suitcase was reloaded. &7*");
		defaults.put("broadcast.reset", "&7* &6Suitcase was reset. &7*");
		
		// join message(s)
		defaults.put("join", "&7* &6Welcome, {player,6}&6! &7*&n&7* &6Rating: {rating,r,7,(/)} &7* &6Warnings: {warnings,w,7,(/)} &7*");
	}
	
	/**
	 * Returns a String
	 * @param key Config.yml key
	 */
	public String getString(String key) {
		return data.getString(key);
	}
	
	/**
	 * Returns an Integer
	 * @param key Config.yml key
	 */
	public int getInt(String key) {
		return data.getInt(key);
	}
	
	/**
	 * Returns a Boolean
	 * @param key Config.yml key
	 */
	public boolean getBoolean(String key) {
		return data.getBoolean(key);
	}
	
	/**
	 * Returns a Double
	 * @param key Config.yml key
	 */
	public double getDouble(String key) {
		return data.getDouble(key);
	}
	
	/**
	 * Sends a message to a player, based on a key, chat colors and variables to be replaced
	 * @param sender Display message to this sender
	 * @param key YAML key of message configuration file
	 * @param arguments Variables and their replacement
	 */
	public void send(CommandSender sender, String key, String...arguments) {
		
		// get the message for the given key
		String message = parse(getString(key), arguments);
		
		// replace \{ with {
		message = message.replaceAll("\\\\\\{", "{");
		
		// send message to player
		String result, hex = "0123456789abcdef";
		char firstChar;
		String[] split;
		
		for (String line : message.split("&n")) {
			if (line.contains("&")) {
				split = line.split("&");
				result = split[0];
				for (int i = 1; i < split.length; i++) {
					if (split[i] != null) {
						firstChar = split[i].toCharArray()[0];
						if (hex.contains(String.valueOf(firstChar))) {
							result += ChatColor.getByChar(firstChar) + split[i].substring(1);
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
	
	/**
	 * Sends messages to all authorized players
	 * @param key Message configuration key
	 * @param arguments Variables and their replacement
	 */
	public void sendAll(String key, String...arguments) {
		// check if broadcast message is enabled
		if (plugin.cfg.getBoolean("broadcast." + key)) {
			// send message to all players
			for (Player player : plugin.getServer().getOnlinePlayers()) {
				if (plugin.hasPermission(player.getName(), "broadcast")) {
					send(player, "broadcast." + key, arguments);
				}
			}
			// send message to console
			if (plugin.cfg.getBoolean("log.console.broadcast")) {
				send(plugin.getServer().getConsoleSender(), "broadcast." + key, arguments);
			}
		}
	}
	
	/**
	 * Parses a String and replaces all variables
	 * @param message Input String
	 * @param arguments Pair of variable and its replacement
	 */
	public String parse(String message, String...arguments) {
		String variable, replacement;
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
							double rating = Double.parseDouble(replacement);
							if (rating < 0) {
								replacement = "&6No rating.";
							}
							else {
								double def = plugin.cfg.getDouble("rating.default");
								double max = plugin.cfg.getDouble("rating.maximum");

								if (rating >= 0 && rating < def * 2 / 5) {
									split[1] =  "4";
								}
								else if (rating >= def * 2 / 5 && rating < def * 4 / 5) {
									split[1] =  "c";
								}
								else if (rating >= def * 4 / 5 && rating <= (max - def) / 5 + def) {
									split[1] =  "e";
								}
								else if (rating > (max - def) / 5 + def && rating <= (max - def) * 3 / 5 + def) {
									split[1] =  "a";
								}
								else if (rating > (max - def) * 3 / 5 + def && rating <= max) {
									split[1] =  "2";
								}
								else {
									split[1] =  "7";
								}
								replacement = "&" + split[1] + replacement + "/&2" + max;
							}
						}
						// set warnings color
						else if (split[1].equals("w")) {
							int warnings = Integer.parseInt(replacement);
							if (warnings < 0) {
								replacement = "&6No warnings.";
							}
							else {
								int max = plugin.cfg.getInt("warnings.maximum");

								if (warnings >= 0 && warnings < max * 1 / 5) {
									split[1] =  "2";
								}
								else if (warnings >=  max / 5 && warnings <  max * 2 / 5) {
									split[1] =  "a";
								}
								else if (warnings >=  max * 2 / 5 && warnings <=  max * 3 / 5) {
									split[1] =  "e";
								}
								else if (warnings >  max * 3 / 5 && warnings <=  max * 4 / 5) {
									split[1] =  "c";
								}
								else if (warnings >  max * 4 / 5 && warnings <=  max) {
									split[1] =  "4";
								}
								else {
									split[1] =  "7";
								}
								replacement = "&" + split[1] + replacement + "/&4" + max;
							}
						}
						// set replacement
						else {
							replacement = "&" + split[1] + replacement;
						}
						
						if (split.length == 2) {
							message = message.replaceAll("(?<!\\\\)\\{" + variable + ",[0-9a-frw]\\}", replacement);
						}
						// split.length == 4
						else {
							message = message.replaceAll("(?<!\\\\)\\{" + variable + ",[0-9a-frw],[0-9a-f],\\([^\\)]*\\)\\}", replacement.replaceAll(split[3], "&" + split[2] + "$1&" + split[1]));
						}
						break;
					}
				}
			}
		}
		
		return message;
	}
	
	/** Resets message configuration */
	public boolean reset() {
		File dataFile = new File("plugins/Suitcase/message-" + plugin.cfg.getString("mechanics.locale") + ".yml");
		dataFile.delete();
		if (plugin.load("plugins/Suitcase/message-" + plugin.cfg.getString("mechanics.locale") + ".yml", defaults, true)) {
			data = YamlConfiguration.loadConfiguration(dataFile);
			return true;
		}
		else {
			return false;
		}
	}
	
	/** Gets and reads message configuration */
	public boolean init() {
		if (plugin.load("plugins/Suitcase/message-" + plugin.cfg.getString("mechanics.locale") + ".yml", defaults, false)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/message-" + plugin.cfg.getString("mechanics.locale") + ".yml"));
			return true;
		}
		else {
			plugin.log(Action.INIT_ERROR, "SuitcaseMessage");
			return false;
		}
	}
	
	/** Disposes message configuration */
	public boolean free() {
		if (plugin.load("plugins/Suitcase/message-" + plugin.cfg.getString("mechanics.locale") + ".yml", data)) {
			data = null;
			return true;
		}
		else {
			plugin.log(Action.INIT_ERROR, "SuitcaseMessage");
			return false;
		}
	}
	
	/** Reloads message configuration */
	public boolean reload() {
		if (free() && init()) {
			return true;
		}
		else {
			return false;
		}
	}
}
