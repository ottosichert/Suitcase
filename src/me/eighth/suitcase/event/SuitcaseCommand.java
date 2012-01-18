package me.eighth.suitcase.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseConsole.actionType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuitcaseCommand implements CommandExecutor {
	
	private Suitcase plugin;
	private Map<String, ArrayList<String>> aliases = new HashMap<String, ArrayList<String>>();
	private Map<String, String> usage = new HashMap<String, String>();
	private Map<String, String> info = new HashMap<String, String>();
	
	public SuitcaseCommand(Suitcase plugin) {
		this.plugin = plugin;

		// command usages
		usage.put("help", "/suitcase help command");
		usage.put("info", "/suitcase info");
		usage.put("rate", "/suitcase rate name rating");
		usage.put("warn", "/suitcase warn name");
		usage.put("forgive", "/suitcase forgive name");
		usage.put("reload", "/suitcase reload");
		
		// argument/command information
		info.put("help", "Shows help for commands.");
		info.put("info", "View current version and authors.");
		info.put("rate", "Rate or view rating of players.");
		info.put("warn", "Warn a player.");
		info.put("forgive", "Reset a player's warning counter.");
		info.put("reload", "Reload this plugin.");
		info.put("command", "View help for a specific command.");
		info.put("name", "Select a player by name.");
		info.put("rating", "Enter positive or negative rating.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// execute commands
		// args[0] -> sub command (help, info etc.)
		// args[1] -> player or command name
		// args[2] -> rating
		
		// set command aliases
		aliases.put("help", new ArrayList<String>(Arrays.asList("help", "h", "?")));
		aliases.put("info", new ArrayList<String>(Arrays.asList("info", "i", "about", "a")));
		aliases.put("rate", new ArrayList<String>(Arrays.asList("rate", "r", "vote", "v")));
		aliases.put("positive", new ArrayList<String>(Arrays.asList("positive", "p", "good", "g", "+")));
		aliases.put("negative", new ArrayList<String>(Arrays.asList("negative", "n", "bad", "b", "-")));
		aliases.put("warn", new ArrayList<String>(Arrays.asList("warn", "w", "!")));
		aliases.put("forgive", new ArrayList<String>(Arrays.asList("forgive", "f")));
		aliases.put("reload", new ArrayList<String>(Arrays.asList("reload", "l")));
		
		// list of all available sub commands
		ArrayList<String> commands = new ArrayList<String>(Arrays.asList("help", "info", "rate", "warn", "forgive", "reload"));
		// messages are put here
		ArrayList<String> lines = new ArrayList<String>();
		// convert args to lowercase ArrayList
		ArrayList<String> arguments = new ArrayList<String>();
		for (String arg : args) {
			arguments.add(arg.toLowerCase());
		}
		
		// /suitcase help [command]
		if (arguments.size() == 0 || aliases.get("help").contains(arguments.get(0))) {
			
			// set first argument to help if no arguments are given
			if (arguments.size() == 0) {
				arguments.add("help");
			}
			
			if (plugin.permission.hasPermission(sender, "suitcase.help")) {
				// check command argument
				if (arguments.size() == 2) {
					
					// send help for that command
					if (commands.contains(arguments.get(1))) {
						if (plugin.permission.hasPermission(sender, "suitcase." + arguments.get(1)) || (plugin.permission.hasPermission(sender, "suitcase.warn") && arguments.get(1) == "forgive") || plugin.config.data.getBoolean("mechanics.full-help")) {
							// send header, usage and aliases
							lines.add(plugin.messages.parse(plugin.messages.data.getString("help.header"), "command", arguments.get(1)));
							lines.add(plugin.messages.parse(plugin.messages.data.getString("help.usage"), "usage", usage.get(arguments.get(1))));
							lines.add(plugin.messages.parse(plugin.messages.data.getString("help.aliases"), "aliases", plugin.messages.getString(aliases.get(arguments.get(1)), true), "[,]"));
							// get each argument
							for (String argument : info.keySet()) {
								if (usage.get(arguments.get(1)).contains(argument)) {
									lines.add(plugin.messages.parse(plugin.messages.parse(plugin.messages.data.getString("help.info"), "object", argument), "info", info.get(argument)));
								}
							}
							plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase help " + arguments.get(1))));
						}
						else {
							// no permission to that command
							lines.add(plugin.messages.parse(plugin.messages.data.getString("error.command.deny"), "command", "/suitcase help " + arguments.get(1)));
							plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase help " + arguments.get(1))));
						}
					}
					else {
						// can't find help for this command
						lines.add(plugin.messages.parse(plugin.messages.data.getString("error.argument.help"), "command", "/suitcase " + arguments.get(1)));
						plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase help " + arguments.get(1))));
					}
				}
				// full command help
				else if (arguments.size() == 1) {
					// add header
					lines.add(plugin.messages.parse(plugin.messages.data.getString("help.header"), "command", "help"));
					
					// parsing permissions for commands
					for (String cmd : commands) {
						// command forgive has permission suitcase.warn, otherwise check default permissions
						if ((cmd == "forgive" && plugin.permission.hasPermission(sender, "suitcase.warn")) || plugin.permission.hasPermission(sender, "suitcase." + cmd) || plugin.config.data.getBoolean("mechanics.full-help")) {
							lines.add(plugin.messages.parse(plugin.messages.parse(plugin.messages.data.getString("help.info"), "object", usage.get(cmd)), "info", info.get(cmd)));
						}
					}
					// add info message
					lines.add(plugin.messages.data.getString("help.optional"));
					plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase help")));
				}
				else {
					// too many arguments
					lines.add(plugin.messages.data.getString("error.argument.count"));
					plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.messages.getString(arguments, false))));
				}
			}
			else {
				// no permission to help command
				lines.add(plugin.messages.parse(plugin.messages.data.getString("error.command.deny"), "command", "/suitcase help"));
				plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase help")));
			}
		}
		// /suitcase info
		else if (aliases.get("info").contains(arguments.get(0))) {
			if (plugin.permission.hasPermission(sender, "suitcase.info")) {
				if (arguments.size() > 1) {
					// too many arguments
					lines.add(plugin.messages.data.getString("error.argument.count"));
					plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.messages.getString(arguments, false))));
				}
				else {
					// send plugin info
					lines.add(plugin.messages.data.getString("info.header"));
					lines.add(plugin.messages.parse(plugin.messages.data.getString("info.version"), "version", plugin.name + " " + plugin.getDescription().getFullName(), " v|\\."));
					lines.add(plugin.messages.parse(plugin.messages.data.getString("info.description"), "description", plugin.getDescription().getDescription()));
					lines.add(plugin.messages.parse(plugin.messages.data.getString("info.authors"), "authors", plugin.messages.getString(plugin.getDescription().getAuthors(), true), "\\, "));
					lines.add(plugin.messages.parse(plugin.messages.data.getString("info.website"), "website", plugin.getDescription().getWebsite(), "[\\-\\.]"));
					plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase info")));
				}
			}
			else {
				// no permission to info command
				lines.add(plugin.messages.parse(plugin.messages.data.getString("error.command.deny"), "command", "/suitcase info"));
				plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase info")));
			}
		}
		// /suitcase rate [player] [rating]
		else if (aliases.get("rate").contains(arguments.get(0))) {
			if (plugin.permission.hasPermission(sender, "suitcase.rate")) {
				// check if rating is enabled
				if (plugin.config.data.getBoolean("mechanics.rating.enable")) {
					// parse arguments
					
					// no arguments -> view one's own rating
					if (arguments.size() == 1) {
						// show rating and warnings
						if (sender instanceof Player) {
							lines.add(plugin.messages.parse(plugin.messages.data.getString("rate.header"), "player", "Your"));
							lines.add(plugin.messages.parse(plugin.messages.data.getString("rate.rating"), "rating", plugin.connector.getRating(sender.getName()), "/"));
							lines.add(plugin.messages.parse(plugin.messages.data.getString("rate.warnings"), "warnings", plugin.connector.getWarnings(sender.getName()), "/"));
							plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate")));
						}
						else {
							// console has no rating
							lines.add(plugin.messages.parse(plugin.messages.data.getString("error.command.console"), "command", "/suitcase rate"));
							plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate")));
						}
					}
					
					// one argument -> view rating of someone else
					else if (arguments.size() == 2) {
						// get player from name
						Player target = plugin.getServer().getPlayer(arguments.get(1));
						// check if player exists
						if (target != null) {
							// separate executing player from console
							if (plugin.connector.hasRated(target.getName(), sender.getName()) || !(sender instanceof Player)) {
								// send target's rating and warnings and check whether he has rated the player, who executed the command, or not
								lines.add(plugin.messages.parse(plugin.messages.data.getString("rate.header"), "player", target.getName() + "'s", "\\'"));
								lines.add(plugin.messages.parse(plugin.messages.data.getString("rate.rating"), "rating", plugin.connector.getRating(target.getName()), "/"));
								lines.add(plugin.messages.parse(plugin.messages.data.getString("rate.warnings"), "warnings", plugin.connector.getWarnings(target.getName()), "/"));
								plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + target.getName())));
							}
							else {
								// player has to rate targeted player first in order to view his rating
								lines.add(plugin.messages.parse(plugin.messages.data.getString("system.player.unrated"), "player", target.getName()));
								plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + target.getName())));
							}
						}
						else {
							// player doesn't exist
							lines.add(plugin.messages.parse(plugin.messages.data.getString("error.player.name"), "player", arguments.get(1)));
							plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + arguments.get(1))));
						}
					}
					
					// two arguments -> rate a player
					else if (arguments.size() == 3) {
						// get player from name
						Player target = plugin.getServer().getPlayer(arguments.get(1));
						// check if player exists
						if (target != null) {
							if (aliases.get("positive").contains(arguments.get(2))) {
								plugin.connector.setRating(sender.getName(), target.getName(), true); // true -> positive or good / false -> negative or bad
								lines.add(plugin.messages.parse(plugin.messages.data.getString("rate.done"), "player", target.getName()));
								plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + target.getName() + " positive")));
							}
							else if (aliases.get("negative").contains(arguments.get(2))) {
								plugin.connector.setRating(sender.getName(), target.getName(), false);
								lines.add(plugin.messages.parse(plugin.messages.data.getString("rate.done"), "player", target.getName()));
								plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + target.getName() + " negative")));
							}
							else {
								// rating not found
								lines.add(plugin.messages.parse(plugin.messages.data.getString("error.argument.rating"), "rating", arguments.get(2)));
								plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + target.getName() + " " + arguments.get(2))));
							}
						}
						else {
							// player doesn't exist
							lines.add(plugin.messages.parse(plugin.messages.data.getString("error.player.name"), "player", arguments.get(1)));
							plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + arguments.get(1) + " " + arguments.get(2))));
						}
					}
					else {
						// too many arguments
						lines.add(plugin.messages.data.getString("system.argument.count"));
						plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.messages.getString(arguments, false))));
					}
				}
				else {
					// rating is not enabled
					lines.add(plugin.messages.parse(plugin.messages.data.getString("error.command.disabled"), "command", "/suitcase rate"));
					plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.messages.getString(arguments, false))));
				}
			}
			else {
				// player doesn't have permission
				lines.add(plugin.messages.parse(plugin.messages.data.getString("error.command.deny"), "command", "/suitcase rate"));
				plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate")));
			}
		}
		// /suitcase warn/forgive [player]
		else if (aliases.get("warn").contains(arguments.get(0)) || aliases.get("forgive").contains(arguments.get(0))) {
			if (plugin.permission.hasPermission(sender, "suitcase.warn")) {
				// check if warning is enabled
				if (plugin.config.data.getBoolean("mechanics.warning.enable")) {
					// two arguments: warn/forgive and a player's name
					if (arguments.size() == 2) {
						// get player from name
						Player target = plugin.getServer().getPlayer(arguments.get(1));
						// check if targeted player exists
						if (target != null) {
							if (aliases.get("warn").contains(arguments.get(0))) {
								plugin.connector.setWarnings(sender.getName(), target.getName(), true);
								lines.add(plugin.messages.parse(plugin.messages.data.getString("warn.done"), "player", target.getName()));
								plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase warn " + target.getName())));
							}
							else { // forgive
								plugin.connector.setWarnings(sender.getName(), target.getName(), false);
								lines.add(plugin.messages.parse(plugin.messages.data.getString("forgive.done"), "player", target.getName()));
								plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase forgive " + target.getName())));
							}
						}
						else {
							// player doesn't exist
							lines.add(plugin.messages.parse(plugin.messages.data.getString("error.player.name"), "player", arguments.get(1)));
							plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.messages.getString(arguments, false))));
						}
					}
					else {
						// invalid amount of arguments
						lines.add(plugin.messages.data.getString("error.argument.count"));
						plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.messages.getString(arguments, false))));
					}
				}
				else {
					// warning is not enabled
					lines.add(plugin.messages.parse(plugin.messages.data.getString("error.command.disabled"), "command", "/suitcase warn"));
					plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.messages.getString(arguments, false))));
				}
			}
			else {
				// player isn't allowed to warn
				lines.add(plugin.messages.parse(plugin.messages.data.getString("error.command.deny"), "command", "/suitcase warn"));
				plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase warn")));
			}
		}
		// /suitcase reload
		else if (aliases.get("reload").contains(arguments.get(0))) {
			if (plugin.permission.hasPermission(sender, "suitcase.reload")) {
				if (arguments.size() > 1) {
					// too many arguments
					lines.add(plugin.messages.data.getString("error.argument.count"));
					plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.messages.getString(arguments, false))));
				}
				else {
					plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase reload")));
					plugin.reload();
					lines.add(plugin.messages.data.getString("reload.done"));
				}
			}
			else {
				// no permission
				lines.add(plugin.messages.parse(plugin.messages.data.getString("error.command.deny"), "command", "/suitcase reload"));
				plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase reload")));
			}
		}
		// command not found
		else {
			lines.add(plugin.messages.parse(plugin.messages.parse(plugin.messages.data.getString("error.command.unknown"), "command", "/suitcase " + plugin.messages.getString(arguments, false)), "help", "/suitcase help"));
			plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.messages.getString(arguments, false))));
		}
		
		// send edited message at the end
		plugin.messages.sendMessage(sender, lines);
		
		// always return true, because we handle wrong commands/arguments
		return true;
	}
}
