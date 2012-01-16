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
	private Map<String, ArrayList<String>> commandAliases = new HashMap<String, ArrayList<String>>();
	
	public SuitcaseCommand(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// execute commands
		// args[0] -> sub command (help, info etc.)
		// args[1] -> player or command name
		// args[2] -> rating
		
		// set command aliases
		commandAliases.put("help", new ArrayList<String>(Arrays.asList("help", "h", "?")));
		commandAliases.put("info", new ArrayList<String>(Arrays.asList("info", "i", "about", "a")));
		commandAliases.put("rate", new ArrayList<String>(Arrays.asList("rate", "r", "vote", "v")));
		commandAliases.put("rate.positive", new ArrayList<String>(Arrays.asList("positive", "p", "good", "g", "+")));
		commandAliases.put("rate.negative", new ArrayList<String>(Arrays.asList("negative", "n", "bad", "b", "-")));
		commandAliases.put("warn", new ArrayList<String>(Arrays.asList("warn", "w", "!")));
		commandAliases.put("warn.forgive", new ArrayList<String>(Arrays.asList("forgive", "f")));
		commandAliases.put("reload", new ArrayList<String>(Arrays.asList("reload")));
		
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
		if (arguments.size() == 0 || commandAliases.get("help").contains(arguments.get(0))) {
			
			// set first argument to help if no arguments are given
			if (arguments.size() == 0) {
				arguments.add("help");
			}
			
			if (plugin.permission.hasPermission(sender, "suitcase.help")) {
				// check command argument
				if (args.length > 1) {
					
					// send help for that command
					if (commands.contains(arguments.get(1))) {
						if (plugin.permission.hasPermission(sender, "suitcase." + arguments.get(1)) || plugin.config.config.getBoolean("mechanics.full-help")) {
							// send header, usage and aliases
							lines.add(plugin.message.config.getString("help.command." + args[1] + ".header"));
							lines.add(plugin.message.config.getString("help.command." + args[1] + ".usage"));
							lines.add(plugin.message.config.getString("help.command." + args[1] + ".aliases"));
							// get each argument
							for (String argument : plugin.message.config.getConfigurationSection("help.command." + args[1] + ".argument").getKeys(false)) {
								lines.add(plugin.message.config.getString("help.command." + arguments.get(1) + ".argument." + argument));
							}
							plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase help " + arguments.get(1))));
						}
						else {
							// no permission to that command
							lines.add(plugin.message.config.getString("system.command.deny").replaceFirst("\\{command\\}", "/suitcase help " + arguments.get(1)));
							plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase help " + arguments.get(1))));
						}
					}
					else {
						// can't find help for this command
						lines.add(plugin.message.config.getString("system.argument.help").replaceFirst("\\{help\\}", "/suitcase " + arguments.get(1)));
						plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + arguments.get(1))));
					}
				}
				// full command help
				else {
					// add header
					lines.add(plugin.message.config.getString("help.header"));
					
					// parsing permissions for commands
					for (String cmd : commands) {
						// command forgive has permission suitcase.warn
						if (cmd == "forgive") {
							if (plugin.permission.hasPermission(sender, "suitcase.warn") || plugin.config.config.getBoolean("mechanics.full-help")) {
								lines.add(plugin.message.config.getString("help.info.forgive"));
							}
						}
						else if (plugin.permission.hasPermission(sender, "suitcase." + cmd) || plugin.config.config.getBoolean("mechanics.full-help")) {
							lines.add(plugin.message.config.getString("help.info." + cmd));
						}
					}
					
					// add info message
					lines.add(plugin.message.config.getString("help.optional"));
					plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase help")));
				}
			}
			else {
				// no permission to help command
				lines.add(plugin.message.config.getString("system.command.deny").replaceFirst("\\{command\\}", "/suitcase help"));
				plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase help")));
			}
		}
		// /suitcase info
		else if (commandAliases.get("info").contains(arguments.get(0))) {
			if (plugin.permission.hasPermission(sender, "suitcase.info")) {
				if (arguments.size() > 1) {
					// too many arguments
					lines.add(plugin.message.config.getString("system.argument.count"));
				}
				else {
					// send plugin info
					lines.add(plugin.message.config.getString("info.header"));
					lines.add(plugin.message.config.getString("info.version").replaceFirst("\\{version\\}", plugin.name + " " + plugin.getDescription().getFullName()));
					lines.add(plugin.message.config.getString("info.description").replaceFirst("\\{description\\}", plugin.getDescription().getDescription()));
					lines.add(plugin.message.config.getString("info.authors").replaceFirst("\\{authors\\}", plugin.message.getString(plugin.getDescription().getAuthors(), true)));
					lines.add(plugin.message.config.getString("info.website").replaceFirst("\\{website\\}", plugin.getDescription().getWebsite().replaceAll("([/.:-])", "&7$1&6")));
					plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase info")));
				}
			}
			else {
				// no permission to info command
				lines.add(plugin.message.config.getString("system.command.deny").replaceFirst("\\{command\\}", "/suitcase info"));
				plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase info")));
			}
		}
		// /suitcase rate [player] [rating]
		else if (commandAliases.get("rate").contains(arguments.get(0))) {
			if (plugin.permission.hasPermission(sender, "suitcase.rate")) {
				// check if rating is enabled
				if (plugin.config.config.getBoolean("mechanics.rating.enable")) {
					// parse arguments
					
					// no arguments -> view one's own rating
					if (arguments.size() == 1) {
						// show rating and warnings
						if (sender instanceof Player) {
							lines.add(plugin.message.config.getString("rate.view.self.header"));
							lines.add(plugin.message.config.getString("rate.view.self.rating").replaceFirst("\\{rating\\}", plugin.connector.getRating(sender.getName())));
							lines.add(plugin.message.config.getString("rate.view.self.warnings").replaceFirst("\\{warnings\\}", plugin.connector.getWarnings(sender.getName())));
							plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate")));
						}
						else {
							// console has no rating
							lines.add(plugin.message.config.getString("system.command.console").replaceFirst("\\{command\\}", "/suitcase rate"));
							plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate")));
						}
					}
					
					// one argument -> view rating of someone
					else if (arguments.size() == 2) { // console can view rating of other players
						// get player from name
						Player target = plugin.getServer().getPlayer(arguments.get(1));
						// check if player exists
						if (target != null) {
							// separate executing player from console
							if (sender instanceof Player) {
								if (plugin.connector.hasRated(target.getName(), sender.getName())) {
									// send target's rating and warnings and check whether he has rated the player, who executed the command, or not
									lines.add(plugin.message.config.getString("rate.view.others.header").replaceFirst("\\{player\\}", target.getName()));
									lines.add(plugin.message.config.getString("rate.view.others.rating").replaceFirst("\\{rating\\}", plugin.connector.getRating(target.getName())));
									lines.add(plugin.message.config.getString("rate.view.others.warnings").replaceFirst("\\{warnings\\}", plugin.connector.getWarnings(target.getName())));
									plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + target.getName())));
								}
								else {
									// player has to rate targeted player first in order to view his rating
									lines.add(plugin.message.config.getString("system.player.unrated").replaceFirst("\\{player\\}", target.getName()));
									plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + target.getName())));
								}
							}
							else {
								// send player's rating and warnings
								lines.add(plugin.message.config.getString("rate.view.others.header").replaceFirst("\\{player\\}", target.getName()));
								lines.add(plugin.message.config.getString("rate.view.others.rating").replaceFirst("\\{rating\\}", plugin.connector.getRating(target.getName())));
								lines.add(plugin.message.config.getString("rate.view.others.warnings").replaceFirst("\\{warnings\\}", plugin.connector.getWarnings(target.getName())));
								plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate" + target.getName())));
							}
						}
						else {
							// player doesn't exist
							lines.add(plugin.message.config.getString("system.player.name").replaceFirst("\\{player\\}", arguments.get(1)));
							plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + arguments.get(1))));
						}
					}
					
					// two arguments -> rate a player
					else if (arguments.size() == 3) {
						// get player from name
						Player target = plugin.getServer().getPlayer(arguments.get(1));
						// check if player exists
						if (target != null) {
							if (commandAliases.get("rate.positive").contains(arguments.get(2))) {
								plugin.connector.setRating(sender.getName(), target.getName(), true); // true -> positive or good / false -> negative or bad
								lines.add(plugin.message.config.getString("rate.done").replaceFirst("\\{player\\}", target.getName()));
								plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + target.getName() + " positive")));
							}
							else if (commandAliases.get("rate.negative").contains(arguments.get(2))) {
								plugin.connector.setRating(sender.getName(), target.getName(), false);
								lines.add(plugin.message.config.getString("rate.done").replaceFirst("\\{player\\}", target.getName()));
								plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + target.getName() + " negative")));
							}
							else {
								// rating not found
								lines.add(plugin.message.config.getString("system.argument.rating").replaceFirst("\\{rating\\}", arguments.get(2)));
								plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + target.getName() + " " + arguments.get(2))));
							}
						}
						else {
							// player doesn't exist
							lines.add(plugin.message.config.getString("system.player.name").replaceFirst("\\{player\\}", arguments.get(1)));
							plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + arguments.get(1) + " " + arguments.get(2))));
						}
					}
					else {
						// too many arguments
						lines.add(plugin.message.config.getString("system.argument.count"));
						plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + plugin.message.getString(new ArrayList<String>(arguments.subList(1, arguments.size() - 1)), false))));
					}
				}
				else {
					// rating is not enabled
					lines.add(plugin.message.config.getString("system.command.disabled").replaceFirst("\\{command\\}", "/suitcase rate"));
					plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate")));
				}
			}
			else {
				// player doesn't have permission
				lines.add(plugin.message.config.getString("system.command.deny").replaceFirst("\\{command\\}", "/suitcase rate"));
				plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate")));
			}
		}
		// /suitcase positive/negative [player]
		else if (commandAliases.get("rate.positive").contains(arguments.get(0)) || commandAliases.get("rate.negative").contains(arguments.get(0))) {
			if (plugin.permission.hasPermission(sender, "suitcase.rate")) {
				// check if rating is enabled
				if (plugin.config.config.getBoolean("mechanics.rating.enable")) {
					// this command requires two arguments, the rating and the targeted player
					if (arguments.size() == 2) {
						// get player from name
						Player target = plugin.getServer().getPlayer(arguments.get(1));
						// check if sender is a player
						if (sender instanceof Player) {
							// check if player exists
							if (target != null) {
								if (commandAliases.get("rate.positive").contains(arguments.get(0))) {
									plugin.connector.setRating(sender.getName(), target.getName(), true);
									lines.add(plugin.message.config.getString("rate.done").replaceFirst("\\{player\\}", target.getName()));
									plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + target.getName() + " positive")));
								}
								// rate.negative
								else {
									plugin.connector.setRating(sender.getName(), target.getName(), false);
									lines.add(plugin.message.config.getString("rate.done").replaceFirst("\\{player\\}", target.getName()));
									plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + target.getName() + " negative")));
								}
							}
							else {
								// player doesn't exist
								lines.add(plugin.message.config.getString("system.player.name").replaceFirst("\\{player\\}", arguments.get(1)));
								plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + arguments.get(1) + " " + arguments.get(0))));
							}
						}
						else {
							// console can't rate
							lines.add(plugin.message.config.getString("system.command.console").replaceFirst("\\{command\\}", "/suitcase rate " + target.getName()));
							plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate " + target.getName())));
						}
					}
					else {
						// invalid amount of arguments
						lines.add(plugin.message.config.getString("system.argument.count"));
						plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.message.getString(arguments, false))));
					}
				}
				else {
					// rating is not enabled
					lines.add(plugin.message.config.getString("system.command.disabled").replaceFirst("\\{command\\}", "/suitcase rate"));
					plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate")));
				}
			}
			else {
				// player doesn't have permission
				lines.add(plugin.message.config.getString("system.command.deny").replaceFirst("\\{command\\}", "/suitcase rate"));
				plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase rate")));
			}
		}
		// /suitcase warn/forgive [player]
		else if (commandAliases.get("warn").contains(arguments.get(0)) || commandAliases.get("warn.forgive").contains(arguments.get(0))) {
			if (plugin.permission.hasPermission(sender, "suitcase.warn")) {
				// check if warning is enabled
				if (plugin.config.config.getBoolean("mechanics.warning.enable")) {
					// two arguments: warn/forgive and a player's name
					if (arguments.size() == 2) {
						// get player from name
						Player target = plugin.getServer().getPlayer(arguments.get(1));
						// check if targeted player exists
						if (target != null) {
							if (commandAliases.get("warn").contains(arguments.get(0))) {
								plugin.connector.setWarnings(sender.getName(), target.getName(), true);
								lines.add(plugin.message.config.getString("warn.done").replaceFirst("\\{player\\}", target.getName()));
								plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase warn " + target.getName())));
							}
							else { // warn.forgive
								plugin.connector.setWarnings(sender.getName(), target.getName(), false);
								lines.add(plugin.message.config.getString("forgive.done").replaceFirst("\\{player\\}", target.getName()));
								plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase forgive " + target.getName())));
							}
						}
						else {
							// player doesn't exist
							lines.add(plugin.message.config.getString("system.player.name").replaceFirst("\\{player\\}", arguments.get(1)));
							plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + arguments.get(0) + " " + arguments.get(1))));
						}
					}
					else {
						// invalid amount of arguments
						lines.add(plugin.message.config.getString("system.argument.count"));
						plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.message.getString(arguments, false))));
					}
				}
				else {
					// warning is not enabled
					lines.add(plugin.message.config.getString("system.command.disabled").replaceFirst("\\{command\\}", "/suitcase warn"));
					plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase warn")));
				}
			}
			else {
				// player isn't allowed to warn
				lines.add(plugin.message.config.getString("system.command.deny").replaceFirst("\\{command\\}", "/suitcase warn"));
				plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase warn")));
			}
		}
		// /suitcase reload
		else if (commandAliases.get("reload").contains(arguments.get(0))) {
			if (plugin.permission.hasPermission(sender, "suitcase.reload")) {
				if (arguments.size() > 1) {
					// too many arguments
					lines.add(plugin.message.config.getString("system.argument.count"));
					plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.message.getString(arguments, false))));
				}
				else {
					plugin.console.sendAction(actionType.PLAYER_COMMAND_EXECUTED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase reload")));
					plugin.reload();
					lines.add(plugin.message.config.getString("reload.done"));
				}
			}
			else {
				// no permission
				lines.add(plugin.message.config.getString("system.command.deny").replaceFirst("\\{command\\}", "/suitcase reload"));
				plugin.console.sendAction(actionType.PLAYER_COMMAND_DENIED, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase reload")));
			}
		}
		// command not found
		else {
			lines.add(plugin.message.config.getString("system.command.unknown").replaceFirst("\\{command\\}", "/suitcase " + plugin.message.getString(arguments, false)));
			plugin.console.sendAction(actionType.PLAYER_COMMAND_INVALID, new ArrayList<String>(Arrays.asList(sender.getName(), "/suitcase " + plugin.message.getString(arguments, false))));
		}
		
		// send edited message at the end
		plugin.message.sendMessage(sender, lines);
		
		// always return true, because we handle wrong commands/arguments
		return true;
	}
}
