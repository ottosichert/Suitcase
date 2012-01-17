package me.eighth.suitcase.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.eighth.suitcase.Suitcase;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SuitcaseMessages {
	
	private Suitcase plugin;
	private Map<String, Object> defaults = new HashMap<String, Object>();
	public FileConfiguration data;
	
	public SuitcaseMessages(Suitcase plugin) {
		this.plugin = plugin;
		
		// help command
		defaults.put("help.header", " &7----- &2Suitcase {command,&2}&7-----");
		defaults.put("help.usage", "&5Usage &7>> {usage,&3,&b}");
		defaults.put("help.aliases", "&5Aliases &7>> {aliases,&3,&7}");
		defaults.put("help.info", "{object,&3} &7>> {info,&6}");
		defaults.put("help.optional", "&6All arguments are optional and there are several aliases.");
		
		// info command
		defaults.put("info.header", " &7----- &2About Suitcase &7-----");
		defaults.put("info.version", "&5Version &7>> {version,&6,&7}");
		defaults.put("info.description", "&5Description &7>> {description,&6,&7}");
		defaults.put("info.authors", "&5Authors &7>> {authors,&6,&7}");
		defaults.put("info.website", "&5Website &7>> {website,&6,&7}");
		
		// rate command
		defaults.put("rate.header", " &7----- {player,&2,&7} &2rating &7-----");
		defaults.put("rate.rating", "&5Rating &7>> {rating,&6,&7}");
		defaults.put("rate.warnings", "&5Warnings &7>> {warnings,&6,&7}");
		
		// basic commands
		defaults.put("rate.done", "&2You have successfully rated {player,&a}&2.");
		defaults.put("warn.done", "&2You have successfully warned {player,&a}&2.");
		defaults.put("forgive.done", "&2You have successfully forgiven {player,&a}&2.");
		defaults.put("reload.done", "&2Suitcase reloaded.");
		
		// command errors
		defaults.put("error.command.deny", "&4You don't have permission to use {command,&7}&4!");
		defaults.put("error.command.unknown", "&4Can't find command {command,&7}&4! Try {help,&7} &4instead.");
		defaults.put("error.command.console", "{command,&7} &4can't be run by console!");
		defaults.put("error.command.disabled", "{command,&7} &4is disabled!");
		defaults.put("error.argument.count", "&4Invalid amount of arguments!");
		defaults.put("error.argument.invalid", "&4Invalid argument {argument,&7}&4!");
		defaults.put("error.argument.help", "&4Can't find help for {command,&7}&4!");
		defaults.put("error.argument.rating", "&4Your entered rating {rating,&7} &4is invalid!");
		defaults.put("error.player.name", "&4Can't find player {player,&7}&4!");
		defaults.put("error.player.immune", "&4Player {player,&7} &4can't be rated or warned!");
		defaults.put("error.player.unrated", "&4You haven't been rated by {player,&7} &4yet!");
	}
	
	public String parse(String message, String variable, String replacement) {
		return parse(message, variable, replacement, "");
	}
	
	// parse variable
	public String parse(String message, String variable, String replacement, String colored) {
		// cut off everything around outer brackets and remove spaces inside
		String variables = message.replaceAll("^[^\\{]*\\{|\\}[^\\}]*$", "");
		String edited = "";
		for (String var : variables.split("\\}.*\\{")) {
			String[] split = var.split(",");
			if (split[0].equals(variable)) {
				
				if (split.length > 1) {
					edited = split[1];
				}
				else {
					edited = "&f";
				}
				if (split.length > 2 && colored != "") {
					edited += replacement.replaceAll("([" + colored + "])", split[2] + "$1" + split[1]) + "&f";
				}
				else {
					edited += replacement;
				}
			}
		}
		return message.replaceAll("\\{" + variable + "(,&[0-9a-fA-F])*\\}", edited);
	}
	
	// TODO: use mechanics.language
	// get messages file
	public boolean init() {
		if (plugin.file.load("messages.yml", defaults)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/messages.yml"));
			return true;
		}
		else {
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
	
	// sends help with header and available commands
	public void sendMessage(CommandSender sender, ArrayList<String> lines) {
		// parse colors and send message line by line
		for (String line : lines) {
			for (String split : line.split("\\n")) {
				sender.sendMessage(color(split));
			}
		}
	}

	// get string from ArrayList and remove brackets
	public String getString(ArrayList<String> list, boolean commas) {
		if (commas) {
			return list.toString().replaceAll("^\\[|\\]$", "");
		}
		else {
			return list.toString().replaceAll("^\\[|\\]$|, ", "");
		}
	}
	
	// returns colored message
	// TODO: Add to parse().
	private String color(String message) {
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
}
