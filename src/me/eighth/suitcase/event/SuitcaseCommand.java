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
		
		// list of all available sub commands
		ArrayList<String> commands = (ArrayList<String>) Arrays.asList("help", "info", "rate", "warn", "forgive", "reload");
		// messages are put here
		ArrayList<String> lines = new ArrayList<String>();
		// convert args to lowercase ArrayList
		ArrayList<String> arguments = new ArrayList<String>();
		for (String arg : args) {
			arguments.add(arg.toLowerCase());
		}
		// full command for error messages
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
						if (Suitcase.utPermission.hasPermission(sender, "suitcase." + arguments.get(1)) || Boolean.getBoolean(Suitcase.configKeys.get("mechanics.full-help").toString())) {
							// send header, usage and aliases
							lines.add(Suitcase.messagesKeys.get("help.command." + args[1] + ".header").toString());
							lines.add(Suitcase.messagesKeys.get("help.command." + args[1] + ".usage").toString());
							lines.add(Suitcase.messagesKeys.get("help.command." + args[1] + ".aliases").toString());
							// get each argument
							for (String key : Suitcase.messagesKeys.getConfigurationSection("help.command." + args[1] + ".argument").getKeys(false)) {
								lines.add(Suitcase.messagesKeys.get("help.command." + arguments.get(1) + ".argument." + key).toString());
							}
						}
						else {
							lines.add(Suitcase.messagesKeys.get("system.command.deny").toString());
							lines.add(full);
						}
						
					}
					// can't find sub command
					else {
						lines.add(Suitcase.messagesKeys.get("system.command.invalid").toString());
						lines.add(full);
					}
					
				}
				else {
					// add header
					lines.add(Suitcase.messagesKeys.get("help.self.header").toString());
					
					// parsing permissions for commands
					for (String cmd : commands) {
						// command forgive has permission suitcase.warn
						if (cmd == "forgive") {
							if (Suitcase.utPermission.hasPermission(sender, "suitcase.warn") || Boolean.getBoolean(Suitcase.configKeys.get("mechanics.full-help").toString())) {
								lines.add(Suitcase.messagesKeys.get("help.self.info.forgive").toString());
							}
						}
						else if (Suitcase.utPermission.hasPermission(sender, "suitcase." + cmd) || Boolean.getBoolean(Suitcase.configKeys.get("mechanics.full-help").toString())) {
							lines.add(Suitcase.messagesKeys.get("help.self.info." + cmd).toString());
						}
					}
					
					// add info message
					lines.add(Suitcase.messagesKeys.get("help.self.optional").toString());
				}
			}
			else {
				lines.add(Suitcase.messagesKeys.get("system.command.deny").toString());
				lines.add(full);
			}
		}
		// /suitcase info
		else if (Suitcase.commandAliases.get("info").contains(arguments.get(0))) {
			if (Suitcase.utPermission.hasPermission(sender, "suitcase.info")) {
				if (arguments.size() > 1) {
					lines.add(Suitcase.messagesKeys.get("system.command.invalid").toString());
					lines.add(full);
				}
				else {
					lines.add(Suitcase.messagesKeys.get("info.self.header").toString());
					lines.add(Suitcase.messagesKeys.get("info.self.version").toString() + Suitcase.version);
					lines.add(Suitcase.messagesKeys.get("info.self.description").toString());
					lines.add(Suitcase.messagesKeys.get("info.self.authors").toString());
					lines.add(Suitcase.messagesKeys.get("info.self.website").toString());
					lines.add(Suitcase.messagesKeys.get("info.self.source").toString());
				}
			}
			else {
				lines.add(Suitcase.messagesKeys.get("system.command.deny").toString());
				lines.add(full);
			}
		}
		// /suitcase rate [player] [rating]
		else if (Suitcase.commandAliases.get("rate").contains(arguments.get(0))) {
			
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
					lines.add(Suitcase.messagesKeys.get("system.command.invalid").toString());
					lines.add(full);
				}
				else {
					Suitcase.reload();
				}
			}
			else {
				lines.add(Suitcase.messagesKeys.get("system.command.deny").toString());
				lines.add(full);
			}
		}
		// command not found
		else {
			lines.add(Suitcase.messagesKeys.get("system.error.unknown").toString());
			lines.add(full);
		}
		
		// send edited message at the end
		Suitcase.cfMessage.sendMessage(sender, lines);
		// log to console
		Suitcase.utConsole.sendAction(actionType.PLAYER_COMMAND_EXECUTE, (ArrayList<String>) Arrays.asList(sender.getName(), full));
		return true;
	}
}
