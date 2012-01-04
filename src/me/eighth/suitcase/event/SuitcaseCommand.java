package me.eighth.suitcase.event;

import java.util.ArrayList;
import java.util.Arrays;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseConsole.actionType;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuitcaseCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// execute commands
		// args[0] -> sub command (help, info etc.)
		// args[1] -> player or command name
		// args[2] -> rating
		
		// command status
		String errorMsg = "";
		boolean error = false;
		boolean denied = false;
		boolean invalid = false;
		
		// list of all available sub commands
		ArrayList<String> commands = (ArrayList<String>) Arrays.asList("help", "info", "rate", "warn", "forgive", "reload");
		// messages are put here
		ArrayList<String> lines = new ArrayList<String>();
		// convert args to lowercase ArrayList
		ArrayList<String> arguments = new ArrayList<String>();
		for (String arg : args) {
			arguments.add(arg.toLowerCase());
		}
		// full command for command error messages
		String full = "&4/" + label.toLowerCase() + " " + Suitcase.cfMessage.getString(arguments);
		
		// /suitcase help [command]
		if (Suitcase.commandAliases.get("help").contains(arguments.get(0)) || arguments.size() == 0) {
			
			// set first argument to help if no arguments are given
			if (arguments.size() == 0) {
				arguments.add("help");
			}
			
			if (Suitcase.utPermission.hasPermission(sender, "suitcase.help")) {
				
				// check command argument
				if (args.length > 1) {
					
					// send help for that command
					if (commands.contains(arguments.get(1))) {
						if (Suitcase.utPermission.hasPermission(sender, "suitcase." + arguments.get(1)) || Suitcase.configKeys.getBoolean("mechanics.full-help")) {
							// send header, usage and aliases
							lines.add(Suitcase.messagesKeys.getString("help.command." + args[1] + ".header"));
							lines.add(Suitcase.messagesKeys.getString("help.command." + args[1] + ".usage"));
							lines.add(Suitcase.messagesKeys.getString("help.command." + args[1] + ".aliases"));
							// get each argument
							for (String argument : Suitcase.messagesKeys.getConfigurationSection("help.command." + args[1] + ".argument").getKeys(false)) {
								lines.add(Suitcase.messagesKeys.getString("help.command." + arguments.get(1) + ".argument." + argument));
							}
						}
						else {
							// no permission to that command
							lines.add(Suitcase.messagesKeys.getString("system.command.deny"));
							lines.add(full);
							denied = true;
						}
					}
					else {
						// can't find sub command
						lines.add(Suitcase.messagesKeys.getString("system.command.invalid-help"));
						lines.add(full);
						invalid = true;
					}
				}
				else {
					// add header
					lines.add(Suitcase.messagesKeys.getString("help.header"));
					
					// parsing permissions for commands
					for (String cmd : commands) {
						// command forgive has permission suitcase.warn
						if (cmd == "forgive") {
							if (Suitcase.utPermission.hasPermission(sender, "suitcase.warn") || Suitcase.configKeys.getBoolean("mechanics.full-help")) {
								lines.add(Suitcase.messagesKeys.getString("help.info.forgive"));
							}
						}
						else if (Suitcase.utPermission.hasPermission(sender, "suitcase." + cmd) || Suitcase.configKeys.getBoolean("mechanics.full-help")) {
							lines.add(Suitcase.messagesKeys.getString("help.info." + cmd));
						}
					}
					
					// add info message
					lines.add(Suitcase.messagesKeys.getString("help.optional"));
				}
			}
			else {
				// no permission to help command
				lines.add(Suitcase.messagesKeys.getString("system.command.deny"));
				lines.add(full);
				denied = true;
			}
		}
		// /suitcase info
		else if (Suitcase.commandAliases.get("info").contains(arguments.get(0))) {
			if (Suitcase.utPermission.hasPermission(sender, "suitcase.info")) {
				if (arguments.size() > 1) {
					// too many arguments
					lines.add(Suitcase.messagesKeys.getString("system.command.too-many-arguments"));
					lines.add(full);
					invalid = true;
				}
				else {
					// send plugin info
					lines.add(Suitcase.messagesKeys.getString("info.header"));
					lines.add(Suitcase.messagesKeys.getString("info.version").replaceFirst("{version}", Suitcase.pdf.getFullName()));
					lines.add(Suitcase.messagesKeys.getString("info.description").replaceFirst("{description}", Suitcase.pdf.getDescription()));
					lines.add(Suitcase.messagesKeys.getString("info.authors").replaceFirst("{authors}", Suitcase.cfMessage.getString(Suitcase.pdf.getAuthors())));
					lines.add(Suitcase.messagesKeys.getString("info.website").replaceFirst("{website}", Suitcase.pdf.getWebsite()));
				}
			}
			else {
				// no permission to info command
				lines.add(Suitcase.messagesKeys.getString("system.command.deny"));
				lines.add(full);
				denied = true;
			}
		}
		// /suitcase rate [player] [rating]
		else if (Suitcase.commandAliases.get("rate").contains(arguments.get(0))) {
			if (Suitcase.utPermission.hasPermission(sender, "suitcase.rate")) {
				// check if rating is enabled
				if (Suitcase.configKeys.getBoolean("mechanics.rating.enable")) {
					// parse arguments
					
					// no arguments -> view one's own rating
					if (arguments.size() == 1) {
						// show rating and warnings
						if (sender instanceof Player) {
							lines.add(Suitcase.messagesKeys.getString("rate.view.self.header"));
							lines.add(Suitcase.messagesKeys.getString("rate.view.self.rating").replaceFirst("{rating}", Suitcase.lgConnector.getRating(sender.getName())));
							lines.add(Suitcase.messagesKeys.getString("rate.view.self.warnings").replaceFirst("{warnings}", Suitcase.lgConnector.getWarnings(sender.getName())));
						}
						else {
							// console has no rating
							lines.add(Suitcase.messagesKeys.getString("system.command.console"));
							lines.add(full);
							denied = true;
						}
					}
					
					// one argument -> view rating of someone
					else if (arguments.size() == 2) { // console can view rating of other players
						// get player from name
						Player target = Suitcase.plugin.getServer().getPlayer(arguments.get(1));
						// check if player exists
						if (target != null) {
							// separate executing player from console
							if (sender instanceof Player) {
								if (Suitcase.lgConnector.hasRated(target.getName(), sender.getName())) {
									// send target's rating and warnings and check whether he has rated the player, who executed the command, or not
									lines.add(Suitcase.messagesKeys.getString("rate.view.others.header").replaceFirst("{player}", target.getName()));
									lines.add(Suitcase.messagesKeys.getString("rate.view.others.rating").replaceFirst("{rating}", Suitcase.lgConnector.getRating(target.getName())));
									lines.add(Suitcase.messagesKeys.getString("rate.view.others.warnings").replaceFirst("{warnings}", Suitcase.lgConnector.getWarnings(target.getName())));
								}
								else {
									// player has to rate targeted player first in order to view his rating
									lines.add(Suitcase.messagesKeys.getString("system.command.unrated"));
									lines.add(full);
									denied = true;
								}
							}
							else {
								// send player's rating and warnings
								lines.add(Suitcase.messagesKeys.getString("rate.view.others.header").replaceFirst("{player}", target.getName()));
								lines.add(Suitcase.messagesKeys.getString("rate.view.others.rating").replaceFirst("{rating}", Suitcase.lgConnector.getRating(target.getName())));
								lines.add(Suitcase.messagesKeys.getString("rate.view.others.warnings").replaceFirst("{warnings}", Suitcase.lgConnector.getWarnings(target.getName())));
							}
						}
						else {
							// player doesn't exist
							lines.add(Suitcase.messagesKeys.getString("system.command.invalid-playername"));
							lines.add(full);
							invalid = true;
						}
					}
					
					// two arguments -> rate a player
					else if (arguments.size() == 3 && sender instanceof Player) {
						// get player from name
						Player target = Suitcase.plugin.getServer().getPlayer(arguments.get(1));
						// check if player exists
						if (target != null) {
							if (Suitcase.commandAliases.get("rate.positive").contains(arguments.get(2))) {
								Suitcase.lgConnector.setRating(sender.getName(), target.getName(), true); // true -> positive or good / false -> negative or bad
								lines.add(Suitcase.messagesKeys.getString("rate.set").replaceFirst("{player}", target.getName()));
							}
							else if (Suitcase.commandAliases.get("rate.negative").contains(arguments.get(2))) {
								Suitcase.lgConnector.setRating(sender.getName(), target.getName(), false);
								lines.add(Suitcase.messagesKeys.getString("rate.set").replaceFirst("{player}", target.getName()));
							}
							else {
								// rating not found
								lines.add(Suitcase.messagesKeys.getString("system.command.invalid-rating"));
								lines.add(full);
								invalid = true;
							}
						}
						else {
							// player doesn't exist
							lines.add(Suitcase.messagesKeys.getString("system.command.invalid-playername"));
							lines.add(full);
							invalid = true;
						}
					}
					else {
						// too many arguments
						lines.add(Suitcase.messagesKeys.getString("system.command.too-many-arguments"));
						lines.add(full);
						invalid = true;
					}
				}
				else {
					// rating is not enabled
					lines.add(Suitcase.messagesKeys.getString("system.command.disabled"));
					lines.add(full);
					denied = true;
				}
			}
			else {
				// player doesn't have permission
				lines.add(Suitcase.messagesKeys.getString("system.command.deny"));
				lines.add(full);
				denied = true;
			}
		}
		// /suitcase positive/negative [player]
		else if (Suitcase.commandAliases.get("rate.positive").contains(arguments.get(0)) || Suitcase.commandAliases.get("rate.negative").contains(arguments.get(0))) {
			if (Suitcase.utPermission.hasPermission(sender, "suitcase.rate")) {
				// check if rating is enabled
				if (Suitcase.configKeys.getBoolean("mechanics.rating.enable")) {
					// this command requires two arguments, the rating and the targeted player
					if (arguments.size() == 2) {
						// get player from name
						Player target = Suitcase.plugin.getServer().getPlayer(arguments.get(1));
						// check if sender is a player
						if (sender instanceof Player) {
							// check if player exists
							if (target != null) {
								if (Suitcase.commandAliases.get("rate.positive").contains(arguments.get(0))) {
									Suitcase.lgConnector.setRating(sender.getName(), target.getName(), true);
								}
								else { // rate.negative
									Suitcase.lgConnector.setRating(sender.getName(), target.getName(), false);
								}
							}
							else {
								// player doesn't exist
								lines.add(Suitcase.messagesKeys.getString("system.command.invalid-playername"));
								lines.add(full);
								invalid = true;
							}
						}
						else {
							// console can't rate
							lines.add(Suitcase.messagesKeys.getString("system.command.console"));
							lines.add(full);
							denied = true;
						}
					}
					else {
						// invalid amount of arguments
						lines.add(Suitcase.messagesKeys.getString("system.command.invalid-arguments"));
						lines.add(full);
						invalid = true;
					}
				}
				else {
					// rating is not enabled
					lines.add(Suitcase.messagesKeys.getString("system.command.disabled"));
					lines.add(full);
					denied = true;
				}
			}
			else {
				// player doesn't have permission
				lines.add(Suitcase.messagesKeys.getString("system.command.deny"));
				lines.add(full);
				denied = true;
			}
		}
		// /suitcase warn/forgive [player]
		else if (Suitcase.commandAliases.get("warn").contains(arguments.get(0)) || Suitcase.commandAliases.get("warn.forgive").contains(arguments.get(0))) {
			if (Suitcase.utPermission.hasPermission(sender, "suitcase.warn")) {
				// check if warning is enabled
				if (Suitcase.configKeys.getBoolean("mechanics.warning.enable")) {
					// two arguments: warn/forgive and a player's name
					if (arguments.size() == 2) {
						// get player from name
						Player target = Suitcase.plugin.getServer().getPlayer(arguments.get(1));
						// check if targeted player exists
						if (target != null) {
							if (Suitcase.commandAliases.get("warn").contains(arguments.get(0))) {
								Suitcase.lgConnector.setWarnings(sender.getName(), target.getName(), true);
							}
							else { // warn.forgive
								Suitcase.lgConnector.setWarnings(sender.getName(), target.getName(), false);
							}
						}
						else {
							// player doesn't exist
							lines.add(Suitcase.messagesKeys.getString("system.command.invalid-playername"));
							lines.add(full);
							invalid = true;
						}
					}
					else {
						// invalid amount of arguments
						lines.add(Suitcase.messagesKeys.getString("system.command.invalid-arguments"));
						lines.add(full);
						invalid = true;
					}
				}
				else {
					// warning is not enabled
					lines.add(Suitcase.messagesKeys.getString("system.command.disabled"));
					lines.add(full);
					denied = true;
				}
			}
			else {
				// player isn't allowed to warn
				lines.add(Suitcase.messagesKeys.getString("system.command.deny"));
				lines.add(full);
				denied = true;
			}
		}
		// /suitcase reload
		else if (Suitcase.commandAliases.get("reload").contains(arguments.get(0))) {
			if (Suitcase.utPermission.hasPermission(sender, "suitcase.reload")) {
				if (arguments.size() > 1) {
					// too many arguments
					lines.add(Suitcase.messagesKeys.getString("system.command.too-many-arguments"));
					lines.add(full);
					invalid = true;
				}
				else {
					Suitcase.reload();
					lines.add(Suitcase.messagesKeys.getString("reload.done"));
				}
			}
			else {
				// no permission
				lines.add(Suitcase.messagesKeys.getString("system.command.deny"));
				lines.add(full);
				denied = true;
			}
		}
		// command not found
		else {
			lines.add(Suitcase.messagesKeys.getString("system.command.unknown"));
			lines.add(full);
			invalid = true;
		}
		
		// send edited message at the end
		Suitcase.cfMessage.sendMessage(sender, lines);
		
		// log command status to console
		if (error) {
			Suitcase.utConsole.sendAction(actionType.PLAYER_COMMAND_ERROR, (ArrayList<String>) Arrays.asList(sender.getName(), full, errorMsg));
		}
		else if (denied) {
			Suitcase.utConsole.sendAction(actionType.PLAYER_COMMAND_DENY, (ArrayList<String>) Arrays.asList(sender.getName(), full));
		}
		else if (invalid) {
			Suitcase.utConsole.sendAction(actionType.PLAYER_COMMAND_INVALID, (ArrayList<String>) Arrays.asList(sender.getName(), full));
		}
		else { // no error occured
			Suitcase.utConsole.sendAction(actionType.PLAYER_COMMAND_EXECUTED, (ArrayList<String>) Arrays.asList(sender.getName(), full));
		}
		
		// always return true, because we handle wrong commands/arguments
		return true;
	}
}
