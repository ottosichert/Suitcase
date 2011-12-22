package me.eighth.suitcase.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseConsole.actionType;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SuitcaseMessage {

	// define variables
	private FileConfiguration msConfig;
	private File msFile;
	
	// TODO: use mechanics.language
	// get messages file
	public boolean initMessages() {
		
		// load default values
		loadMessages();
		
		// read file
		msFile = new File(Suitcase.plugin.getDataFolder(), "messages.yml");
		if (msFile.exists()) {
			msConfig = YamlConfiguration.loadConfiguration(msFile);
			// add property if missing
			for (String path : Suitcase.messagesKeys.getKeys(true)) {
				if (!msConfig.contains(path)) {
					msConfig.set(path, Suitcase.messagesKeys.get(path));
				}
				// compare object types
				else if (msConfig.get(path).getClass() != (Suitcase.messagesKeys.get(path).getClass())) {
					msConfig.set(path, Suitcase.messagesKeys.get(path));
				}
			}
			// save and use verified configKeys
			try {
				msConfig.save(msFile);
				Suitcase.messagesKeys = msConfig;
				return true;
			} catch (IOException e) {
				Suitcase.utConsole.sendAction(actionType.FILE_SAVE_ERROR, (ArrayList<String>) Arrays.asList("messages.yml", e.toString()));
				return false;
			}
		}
		else {
			Suitcase.utConsole.sendAction(actionType.FILE_NOT_FOUND, (ArrayList<String>) Arrays.asList("messages.yml"));
			try {
				msFile.createNewFile();
				msConfig = Suitcase.messagesKeys;
				msConfig.save(msFile);
				return true;
			} catch (IOException e) {
				Suitcase.utConsole.sendAction(actionType.FILE_SAVE_ERROR, (ArrayList<String>) Arrays.asList("messages.yml", e.toString()));
				return false;
			}
		}
	}
	
	// TODO: Add new entries here
	private void loadMessages() {
		// help page
		Suitcase.messagesKeys.set("help.self.header", " &7----- &aSuitcase help &7-----");
		Suitcase.messagesKeys.set("help.self.info.help", "&1/sc &9help &3command &7..... &c>> &6Show command help");
		Suitcase.messagesKeys.set("help.self.info.info", "&1/sc &9info &7.......... &c>> &6About Suitcase");
		Suitcase.messagesKeys.set("help.self.info.rate", "&1/sc &9rate &3name &brating &c>> &6Rate or view rating");
		Suitcase.messagesKeys.set("help.self.info.warn", "&1/sc &9warn &3name &7....... &c>> &6Warn a player");
		Suitcase.messagesKeys.set("help.self.info.forgive", "&1/sc &9forgive &3name &7....... &c>> &6Forgive a player");
		Suitcase.messagesKeys.set("help.self.info.reload", "&1/sc &9reload &7......... &c>> &6Reload this plugin");
		Suitcase.messagesKeys.set("help.self.optional", "&6All &3arguments &6are optional and there are several &5aliases&6");
		// help command help
		Suitcase.messagesKeys.set("help.command.help.header", " &7----- &aHelp command &7-----");
		Suitcase.messagesKeys.set("help.command.help.usage", "&5Usage&c: &1/suitcase &9help");
		Suitcase.messagesKeys.set("help.command.help.aliases", "&5Aliases&c: &9help &c[&9h&c, &9?&c]");
		Suitcase.messagesKeys.set("help.command.help.argument.help", "&9help &7.. &c>> &6Show a list of all accessible commands");
		// info help
		Suitcase.messagesKeys.set("help.command.info.header", " &7----- &aInfo command &7-----");
		Suitcase.messagesKeys.set("help.command.info.usage", "&5Usage&c: &1/suitcase &9info");
		Suitcase.messagesKeys.set("help.command.info.aliases", "&5Aliases&c: &9info &c[&9i&c, &9about&c, &9a&c]");
		Suitcase.messagesKeys.set("help.command.info.argument.info", "&9info &7.. &c>> &6Show plugin authors and current version");
		// rate help
		Suitcase.messagesKeys.set("help.command.rate.header", " &7----- &aRate command &7-----");
		Suitcase.messagesKeys.set("help.command.rate.usage", "&5Usage&c: &1/suitcase &9rate &3name &brating");
		Suitcase.messagesKeys.set("help.command.rate.aliases", "&5Aliases&c: &9rate &c[&9r&c, &9vote&c, &9v&c]");
		Suitcase.messagesKeys.set("help.command.rate.argument.rate", "&9rate &7..... &c>> &6Rate players or view own rating if no name is given");
		Suitcase.messagesKeys.set("help.command.rate.argument.name", "&3name &7..... &c>> &6Select player");
		Suitcase.messagesKeys.set("help.command.rate.argument.rating", "&brating &7.. &c>> &6Give a good &2+ or bad &4- &6rating");
		// warn help
		Suitcase.messagesKeys.set("help.command.warn.header", " &7----- &aWarn command &7-----");
		Suitcase.messagesKeys.set("help.command.warn.usage", "&5Usage&c: &1/suitcase &9warn &3name");
		Suitcase.messagesKeys.set("help.command.warn.aliases", "&5Aliases&c: &9warn &c[&9w&c, &9!&c]");
		Suitcase.messagesKeys.set("help.command.warn.argument.warn", "&9warn &7..... &c>> &6Warn a player and increase his warning counter");
		Suitcase.messagesKeys.set("help.command.warn.argument.name", "&3name &7..... &c>> &6Select a player");
		// forgive help
		Suitcase.messagesKeys.set("help.command.forgive.header", " &7----- &aForgive command &7-----");
		Suitcase.messagesKeys.set("help.command.forgive.usage", "&5Usage&c: &1/suitcase &9forgive &3name");
		Suitcase.messagesKeys.set("help.command.forgive.aliases", "&5Aliases&c: &9forgive &c[&9f&c]");
		Suitcase.messagesKeys.set("help.command.forgive.argument.forgive", "&9forgive &7.. &c>> &6Forgive a player and reset his warning counter");
		Suitcase.messagesKeys.set("help.command.forgive.argument.name", "&3name &7...... &c>> &6Select a player");
		// reload help
		Suitcase.messagesKeys.set("help.command.reload.header", " &7----- &aReload command &7-----");
		Suitcase.messagesKeys.set("help.command.reload.usage", "&5Usage&c: &1/suitcase &9reload");
		Suitcase.messagesKeys.set("help.command.reload.aliases", "&5Aliases&c: &9none");
		Suitcase.messagesKeys.set("help.command.reload.argument.reload", "&9reload &7.. &c>> &6Reload all configuration files and database connections");
		// plugin info
		Suitcase.messagesKeys.set("info.self.header", " &7----- &aAbout Suitcase &7-----");
		Suitcase.messagesKeys.set("info.self.version", "&5Current version&4: &6"); // info command adds version here
		Suitcase.messagesKeys.set("info.self.description", "&5Description&4: &6Suitcase is a rating and warning system with website integration.");
		Suitcase.messagesKeys.set("info.self.authors", "&5Authors&4: &6eighth&4, &6HavaTequila");
		Suitcase.messagesKeys.set("info.self.website", "&5Website&4: &6gamez&4-&6pla&4.&6net");
		Suitcase.messagesKeys.set("info.self.source", "&5Source&4: &6github&4.&6com&4/&6eighth&4/&6Suitcase");
		// system messages
		Suitcase.messagesKeys.set("system.command.deny", "&4You don't have permission for that command!");
		Suitcase.messagesKeys.set("system.command.unknown", "&4Can't find that command! Try /suitcase help");
		Suitcase.messagesKeys.set("system.command.invalid", "&4Invalid command or argument!");
		Suitcase.messagesKeys.set("system.command.console", "&4This command can't be used by console!");
		Suitcase.messagesKeys.set("system.command.player", "&4Player not found!");
		Suitcase.messagesKeys.set("system.rate.disabled", "&4Rating has been disabled.");
		Suitcase.messagesKeys.set("system.rate.unrated", "&4You have to rate this player first to view his rating!");
		Suitcase.messagesKeys.set("system.error.log", "&4Can't fetch data from file or database!");
		// TODO: custom events
		/*
		Suitcase.messagesKeys.set("event.", "");
		*/
	}
	
	// sends help with header and available commands
	public void sendMessage(CommandSender sender, ArrayList<String> lines) {
		// parse colors and send message line by line
		for (String line : lines) {
			sender.sendMessage(parseColor(line));
		}
	}

	// get string from ArrayList and remove brackets
	public String getString(ArrayList<String> list) {
		return list.toString().replaceAll("^\\[|\\]$", "");
	}
	
	// returns colored message
	private String parseColor(String message) {
		String hex = "0123456789abcdef";
		String[] split = message.split("&[^&]");
		String result = split[0];
		String firstChar = "";
		for (int i = 1; i < split.length; i++) {
			firstChar = split[i].substring(0, 1).toLowerCase();
			if (hex.contains(firstChar)) {
				result += ChatColor.getByCode(hex.indexOf(firstChar)) + split[i].substring(1);
			}
			else {
				result += "&" + split[i];
			}
		}
		return result;
	}
}
