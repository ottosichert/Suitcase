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
						// set missing property and log to console
						msConfig.set(path, Suitcase.messagesKeys.get(path));
						Suitcase.utConsole.sendAction(actionType.PROPERTY_MISSING, (ArrayList<String>) Arrays.asList(path, "messages.yml", Suitcase.messagesKeys.getString(path)));
					}
					// compare object types
					// TODO: check if this is working
					else if (msConfig.get(path).getClass() != (Suitcase.messagesKeys.get(path).getClass())) {
						// reset value and log to console
						msConfig.set(path, Suitcase.messagesKeys.get(path));
						Suitcase.utConsole.sendAction(actionType.PROPERTY_BAD_TYPE, (ArrayList<String>) Arrays.asList(path, "messages.yml", msConfig.get(path).getClass().toString(), Suitcase.messagesKeys.get(path).getClass().toString()));
					}
				}
				// save and use verified configKeys
				try {
					saveMessages();
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
					saveMessages();
					return true;
				} catch (IOException e) {
					Suitcase.utConsole.sendAction(actionType.FILE_SAVE_ERROR, (ArrayList<String>) Arrays.asList("messages.yml", e.toString()));
					return false;
				}
			}
		}
		
		public boolean freeMessages() {
			msConfig = null;
			msFile = null;
			Suitcase.messagesKeys = null;
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
		
		// save config to file
		private void saveMessages() throws IOException {
			msConfig.save(msFile);
		}
	
	private void loadMessages() {
		// help command
		// help page
		Suitcase.messagesKeys.set("help.header", " &7----- &aSuitcase help &7-----");
		Suitcase.messagesKeys.set("help.info.help", "&1/sc &9help &3command &7>> &6Show command help");
		Suitcase.messagesKeys.set("help.info.info", "&1/sc &9info &7>> &6About Suitcase");
		Suitcase.messagesKeys.set("help.info.rate", "&1/sc &9rate &3name &brating &7>> &6Rate or view rating");
		Suitcase.messagesKeys.set("help.info.warn", "&1/sc &9warn &3name &7>> &6Warn a player");
		Suitcase.messagesKeys.set("help.info.forgive", "&1/sc &9forgive &3name &7>> &6Forgive a player");
		Suitcase.messagesKeys.set("help.info.reload", "&1/sc &9reload &7>> &6Reload this plugin");
		Suitcase.messagesKeys.set("help.optional", "&6All &3arguments &6are optional and there are several &5aliases&6");
		// help command help
		Suitcase.messagesKeys.set("help.command.help.header", " &7----- &aHelp command &7-----");
		Suitcase.messagesKeys.set("help.command.help.usage", "&5Usage&c: &1/suitcase &9help");
		Suitcase.messagesKeys.set("help.command.help.aliases", "&5Aliases&c: &9help &c[&9h&c, &9?&c]");
		Suitcase.messagesKeys.set("help.command.help.argument.help", "&9help &7>> &6Show a list of all accessible commands");
		// info help
		Suitcase.messagesKeys.set("help.command.info.header", " &7----- &aInfo command &7-----");
		Suitcase.messagesKeys.set("help.command.info.usage", "&5Usage&c: &1/suitcase &9info");
		Suitcase.messagesKeys.set("help.command.info.aliases", "&5Aliases&c: &9info &c[&9i&c, &9about&c, &9a&c]");
		Suitcase.messagesKeys.set("help.command.info.argument.info", "&9info &7>> &6Show plugin authors and current version");
		// rate help
		Suitcase.messagesKeys.set("help.command.rate.header", " &7----- &aRate command &7-----");
		Suitcase.messagesKeys.set("help.command.rate.usage", "&5Usage&c: &1/suitcase &9rate &3name &brating");
		Suitcase.messagesKeys.set("help.command.rate.aliases", "&5Aliases&c: &9rate &c[&9r&c, &9vote&c, &9v&c]");
		Suitcase.messagesKeys.set("help.command.rate.argument.rate", "&9rate &7>> &6Rate players or view own rating if no name is given");
		Suitcase.messagesKeys.set("help.command.rate.argument.name", "&3name &7>> &6Select player");
		Suitcase.messagesKeys.set("help.command.rate.argument.rating", "&brating &7>> &6Give a good &2+ or bad &4- &6rating");
		// warn help
		Suitcase.messagesKeys.set("help.command.warn.header", " &7----- &aWarn command &7-----");
		Suitcase.messagesKeys.set("help.command.warn.usage", "&5Usage&c: &1/suitcase &9warn &3name");
		Suitcase.messagesKeys.set("help.command.warn.aliases", "&5Aliases&c: &9warn &c[&9w&c, &9!&c]");
		Suitcase.messagesKeys.set("help.command.warn.argument.warn", "&9warn &7>> &6Warn a player and increase his warning counter");
		Suitcase.messagesKeys.set("help.command.warn.argument.name", "&3name &7>> &6Select a player");
		// forgive help
		Suitcase.messagesKeys.set("help.command.forgive.header", " &7----- &aForgive command &7-----");
		Suitcase.messagesKeys.set("help.command.forgive.usage", "&5Usage&c: &1/suitcase &9forgive &3name");
		Suitcase.messagesKeys.set("help.command.forgive.aliases", "&5Aliases&c: &9forgive &c[&9f&c]");
		Suitcase.messagesKeys.set("help.command.forgive.argument.forgive", "&9forgive &7>> &6Forgive a player and reset his warning counter");
		Suitcase.messagesKeys.set("help.command.forgive.argument.name", "&3name &7>> &6Select a player");
		// reload help
		Suitcase.messagesKeys.set("help.command.reload.header", " &7----- &aReload command &7-----");
		Suitcase.messagesKeys.set("help.command.reload.usage", "&5Usage&c: &1/suitcase &9reload");
		Suitcase.messagesKeys.set("help.command.reload.aliases", "&5Aliases&c: &9none");
		Suitcase.messagesKeys.set("help.command.reload.argument.reload", "&9reload &7>> &6Reload all configuration files and database connections");
		
		// info command
		// plugin info
		Suitcase.messagesKeys.set("info.header", " &7----- &aAbout Suitcase &7-----");
		Suitcase.messagesKeys.set("info.version", "&5Current version&4: &6{version}");
		Suitcase.messagesKeys.set("info.description", "&5Description&4: &6{description}");
		Suitcase.messagesKeys.set("info.authors", "&5Authors&4: &6{authors}");
		Suitcase.messagesKeys.set("info.website", "&5Website&4: &6{website}");
		
		// rate command
		// one's own rating
		Suitcase.messagesKeys.set("rate.view.self.header", " &7----- &aYour rating &7-----");
		Suitcase.messagesKeys.set("rate.view.self.rating", "&5Rating&4: &6{rating}");
		Suitcase.messagesKeys.set("rate.view.self.warnings", "&5Warnings&4: &6{warnings}");
		// rating of others
		Suitcase.messagesKeys.set("rate.view.others.header", " &7----- &a{player}'s rating &7-----");
		Suitcase.messagesKeys.set("rate.view.others.rating", "&5Rating&4: &6{rating}");
		Suitcase.messagesKeys.set("rate.view.others.warnings", "&5Warnings&4: &6{warnings}");
		// rate players
		Suitcase.messagesKeys.set("rate.set", "&2You have successfully rated {player}!");
		
		// warn command
		Suitcase.messagesKeys.set("warn.set", "&2You have successfully warned {player}!");
		
		// reload command
		Suitcase.messagesKeys.set("reload.done", "&2Suitcase reloaded.");
		
		// system messages
		// command errors
		Suitcase.messagesKeys.set("system.command.deny", "&4You don't have permission to that command!");
		Suitcase.messagesKeys.set("system.command.unknown", "&4Can't find that command! Try /suitcase help");
		Suitcase.messagesKeys.set("system.command.console", "&4This command can't be run by console!");
		Suitcase.messagesKeys.set("system.command.disabled", "&4This command is disabled.");
		Suitcase.messagesKeys.set("system.command.too-many-arguments", "&4Too many arguments!");
		Suitcase.messagesKeys.set("system.command.invalid-arguments", "&4Invalid amount of arguments!");
		Suitcase.messagesKeys.set("system.command.invalid-playername", "&4Can't find selected player!");
		Suitcase.messagesKeys.set("system.command.invalid-help", "&4There is no help page for that command.");
		Suitcase.messagesKeys.set("system.command.invalid-rating", "&4Your entered rating is invalid!");
		Suitcase.messagesKeys.set("system.command.unrated", "&4You haven't been rated by this player yet.");
		
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
