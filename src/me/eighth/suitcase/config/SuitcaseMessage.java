package me.eighth.suitcase.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.log.SuitcaseConsole.Action;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SuitcaseMessage {
	
	private Suitcase plugin;
	private Map<String, Object> defaults = new HashMap<String, Object>();
	public FileConfiguration data;
	
	public SuitcaseMessage(Suitcase plugin) {
		this.plugin = plugin;
		
		// variable usage: {var,1,2}
		// var: variable name, will be replaced, e.g. 'Hello! How are you?'
		// 1 (r and w will scale with rating/warnings): color of text, e.g. '&1Hello! &1How are you?'
		// 2 (only available for some variables): color of special characters, e.g. ! and ?: '&1Hello&2! &1How are you&2?'
		
		// help command
		defaults.put("help.header", " &7----- &2Suitcase {command,2} &7-----");
		defaults.put("help.usage", "&5Usage &7>> {usage,3,b}");
		defaults.put("help.aliases", "&5Aliases &7>> {aliases,3,7}");
		defaults.put("help.info", "{object,3} &7>> {info,6}");
		defaults.put("help.optional", "&6All arguments are optional and there are several aliases.");
		
		// info command
		defaults.put("info.header", " &7----- &2About Suitcase &7-----");
		defaults.put("info.version", "&5Version &7>> {version,6,7}");
		defaults.put("info.description", "&5Description &7>> {description,6}");
		defaults.put("info.authors", "&5Authors &7>> {authors,6,7}");
		defaults.put("info.website", "&5Website &7>> {website,6,7}");
		
		// rate command
		defaults.put("rate.header", " &7----- {player,2,7} &2rating &7-----");
		defaults.put("rate.rating", "&5Rating &7>> {rating,r}&7/{maxrate,2}");
		defaults.put("rate.warnings", "&5Warnings &7>> {warnings,w}&7/{maxwarn,4}");
		
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
		defaults.put("error.player.rate", "&4{player,7} &4doesn't have a rating!&4!");
		defaults.put("error.player.warn", "&4{player,7} &4can't be warned!");
		defaults.put("error.player.self", "&4You can't rate yourself!");
		
		// broadcast
		defaults.put("broadcast.warn", "&7* {player,6} &6was warned! &7*");
		defaults.put("broadcast.forgive", "&7* {player,6} &6was forgiven! &7*");
		defaults.put("broadcast.reset", "&7* &6Suitcase has been reset. &7*");
		
		// join message(s)
		defaults.put("join", "&7* &6Welcome, {player,6}&6! &7*\n&7* &6Rating: {rating,r} &7* &6Warnings: {warnings,w} &7*");
	}
	
	public String parse(String message, String...arguments) {
		for (int i = 1; i < arguments.length; i += 3) {
			if (i + 1 == arguments.length) {
				message = parse(message, arguments[i - 1], arguments[i]);
			}
			else {
				message = parse(message, arguments[i - 1], arguments[i], arguments[i + 1]);
			}
		}
		return message;
	}
	
	private String parse(String message, String variable, String replacement) {
		return parse(message, variable, replacement, "");
	}
	
	// parse variable
	private String parse(String message, String variable, String replacement, String regex) {
		// cut off everything around outer brackets and remove spaces inside
		String variables = message.replaceAll("^[^\\{]*\\{|\\}[^\\}]*$", "");
		String edited = "";
		for (String var : variables.split("\\}.*\\{")) {
			String[] split = var.split(",");
			if (split[0].equals(variable)) {
				// set colors
				if (split.length > 1) {
					if (split[1].equals("r")) {
						split[1] = ratingColor(Double.parseDouble(replacement));
					}
					else if (split[1].equals("w")) {
						split[1] = warningsColor(Integer.parseInt(replacement));
					}
					edited = "&" + split[1];
				}
				else {
					edited = "&f";
				}
				if (split.length > 2 && regex != "") {
					edited += replacement.replaceAll("(" + regex + ")", "&" + split[2] + "$1&" + split[1]) + "&f";
				}
				else {
					edited += replacement;
				}
			}
		}
		return message.replaceAll("\\{" + variable + "(,[0-9a-frw])*\\}", edited);
	}

	public void reset() {
		File dataFile = new File("plugins/Suitcase/message-" + plugin.cfg.data.getString("mechanics.locale") + ".yml");
		dataFile.delete();
		if (plugin.file.load(dataFile.getPath(), defaults, true)) {
			data = YamlConfiguration.loadConfiguration(dataFile);
		}
		else {
			plugin.con.log(Action.FILE_SAVE_ERROR, dataFile.getName(), "FileNotLoaded");
		}
	}
	
	// TODO: use mechanics.locale
	// get message file
	public boolean init() {
		if (plugin.file.load("plugins/Suitcase/message-" + plugin.cfg.data.getString("mechanics.locale") + ".yml", defaults, false)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/message-" + plugin.cfg.data.getString("mechanics.locale") + ".yml"));
			return true;
		}
		else {
			plugin.con.log(Action.INIT_ERROR, "SuitcaseMessage", "FileNotLoaded");
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
	
	public void sendMessage(CommandSender sender, String...lines) {
		sendMessage(sender, new ArrayList<String>(Arrays.asList(lines)));
	}
	
	// send split and colored message
	public void sendMessage(CommandSender sender, ArrayList<String> lines) {
		// parse colors and send message line by line
		for (String line : lines) {
			for (String split : line.split("\\n")) {
				sender.sendMessage(messageColor(split));
			}
		}
	}

	// get string from ArrayList and remove brackets
	public String getString(ArrayList<String> list, boolean commas) {
		if (commas) {
			return list.toString().replaceAll("^\\[|\\]$", "");
		}
		else {
			return list.toString().replaceAll("^\\[|\\]$|, ", " ").trim();
		}
	}
	
	// return colored message
	private String messageColor(String message) {
		String hex = "0123456789abcdef";
		if (!message.contains("&")) {
			return message;
		}
		String[] split = message.split("&");
		String result = split[0];
		String firstChar = "";
		for (int i = 1; i < split.length; i++) {
			if (split[i] != null) {
				firstChar = split[i].substring(0, 1).toLowerCase();
				if (hex.contains(firstChar)) {
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
		return result;
	}
	
	// return color char of ratings
	private String ratingColor(double rating) {
		if (rating >= 0 && rating < plugin.cfg.data.getInt("mechanics.rating.default") * 2 / 5) {
			return "4";
		}
		else if (rating >= plugin.cfg.data.getInt("mechanics.rating.default") * 2 / 5 && rating < plugin.cfg.data.getInt("mechanics.rating.default") * 4 / 5) {
			return "c";
		}
		else if (rating >= plugin.cfg.data.getInt("mechanics.rating.default") * 4 / 5 && rating <= (plugin.cfg.data.getInt("mechanics.rating.maximum") - plugin.cfg.data.getInt("mechanics.rating.default")) / 5 + plugin.cfg.data.getInt("mechanics.rating.default")) {
			return "e";
		}
		else if (rating > (plugin.cfg.data.getInt("mechanics.rating.maximum") - plugin.cfg.data.getInt("mechanics.rating.default")) / 5 + plugin.cfg.data.getInt("mechanics.rating.default") && rating <= (plugin.cfg.data.getInt("mechanics.rating.maximum") - plugin.cfg.data.getInt("mechanics.rating.default")) * 3 / 5 + plugin.cfg.data.getInt("mechanics.rating.default")) {
			return "a";
		}
		else if (rating > (plugin.cfg.data.getInt("mechanics.rating.maximum") - plugin.cfg.data.getInt("mechanics.rating.default")) * 3 / 5 + plugin.cfg.data.getInt("mechanics.rating.default") && rating <= plugin.cfg.data.getInt("mechanics.rating.maximum")) {
			return "2";
		}
		else {
			return "7";
		}
	}
	
	// return color char of warnings
	private String warningsColor(int warnings) {
		if (warnings >= 0 && warnings < plugin.cfg.data.getInt("mechanics.warnings.maximum") * 1 / 5) {
			return "2";
		}
		else if (warnings >= plugin.cfg.data.getInt("mechanics.warnings.maximum") * 1 / 5 && warnings < plugin.cfg.data.getInt("mechanics.warnings.maximum") * 2 / 5) {
			return "a";
		}
		else if (warnings >= plugin.cfg.data.getInt("mechanics.warnings.maximum") * 2 / 5 && warnings <= plugin.cfg.data.getInt("mechanics.warnings.maximum") * 3 / 5) {
			return "e";
		}
		else if (warnings > plugin.cfg.data.getInt("mechanics.warnings.maximum") * 3 / 5 && warnings <= plugin.cfg.data.getInt("mechanics.warnings.maximum") * 4 / 5) {
			return "c";
		}
		else if (warnings > plugin.cfg.data.getInt("mechanics.warnings.maximum") * 4 / 5 && warnings <= plugin.cfg.data.getInt("mechanics.warnings.maximum")) {
			return "4";
		}
		else {
			return "7";
		}
	}
}
