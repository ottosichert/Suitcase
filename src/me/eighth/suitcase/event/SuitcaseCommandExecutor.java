package me.eighth.suitcase.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.log.SuitcaseConsole.Action;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuitcaseCommandExecutor implements CommandExecutor {
	
	private Suitcase plugin;
	private Map<String, ArrayList<String>> aliases = new HashMap<String, ArrayList<String>>();
	private Map<String, String> usage = new HashMap<String, String>();
	private Map<String, String> info = new HashMap<String, String>();
	
	public SuitcaseCommandExecutor(Suitcase plugin) {
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
		info.put("rating", "Give a rating.");
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
			
			if (plugin.perm.hasPermission(sender.getName(), "suitcase.help")) {
				// check command argument
				if (arguments.size() == 2) {
					
					// send help for that command
					if (commands.contains(arguments.get(1))) {
						if (plugin.perm.hasPermission(sender.getName(), "suitcase." + arguments.get(1)) || (plugin.perm.hasPermission(sender.getName(), "suitcase.warn") && arguments.get(1) == "forgive") || plugin.cfg.data.getBoolean("mechanics.full-help")) {
							// send header, usage and aliases
							lines.add(plugin.msg.parse(plugin.msg.data.getString("help.header"), "command", arguments.get(1)));
							lines.add(plugin.msg.parse(plugin.msg.data.getString("help.usage"), "usage", usage.get(arguments.get(1))));
							lines.add(plugin.msg.parse(plugin.msg.data.getString("help.aliases"), "aliases", plugin.msg.getString(aliases.get(arguments.get(1)), true), "[,]"));
							// get each argument
							for (String argument : info.keySet()) {
								if (usage.get(arguments.get(1)).contains(argument)) {
									lines.add(plugin.msg.parse(plugin.msg.parse(plugin.msg.data.getString("help.info"), "object", argument), "info", info.get(argument)));
								}
							}
							plugin.con.log(Action.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase help " + arguments.get(1))));
						}
						else {
							// no permission to that command
							lines.add(plugin.msg.parse(plugin.msg.data.getString("error.command.deny"), "command", "/suitcase help " + arguments.get(1)));
							plugin.con.log(Action.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase help " + arguments.get(1))));
						}
					}
					else {
						// can't find help for this command
						lines.add(plugin.msg.parse(plugin.msg.data.getString("error.argument.help"), "command", "/suitcase " + arguments.get(1)));
						plugin.con.log(Action.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase help " + arguments.get(1))));
					}
				}
				// full command help
				else if (arguments.size() == 1) {
					// add header
					lines.add(plugin.msg.parse(plugin.msg.data.getString("help.header"), "command", "help"));
					
					// parsing permissions for commands
					for (String cmd : commands) {
						// command forgive has permission suitcase.warn, otherwise check default permissions
						if ((cmd == "forgive" && plugin.perm.hasPermission(sender.getName(), "suitcase.warn")) || plugin.perm.hasPermission(sender.getName(), "suitcase." + cmd) || plugin.cfg.data.getBoolean("mechanics.full-help")) {
							lines.add(plugin.msg.parse(plugin.msg.parse(plugin.msg.data.getString("help.info"), "object", usage.get(cmd)), "info", info.get(cmd)));
						}
					}
					
					// add info message
					lines.add(plugin.msg.data.getString("help.optional"));
					plugin.con.log(Action.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase help")));
				}
				else {
					// too many arguments
					lines.add(plugin.msg.data.getString("error.argument.count"));
					plugin.con.log(Action.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.msg.getString(arguments, false))));
				}
			}
			else {
				// no permission to help command
				lines.add(plugin.msg.parse(plugin.msg.data.getString("error.command.deny"), "command", "/suitcase help"));
				plugin.con.log(Action.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase help")));
			}
		}
		// /suitcase info
		else if (aliases.get("info").contains(arguments.get(0))) {
			if (plugin.perm.hasPermission(sender.getName(), "suitcase.info")) {
				if (arguments.size() > 1) {
					// too many arguments
					lines.add(plugin.msg.data.getString("error.argument.count"));
					plugin.con.log(Action.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.msg.getString(arguments, false))));
				}
				else {
					// send plugin info
					lines.add(plugin.msg.data.getString("info.header"));
					lines.add(plugin.msg.parse(plugin.msg.data.getString("info.version"), "version", plugin.name + " " + plugin.getDescription().getFullName(), " v|\\."));
					lines.add(plugin.msg.parse(plugin.msg.data.getString("info.description"), "description", plugin.getDescription().getDescription()));
					lines.add(plugin.msg.parse(plugin.msg.data.getString("info.authors"), "authors", plugin.msg.getString(plugin.getDescription().getAuthors(), true), "\\, "));
					lines.add(plugin.msg.parse(plugin.msg.data.getString("info.website"), "website", plugin.getDescription().getWebsite(), "[\\-\\.]"));
					plugin.con.log(Action.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase info")));
				}
			}
			else {
				// no permission to info command
				lines.add(plugin.msg.parse(plugin.msg.data.getString("error.command.deny"), "command", "/suitcase info"));
				plugin.con.log(Action.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase info")));
			}
		}
		// /suitcase rate [player] [rating]
		else if (aliases.get("rate").contains(arguments.get(0))) {
			if (plugin.perm.hasPermission(sender.getName(), "suitcase.rate")) {
				// check if rating is enabled
				if (plugin.cfg.data.getBoolean("mechanics.rating.enable")) {
					// parse arguments
					
					// no arguments -> view one's own rating
					if (arguments.size() == 1) {
						// show rating and warnings
						if (sender instanceof Player) {
							lines.add(plugin.msg.parse(plugin.msg.data.getString("rate.header"), "player", "Your"));
							lines.add(plugin.msg.parse(plugin.msg.parse(plugin.msg.data.getString("rate.rating"), "rating", String.valueOf(plugin.con.getRating(sender.getName()))), "maxrate", String.valueOf(plugin.cfg.data.getInt("mechanics.rating.maximum"))));
							lines.add(plugin.msg.parse(plugin.msg.parse(plugin.msg.data.getString("rate.warnings"), "warnings", String.valueOf(plugin.con.getWarnings(sender.getName()))), "maxwarn",  String.valueOf(plugin.cfg.data.getInt("mechanics.warnings.maximum"))));
							plugin.con.log(Action.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate")));
						}
						else {
							// console has no rating
							lines.add(plugin.msg.parse(plugin.msg.data.getString("error.command.console"), "command", "/suitcase rate"));
							plugin.con.log(Action.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate")));
						}
					}
					
					// one argument -> view rating of someone else
					else if (arguments.size() == 2) {
						// check if player is registered
						if (plugin.con.isRegistered(arguments.get(1))) {
							if (!plugin.perm.hasPermission(arguments.get(1), "suitcase.rate")) {
								// send target's rating and warnings
								lines.add(plugin.msg.parse(plugin.msg.data.getString("rate.header"), "player", arguments.get(1) + "'s", "\\'"));
								lines.add(plugin.msg.parse(plugin.msg.parse(plugin.msg.data.getString("rate.rating"), "rating", String.valueOf(plugin.con.getRating(arguments.get(1)))), "maxrate", String.valueOf(plugin.cfg.data.getInt("mechanics.rating.maximum"))));
								lines.add(plugin.msg.parse(plugin.msg.parse(plugin.msg.data.getString("rate.warnings"), "warnings", String.valueOf(plugin.con.getWarnings(arguments.get(1)))), "maxwarn",  String.valueOf(plugin.cfg.data.getInt("mechanics.warnings.maximum"))));
								plugin.con.log(Action.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + arguments.get(1))));
							}
							else {
								// player can't be rated
								lines.add(plugin.msg.parse(plugin.msg.data.getString("error.player.rate"), "player", arguments.get(1)));
								plugin.con.log(Action.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + arguments.get(1))));
							}
						}
						else {
							// player doesn't exist
							lines.add(plugin.msg.parse(plugin.msg.data.getString("error.player.name"), "player", arguments.get(1)));
							plugin.con.log(Action.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + arguments.get(1))));
						}
					}
					
					// two arguments -> rate a player
					else if (arguments.size() == 3) {
						// check if player exists
						if (plugin.con.isRegistered(arguments.get(1))) {
							if (!plugin.perm.hasPermission(arguments.get(1), "suitcase.rate")) {
								try { // catch NumberFormatException
									if (Integer.parseInt(arguments.get(2)) >= 0 && Integer.parseInt(arguments.get(2)) <= plugin.cfg.data.getInt("mechanics.rating.maximum")) {
										if (plugin.con.setRating(sender.getName(), arguments.get(1), Integer.parseInt(arguments.get(2)))) {
											lines.add(plugin.msg.parse(plugin.msg.data.getString("rate.done"), "player", arguments.get(1)));
											plugin.con.log(Action.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + arguments.get(1) + " " + arguments.get(2))));
										}
										else {
											// rating failed
											lines.add(plugin.msg.parse(plugin.msg.data.getString("error.player.rate"), "player", arguments.get(1)));
											plugin.con.log(Action.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + arguments.get(1) + " " + arguments.get(2))));
										}
									}
									else {
										// rating not found
										lines.add(plugin.msg.parse(plugin.msg.data.getString("error.argument.rating"), "rating", arguments.get(2)));
										plugin.con.log(Action.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + arguments.get(1) + " " + arguments.get(2))));
									}
								}
								catch (NumberFormatException e)
								{
									// no number
									lines.add(plugin.msg.parse(plugin.msg.data.getString("error.argument.rating"), "rating", arguments.get(2)));
									plugin.con.log(Action.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + arguments.get(1) + " " + arguments.get(2))));
								}
							}
							else {
								// player can't be rated
								lines.add(plugin.msg.parse(plugin.msg.data.getString("error.player.rate"), "player", arguments.get(1)));
								plugin.con.log(Action.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + arguments.get(1))));
							}
						}
						else {
							// player doesn't exist
							lines.add(plugin.msg.parse(plugin.msg.data.getString("error.player.name"), "player", arguments.get(1)));
							plugin.con.log(Action.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + arguments.get(1) + " " + arguments.get(2))));
						}
					}
					else {
						// too many arguments
						lines.add(plugin.msg.data.getString("error.argument.count"));
						plugin.con.log(Action.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.msg.getString(arguments, false))));
					}
				}
				else {
					// rating is not enabled
					lines.add(plugin.msg.parse(plugin.msg.data.getString("error.command.disabled"), "command", "/suitcase rate"));
					plugin.con.log(Action.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.msg.getString(arguments, false))));
				}
			}
			else {
				// player doesn't have permission
				lines.add(plugin.msg.parse(plugin.msg.data.getString("error.command.deny"), "command", "/suitcase rate"));
				plugin.con.log(Action.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate")));
			}
		}
		// /suitcase warn/forgive [player]
		else if (aliases.get("warn").contains(arguments.get(0)) || aliases.get("forgive").contains(arguments.get(0))) {
			if (plugin.perm.hasPermission(sender.getName(), "suitcase.warn")) {
				// check if warning is enabled
				if (plugin.cfg.data.getBoolean("mechanics.warnings.enable")) {
					// two arguments: warn/forgive and a player's name
					if (arguments.size() == 2) {
						// check if targeted player exists
						if (plugin.con.isRegistered(arguments.get(1))) {
							if (aliases.get("warn").contains(arguments.get(0))) {
								if (plugin.con.setWarnings(sender.getName(), arguments.get(1), true)) {
									lines.add(plugin.msg.parse(plugin.msg.data.getString("warn.done"), "player", arguments.get(1)));
									plugin.con.log(Action.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase warn " + arguments.get(1))));
								}
								else {
									// warning failed
									lines.add(plugin.msg.parse(plugin.msg.data.getString("error.player.warn"), "player", arguments.get(1)));
									plugin.con.log(Action.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase warn " + arguments.get(1))));
								}
							}
							else { // forgive
								if (plugin.con.setWarnings(sender.getName(), arguments.get(1), false)) {
									lines.add(plugin.msg.parse(plugin.msg.data.getString("forgive.done"), "player", arguments.get(1)));
									plugin.con.log(Action.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase forgive " + arguments.get(1))));
								}
								else {
									// can't be warned -> can't be forgiven
									lines.add(plugin.msg.parse(plugin.msg.data.getString("error.player.warn"), "player", arguments.get(1)));
									plugin.con.log(Action.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase warn " + arguments.get(1))));
								}
							}
						}
						else {
							// player doesn't exist
							lines.add(plugin.msg.parse(plugin.msg.data.getString("error.player.name"), "player", arguments.get(1)));
							plugin.con.log(Action.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.msg.getString(arguments, false))));
						}
					}
					else {
						// invalid amount of arguments
						lines.add(plugin.msg.data.getString("error.argument.count"));
						plugin.con.log(Action.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.msg.getString(arguments, false))));
					}
				}
				else {
					// warning is not enabled
					lines.add(plugin.msg.parse(plugin.msg.data.getString("error.command.disabled"), "command", "/suitcase warn"));
					plugin.con.log(Action.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.msg.getString(arguments, false))));
				}
			}
			else {
				// player isn't allowed to warn
				lines.add(plugin.msg.parse(plugin.msg.data.getString("error.command.deny"), "command", "/suitcase warn"));
				plugin.con.log(Action.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase warn")));
			}
		}
		// /suitcase reload
		else if (aliases.get("reload").contains(arguments.get(0))) {
			if (plugin.perm.hasPermission(sender.getName(), "suitcase.reload")) {
				if (arguments.size() > 1) {
					// too many arguments
					lines.add(plugin.msg.data.getString("error.argument.count"));
					plugin.con.log(Action.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.msg.getString(arguments, false))));
				}
				else {
					plugin.con.log(Action.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase reload")));
					plugin.reload();
					lines.add(plugin.msg.data.getString("reload.done"));
				}
			}
			else {
				// no permission
				lines.add(plugin.msg.parse(plugin.msg.data.getString("error.command.deny"), "command", "/suitcase reload"));
				plugin.con.log(Action.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase reload")));
			}
		}
		// command not found
		else {
			lines.add(plugin.msg.parse(plugin.msg.parse(plugin.msg.data.getString("error.command.unknown"), "command", "/suitcase " + plugin.msg.getString(arguments, false)), "help", "/suitcase help"));
			plugin.con.log(Action.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.msg.getString(arguments, false))));
		}
		
		// send edited message at the end
		plugin.msg.sendMessage(sender, lines);
		
		// always return true, because we handle wrong commands/arguments
		return true;
	}
}
