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
				if (args.length > 1 && commands.contains(arguments.get(1))) {
					
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
						lines.add(Suitcase.messagesKeys.getString("system.command.invalid"));
						lines.add(full);
						invalid = true;
					}
				}
				else {
					// add header
					lines.add(Suitcase.messagesKeys.getString("help.self.header"));
					
					// parsing permissions for commands
					for (String cmd : commands) {
						// command forgive has permission suitcase.warn
						if (cmd == "forgive") {
							if (Suitcase.utPermission.hasPermission(sender, "suitcase.warn") || Suitcase.configKeys.getBoolean("mechanics.full-help")) {
								lines.add(Suitcase.messagesKeys.getString("help.self.info.forgive"));
							}
						}
						else if (Suitcase.utPermission.hasPermission(sender, "suitcase." + cmd) || Suitcase.configKeys.getBoolean("mechanics.full-help")) {
							lines.add(Suitcase.messagesKeys.getString("help.self.info." + cmd));
						}
					}
					
					// add info message
					lines.add(Suitcase.messagesKeys.getString("help.self.optional"));
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
					lines.add(Suitcase.messagesKeys.getString("system.command.invalid"));
					lines.add(full);
					invalid = true;
				}
				else {
					// send plugin info
					lines.add(Suitcase.messagesKeys.getString("info.self.header"));
					lines.add(Suitcase.messagesKeys.getString("info.self.version") + Suitcase.version);
					lines.add(Suitcase.messagesKeys.getString("info.self.description"));
					lines.add(Suitcase.messagesKeys.getString("info.self.authors"));
					lines.add(Suitcase.messagesKeys.getString("info.self.website"));
					lines.add(Suitcase.messagesKeys.getString("info.self.source"));
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
					if (arguments.size() == 1 && sender instanceof Player) {
						// TODO: use a connector class to file and database log, so we don't have to use something like this
						
						// we prefer logging to database
						if (Suitcase.configKeys.getBoolean("log.database.enable")) {
							// lines.add(Suitcase.lgDatabase.getRating((Player) sender));
						}
						else if (Suitcase.configKeys.getBoolean("log.file.enable")) {
							// lines.add(Suitcase.lgFile.getRating((Player) sender));
						}
						else {
							lines.add(Suitcase.messagesKeys.getString("system.error.log"));
							error = true;
						}
						
						// better:
						// lines.add(Suitcase.lgConnector.getRating((Player) sender));
					}
					else if (arguments.size() == 2) { // console can view rating of other players
						// get player from name
						Player target = Suitcase.plugin.getServer().getPlayer(arguments.get(1));
						// check if player exists
						if (target != null) {
							// separate executing player from console
							if (sender instanceof Player) {
								/*
								 * if (Suitcase.lgConnector.hasRated((Player) sender, target)) {
								 * 	lines.add(Suitcase.lgConnector.getRating(target));
								 * }
								 * else {
								 *  // player has to rate targeted player first in order to view his rating
								 * 	lines.add(Suitcase.messageKeys.getString("system.rate.unrated"));
								 * 	lines.add(full);
								 *  denied = true;
								 * }
								 */
							}
							else {
								// lines.add(Suitcase.lgConnector.getRating(target));
							}
							
						}
						else {
							// player doesn't exist
							lines.add(Suitcase.messagesKeys.getString("system.command.player"));
							lines.add(full);
							invalid = true;
						}
					}
				}
				else {
					lines.add(Suitcase.messagesKeys.getString("system.rate.disabled"));
					lines.add(full);
					denied = true;
				}
			}
			else {
				lines.add(Suitcase.messagesKeys.getString("system.command.deny"));
				lines.add(full);
				denied = true;
			}
		}
		// /suitcase positive [player]
		else if (Suitcase.commandAliases.get("rate.positive").contains(arguments.get(0))) {
			
		}
		// /suitcase negative [player]
		else if (Suitcase.commandAliases.get("rate.negative").contains(arguments.get(0))) {
			
		}
		// /suitcase warn [player]
		else if (Suitcase.commandAliases.get("warn").contains(arguments.get(0))) {
			
		}
		// /suitcase forgive [player]
		else if (Suitcase.commandAliases.get("warn.forgive").contains(arguments.get(0))) {
			
		}
		// /suitcase reload
		else if (Suitcase.commandAliases.get("reload").contains(arguments.get(0))) {
			if (Suitcase.utPermission.hasPermission(sender, "suitcase.reload")) {
				if (arguments.size() > 1) {
					lines.add(Suitcase.messagesKeys.getString("system.command.invalid"));
					lines.add(full);
					invalid = true;
				}
				else {
					Suitcase.reload();
				}
			}
			else {
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
			Suitcase.utConsole.sendAction(actionType.PLAYER_COMMAND_ERROR, (ArrayList<String>) Arrays.asList(sender.getName(), full, "logFetchDataError"));
		}
		else if (denied) {
			Suitcase.utConsole.sendAction(actionType.PLAYER_COMMAND_DENY, (ArrayList<String>) Arrays.asList(sender.getName(), full));
		}
		else if (invalid) {
			Suitcase.utConsole.sendAction(actionType.PLAYER_COMMAND_INVALID, (ArrayList<String>) Arrays.asList(sender.getName(), full));
		}
		else {
			Suitcase.utConsole.sendAction(actionType.PLAYER_COMMAND_EXECUTE, (ArrayList<String>) Arrays.asList(sender.getName(), full));
		}
		return true;
	}
}
