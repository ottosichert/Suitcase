package me.eighth.suitcase.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseConsole.Action;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuitcaseCommandExecutor implements CommandExecutor {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/** All available command aliases */
	private Map<String, ArrayList<String>> aliases = new HashMap<String, ArrayList<String>>();
	
	/** Stores usage of all commands */
	private Map<String, String> usage = new HashMap<String, String>();
	
	/** Short information about commands or arguments */
	private Map<String, String> info = new HashMap<String, String>();
	
	/** Permissions linked to commands */
	private Map<String, String> permission = new HashMap<String, String>();
	
	/** Plugin reset confirmation */
	private String reset = "";
	
	/**
	 * Command handler for all suitcase commands
	 * @param plugin Instance of Suitcase
	 */
	public SuitcaseCommandExecutor(Suitcase plugin) {
		this.plugin = plugin;
		
		// set command aliases
		aliases.put("help", new ArrayList<String>(Arrays.asList("help", "h", "?")));
		aliases.put("info", new ArrayList<String>(Arrays.asList("info", "i", "about", "a")));
		aliases.put("rate", new ArrayList<String>(Arrays.asList("rate", "r", "vote", "v")));
		aliases.put("warn", new ArrayList<String>(Arrays.asList("warn", "w", "!")));
		aliases.put("forgive", new ArrayList<String>(Arrays.asList("forgive", "f")));
		aliases.put("reload", new ArrayList<String>(Arrays.asList("reload", "l")));
		aliases.put("reset", new ArrayList<String>(Arrays.asList("reset", "s")));

		// command usages
		usage.put("help", "/suitcase help command");
		usage.put("info", "/suitcase info");
		usage.put("rate", "/suitcase rate name rating");
		usage.put("warn", "/suitcase warn name");
		usage.put("forgive", "/suitcase forgive name");
		usage.put("reload", "/suitcase reload");
		usage.put("reset", "/suitcase reset");
		
		// argument/command information
		info.put("help", "Shows help for commands.");
		info.put("info", "View current version and authors.");
		info.put("rate", "Rate or view rating of players.");
		info.put("warn", "Warn a player.");
		info.put("forgive", "Reset a player's warning counter.");
		info.put("reload", "Reload this plugin.");
		info.put("reset", "Reset configuration and player ratings.");
		info.put("command", "View help for a specific command.");
		info.put("name", "Select a player by name.");
		info.put("rating", "Give a rating.");
		
		// link permissions
		permission.put("help", "suitcase.help");
		permission.put("info", "suitcase.help");
		permission.put("rate", "suitcase.rate");
		permission.put("warn", "suitcase.warn");
		permission.put("forgive", "suitcase.warn");
		permission.put("reload", "suitcase.admin");
		permission.put("reset", "suitcase.admin");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// execute commands
		// args[0] -> sub command (help, info etc.)
		// args[1] -> player or command name
		// args[2] -> rating
		
		/** List of all lowercased arguments */
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
			
			if (plugin.perm.hasPermission(sender.getName(), permission.get("help"))) {
				// check command argument
				if (arguments.size() == 2) {
					
					// send help for that command
					if (aliases.keySet().contains(arguments.get(1))) {
						if (plugin.perm.hasPermission(sender.getName(), permission.get(arguments.get(1))) || plugin.cfg.data.getBoolean("mechanics.full-help")) {
							// send header, usage and aliases
							plugin.msg.send(sender, "help.header", "command", arguments.get(1));
							plugin.msg.send(sender, "help.usage", "usage", usage.get(arguments.get(1)));
							plugin.msg.send(sender, "help.aliases", "aliases", plugin.getString(aliases.get(arguments.get(1)), true));
							// get each argument
							for (String argument : info.keySet()) {
								if (usage.get(arguments.get(1)).contains(argument)) {
									plugin.msg.send(sender, "help.info", "object", argument, "info", info.get(argument));
								}
							}
							plugin.console.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), "/suitcase help " + arguments.get(1));
						}
						else {
							// no permission to that command
							plugin.msg.send(sender, "error.command.deny", "command", "/suitcase help " + arguments.get(1));
							plugin.console.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase help " + arguments.get(1));
						}
					}
					else {
						// can't find help for this command
						plugin.msg.send(sender, "error.argument.help", "command", "/suitcase " + arguments.get(1));
						plugin.console.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), "/suitcase help " + arguments.get(1));
					}
				}
				// full command help
				else if (arguments.size() == 1) {
					// add header
					plugin.msg.send(sender, "help.header", "command", "help");
					
					// parsing permissions for commands
					for (String cmd : aliases.keySet()) {
						// check permissions
						if (plugin.perm.hasPermission(sender.getName(), permission.get(cmd)) || plugin.cfg.data.getBoolean("mechanics.full-help")) {
							plugin.msg.send(sender, "help.info", "object", usage.get(cmd), "info", info.get(cmd));
						}
					}
					
					// add info message
					plugin.msg.send(sender, "help.optional");
					plugin.console.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), "/suitcase help");
				}
				else {
					// too many arguments
					plugin.msg.send(sender, "error.argument.count");
					plugin.console.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), "/suitcase " + plugin.getString(arguments, false));
				}
			}
			else {
				// no permission to help command
				plugin.msg.send(sender, "error.command.deny", "command", "/suitcase help");
				plugin.console.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase help");
			}
		}
		// /suitcase info
		else if (aliases.get("info").contains(arguments.get(0))) {
			if (plugin.perm.hasPermission(sender.getName(), permission.get("info"))) {
				if (arguments.size() > 1) {
					// too many arguments
					plugin.msg.send(sender, "error.argument.count");
					plugin.console.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), "/suitcase " + plugin.getString(arguments, false));
				}
				else {
					// send plugin info
					plugin.msg.send(sender, "info.header");
					plugin.msg.send(sender, "info.version", "version", plugin.name + " " + plugin.getDescription().getFullName());
					plugin.msg.send(sender, "info.description", "description", plugin.getDescription().getDescription());
					plugin.msg.send(sender, "info.authors", "authors", plugin.getString(plugin.getDescription().getAuthors(), true));
					plugin.msg.send(sender, "info.website", "website", plugin.getDescription().getWebsite());
					plugin.console.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), "/suitcase info");
				}
			}
			else {
				// no permission to info command
				plugin.msg.send(sender, "error.command.deny", "command", "/suitcase info");
				plugin.console.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase info");
			}
		}
		// /suitcase rate [player] [rating]
		else if (aliases.get("rate").contains(arguments.get(0))) {
			if (plugin.perm.hasPermission(sender.getName(), permission.get("rate"))) {
				// check if rating is enabled
				if (plugin.cfg.data.getBoolean("mechanics.rating.enable")) {
					// parse arguments
					
					// no arguments or your name -> view one's own rating
					if (arguments.size() == 1 || (arguments.size() == 2 && arguments.get(1).equals(sender.getName()))) {
						// show rating and warnings
						if (sender instanceof Player) {
							// send target's rating and warnings
							plugin.msg.send(sender, "rate.header", "player", "Your");
							
							// check if sender can be rated
							if (plugin.perm.hasPermission(sender.getName(), permission.get("rate"))) {
								plugin.msg.send(sender, "rate.rating", "rating", String.valueOf(plugin.con.getRating(sender.getName())), "maxrate", plugin.cfg.data.getString("mechanics.rating.maximum"));
							}
							else {
								plugin.msg.send(sender, "rate.no-rating");
							}
							
							// check if sender can be warned
							if (!plugin.perm.hasPermission(sender.getName(), permission.get("warn"))) {
								plugin.msg.send(sender, "rate.warnings", "warnings", String.valueOf(plugin.con.getWarnings(sender.getName())), "maxwarn",  plugin.cfg.data.getString("mechanics.warnings.maximum"));
							}
							else {
								plugin.msg.send(sender, "rate.no-warnings");
							}
							plugin.console.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), "/suitcase rate");
						}
						else {
							// console has no rating
							plugin.msg.send(sender, "error.command.console", "command", "/suitcase rate");
							plugin.console.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase rate");
						}
					}
					
					// one argument -> view rating of someone else
					else if (arguments.size() == 2) {
						// check if player is registered
						if (plugin.con.isRegistered(arguments.get(1))) {

							// send target's rating and warnings
							plugin.msg.send(sender, "rate.header", "player", arguments.get(1) + "'s");
							
							// check if target can be rated
							if (plugin.perm.hasPermission(arguments.get(1), permission.get("rate"))) {
								plugin.msg.send(sender, "rate.rating", "rating", String.valueOf(plugin.con.getRating(arguments.get(1))), "maxrate", plugin.cfg.data.getString("mechanics.rating.maximum"));
							}
							else {
								plugin.msg.send(sender, "rate.no-rating");
							}
							
							// check if target can be warned
							if (!plugin.perm.hasPermission(arguments.get(1), permission.get("warn"))) {
								plugin.msg.send(sender, "rate.warnings", "warnings", String.valueOf(plugin.con.getWarnings(arguments.get(1))), "maxwarn",  plugin.cfg.data.getString("mechanics.warnings.maximum"));
							}
							else {
								plugin.msg.send(sender, "rate.no-warnings");
							}
							
							plugin.console.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), "/suitcase rate " + arguments.get(1));
						}
						else {
							// player doesn't exist
							plugin.msg.send(sender, "error.player.name", "player", arguments.get(1));
							plugin.console.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), "/suitcase rate " + arguments.get(1));
						}
					}
					
					// two arguments -> rate a player
					else if (arguments.size() == 3) {
						// arguments.get(1) shouldn't be the sender's name
						if (!arguments.get(1).equals(sender.getName())) {
							// check if player exists
							if (plugin.con.isRegistered(arguments.get(1))) {
								// target must have permission suitcase.rate to be rated
								if (plugin.perm.hasPermission(arguments.get(1), permission.get("rate"))) {
									try { // catch NumberFormatException
										if (Integer.parseInt(arguments.get(2)) >= 0 && Integer.parseInt(arguments.get(2)) <= plugin.cfg.data.getInt("mechanics.rating.maximum")) {
											if (plugin.con.setRating(sender.getName(), arguments.get(1), Integer.parseInt(arguments.get(2)))) {
												plugin.msg.send(sender, "rate.done", "player", arguments.get(1));
												plugin.console.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), "/suitcase rate " + arguments.get(1) + " " + arguments.get(2));
											}
											else {
												// rating failed
												plugin.msg.send(sender, "error.player.rate", "player", arguments.get(1));
												plugin.console.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase rate " + arguments.get(1) + " " + arguments.get(2));
											}
										}
										else {
											// rating not found
											plugin.msg.send(sender, "error.argument.rating", "rating", arguments.get(2));
											plugin.console.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), "/suitcase rate " + arguments.get(1) + " " + arguments.get(2));
										}
									}
									catch (NumberFormatException e)
									{
										// no number
										plugin.msg.send(sender, "error.argument.rating", "rating", arguments.get(2));
										plugin.console.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), "/suitcase rate " + arguments.get(1) + " " + arguments.get(2));
									}
								}
								else {
									// player can't be rated
									plugin.msg.send(sender, "error.player.rate", "player", arguments.get(1));
									plugin.console.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), "/suitcase rate " + arguments.get(1));
								}
							}
							else {
								// player doesn't exist
								plugin.msg.send(sender, "error.player.name", "player", arguments.get(1));
								plugin.console.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), "/suitcase rate " + arguments.get(1) + " " + arguments.get(2));
							}
						}
						else {
							// player can't rate himself
							plugin.msg.send(sender, "error.player.self");
							plugin.console.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), "/suitcase rate " + arguments.get(1) + " " + arguments.get(2));
						}
					}
					else {
						// too many arguments
						plugin.msg.send(sender, "error.argument.count");
						plugin.console.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), "/suitcase " + plugin.getString(arguments, false));
					}
				}
				else {
					// rating is not enabled
					plugin.msg.send(sender, "error.command.disabled", "command", "/suitcase rate");
					plugin.console.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase " + plugin.getString(arguments, false));
				}
			}
			else {
				// player doesn't have permission
				plugin.msg.send(sender, "error.command.deny", "command", "/suitcase rate");
				plugin.console.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase rate");
			}
		}
		// /suitcase warn/forgive [player]
		else if (aliases.get("warn").contains(arguments.get(0)) || aliases.get("forgive").contains(arguments.get(0))) {
			if (plugin.perm.hasPermission(sender.getName(), permission.get("warn"))) {
				// we don't have to check if the given player is the sender himself because he has the permission to warn
				// check if warning is enabled
				if (plugin.cfg.data.getBoolean("mechanics.warnings.enable")) {
					// two arguments: warn/forgive and a player's name
					if (arguments.size() == 2) {
						// check if targeted player exists
						if (plugin.con.isRegistered(arguments.get(1))) {
							if (aliases.get("warn").contains(arguments.get(0))) {
								if (plugin.con.setWarnings(arguments.get(1), true)) {
									plugin.msg.send(sender, "warn.done", "player", arguments.get(1));
									plugin.console.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), "/suitcase warn " + arguments.get(1));
									plugin.broad.send("broadcast.warn", "player", arguments.get(1));
								}
								else {
									// warning failed
									plugin.msg.send(sender, "error.player.warn", "player", arguments.get(1));
									plugin.console.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase warn " + arguments.get(1));
								}
							}
							else { // forgive
								if (plugin.con.setWarnings(arguments.get(1), false)) {
									plugin.msg.send(sender, "forgive.done", "player", arguments.get(1));
									plugin.console.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), "/suitcase forgive " + arguments.get(1));
									plugin.broad.send("broadcast.forgive", "player", arguments.get(1));
								}
								else {
									// can't be warned -> can't be forgiven
									plugin.msg.send(sender, "error.player.warn", "player", arguments.get(1));
									plugin.console.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase warn " + arguments.get(1));
								}
							}
						}
						else {
							// player doesn't exist
							plugin.msg.send(sender, "error.player.name", "player", arguments.get(1));
							plugin.console.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), "/suitcase " + plugin.getString(arguments, false));
						}
					}
					else {
						// invalid amount of arguments
						plugin.msg.send(sender, "error.argument.count");
						plugin.console.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), "/suitcase " + plugin.getString(arguments, false));
					}
				}
				else {
					// warning is not enabled
					plugin.msg.send(sender, "error.command.disabled", "command", "/suitcase warn");
					plugin.console.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase " + plugin.getString(arguments, false));
				}
			}
			else {
				// player isn't allowed to warn
				plugin.msg.send(sender, "error.command.deny", "command", "/suitcase warn");
				plugin.console.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase warn");
			}
		}
		// /suitcase reload
		else if (aliases.get("reload").contains(arguments.get(0))) {
			if (plugin.perm.hasPermission(sender.getName(), permission.get("reload"))) {
				if (arguments.size() > 1) {
					// too many arguments
					plugin.msg.send(sender, "error.argument.count");
					plugin.console.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), "/suitcase " + plugin.getString(arguments, false));
				}
				else {
					// be sure that we reload the plugin after we sent a message to console and before we send a message to the executing player or console
					plugin.console.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), "/suitcase reload");
					plugin.reload();
					plugin.msg.send(sender, "reload.done");
				}
			}
			else {
				// no permission
				plugin.msg.send(sender, "error.command.deny", "command", "/suitcase reload");
				plugin.console.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase reload");
			}
		}
		// /suitcase reset
		else if (aliases.get("reset").contains(arguments.get(0))) {
			if (plugin.perm.hasPermission(sender.getName(), permission.get("reset"))) {
				if (arguments.size() > 1) {
					// too many arguments
					plugin.msg.send(sender, "error.argument.count");
					plugin.console.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), "/suitcase " + plugin.getString(arguments, false));
				}
				else {
					if (reset.equals(sender.getName())) {
						// delete all files
						plugin.console.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), "/suitcase reset");
						plugin.reset();
						plugin.msg.send(sender, "reset.done");
						plugin.broad.send("broadcast.reset");
						reset = "";
					}
					else {
						reset = sender.getName();
						plugin.msg.send(sender, "reset.confirm");
					}
				}
			}
			else {
				// no permission
				plugin.msg.send(sender, "error.command.deny", "command", "/suitcase reset");
				plugin.console.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase reset");
			}
		}
		// command not found
		else {
			plugin.msg.send(sender, "error.command.unknown", "command", "/suitcase " + plugin.getString(arguments, false), "help", "/suitcase help");
			plugin.console.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), "/suitcase " + plugin.getString(arguments, false));
		}
		
		// clear reset variable if entered another command
		if (!aliases.get("reset").contains(arguments.get(0)) && reset.equals(sender.getName())) {
			reset = ""; 
		}
		
		// always return true, because we handle wrong commands/arguments
		return true;
	}
}
