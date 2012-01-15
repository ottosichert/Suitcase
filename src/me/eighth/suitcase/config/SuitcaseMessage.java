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

public class SuitcaseMessage {
	
	private Suitcase plugin;
	private Map<String, Object> messagesDefault = new HashMap<String, Object>();
	
	public FileConfiguration config;
	
	public SuitcaseMessage(Suitcase plugin) {
		this.plugin = plugin;
		
		// load default values
		// help command
		// help page
		messagesDefault.put("help.header", " &7----- &2Suitcase help &7-----");
		messagesDefault.put("help.info.help", "&3/sc help command &7>> &6Show command help.");
		messagesDefault.put("help.info.info", "&3/sc info &7>> &6About Suitcase.");
		messagesDefault.put("help.info.rate", "&3/sc rate name rating &7>> &6Rate or view rating.");
		messagesDefault.put("help.info.warn", "&3/sc warn name &7>> &6Warn a player.");
		messagesDefault.put("help.info.forgive", "&3/sc forgive name &7>> &6Forgive a player.");
		messagesDefault.put("help.info.reload", "&3/sc reload &7>> &6Reload this plugin.");
		messagesDefault.put("help.optional", "&6All &3arguments &6are optional and there are several &5aliases&6.");
		// help-command help
		messagesDefault.put("help.command.help.header", " &7----- &2Help command &7-----");
		messagesDefault.put("help.command.help.usage", "&5Usage &7>> &3/suitcase help");
		messagesDefault.put("help.command.help.aliases", "&5Aliases &7>> &3h&7, &3?");
		messagesDefault.put("help.command.help.argument.help", "&3help &7>> &6Show a list of all accessible commands.");
		// info help
		messagesDefault.put("help.command.info.header", " &7----- &2Info command &7-----");
		messagesDefault.put("help.command.info.usage", "&5Usage &7>> &3/suitcase info");
		messagesDefault.put("help.command.info.aliases", "&5Aliases &7>> &3i&7, &3about&7, &3a]");
		messagesDefault.put("help.command.info.argument.info", "&3info &7>> &6Show plugin authors and current version.");
		// rate help
		messagesDefault.put("help.command.rate.header", " &7----- &2Rate command &7-----");
		messagesDefault.put("help.command.rate.usage", "&5Usage &7>> &3/suitcase rate name rating");
		messagesDefault.put("help.command.rate.aliases", "&5Aliases &7>> &3r&7, &3vote&7, &3v");
		messagesDefault.put("help.command.rate.argument.rate", "&3rate &7>> &6Rate players or view own rating if no name is given.");
		messagesDefault.put("help.command.rate.argument.name", "&3name &7>> &6Select player.");
		messagesDefault.put("help.command.rate.argument.rating", "&3rating &7>> &6Give a good &2+ or bad &4- &6rating.");
		// warn help
		messagesDefault.put("help.command.warn.header", " &7----- &2Warn command &7-----");
		messagesDefault.put("help.command.warn.usage", "&5Usage &7>> &3/suitcase warn name");
		messagesDefault.put("help.command.warn.aliases", "&5Aliases &7>> &3w&7, &3!");
		messagesDefault.put("help.command.warn.argument.warn", "&3warn &7>> &6Warn a player and increase his warning counter by one.");
		messagesDefault.put("help.command.warn.argument.name", "&3name &7>> &6Select a player.");
		// forgive help
		messagesDefault.put("help.command.forgive.header", " &7----- &2Forgive command &7-----");
		messagesDefault.put("help.command.forgive.usage", "&5Usage &7>> &3/suitcase forgive name");
		messagesDefault.put("help.command.forgive.aliases", "&5Aliases &7>> &3f");
		messagesDefault.put("help.command.forgive.argument.forgive", "&3forgive &7>> &6Forgive a player and reset his warning counter.");
		messagesDefault.put("help.command.forgive.argument.name", "&3name &7>> &6Select a player.");
		// reload help
		messagesDefault.put("help.command.reload.header", " &7----- &2Reload command &7-----");
		messagesDefault.put("help.command.reload.usage", "&5Usage &7>> &3/suitcase reload");
		messagesDefault.put("help.command.reload.aliases", "&5Aliases &7>> none");
		messagesDefault.put("help.command.reload.argument.reload", "&3reload &7>> &6Reload all configuration files and database connections.");
		
		// info command
		// plugin info
		messagesDefault.put("info.header", " &7----- &2About Suitcase &7-----");
		messagesDefault.put("info.version", "&5Current version &7>> &6{version}");
		messagesDefault.put("info.description", "&5Description &7>> &6{description}");
		messagesDefault.put("info.authors", "&5Authors &7>> &6{authors}");
		messagesDefault.put("info.website", "&5Website &7>> &6{website}");
		
		// rate command
		// one's own rating
		messagesDefault.put("rate.view.self.header", " &7----- &2Your rating &7-----");
		messagesDefault.put("rate.view.self.rating", "&5Rating &7>> &6{rating}");
		messagesDefault.put("rate.view.self.warnings", "&5Warnings &7>> &6{warnings}");
		// rating of others
		messagesDefault.put("rate.view.others.header", " &7----- &2{player}'s rating &7-----");
		messagesDefault.put("rate.view.others.rating", "&5Rating &7>> &6{rating}");
		messagesDefault.put("rate.view.others.warnings", "&5Warnings &7>> &6{warnings}");
		// rate players
		messagesDefault.put("rate.done", "&2You have successfully rated &a{player}&2!");
		
		// warn command
		messagesDefault.put("warn.done", "&2You have successfully warned &a{player}&2!");
		
		// forgive command
		messagesDefault.put("forgive.done", "&2You have successfully forgiven &a{player}&2!");
		
		// reload command
		messagesDefault.put("reload.done", "&2Suitcase reloaded.");
		
		// system messages
		// command errors
		messagesDefault.put("system.command.deny", "&4You don't have permission to use &7{command}&4!");
		messagesDefault.put("system.command.unknown", "&4Can't find command &7{command}&4! Try &7/suitcase help&4 instead.");
		messagesDefault.put("system.command.console", "&7{command} &4can't be run by console!");
		messagesDefault.put("system.command.disabled", "&7{command} &4is disabled.");
		messagesDefault.put("system.argument.count", "&4Invalid amount of arguments!");
		messagesDefault.put("system.argument.invalid", "&4Invalid argument &7{argument}&4!");
		messagesDefault.put("system.argument.help", "&4Can't find help for &7{help}&4.");
		messagesDefault.put("system.argument.rating", "&4Your entered rating &7{rating} &4is invalid!");
		messagesDefault.put("system.player.name", "&4Can't find player &7{player}&4!");
		messagesDefault.put("system.player.unrated", "&4You haven't been rated by &7{player} &4yet.");
	}

	// TODO: use mechanics.language
	// get messages file
	public boolean initMessages() {
		if (plugin.file.loadFile("messages.yml", messagesDefault)) {
			config = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/messages.yml"));
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean freeMessages() {
		config = null;
		return true;
	}
	
	public boolean reloadMessages() {
		if (freeMessages() && initMessages()) {
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
			sender.sendMessage(parseColor(line));
		}
	}

	// get string from ArrayList and remove brackets
	public String getString(ArrayList<String> list, boolean commas) {
		if (commas) {
			return list.toString().replaceAll("^\\[|\\]$", "");
		}
		else {
			return list.toString().replaceAll("^\\[|\\]$|,", "");
		}
	}
	
	// returns colored message
	private String parseColor(String message) {
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
