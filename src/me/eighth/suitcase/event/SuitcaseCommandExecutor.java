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
		aliases.put("top", new ArrayList<String>(Arrays.asList("top", "t")));
		aliases.put("warn", new ArrayList<String>(Arrays.asList("warn", "w", "!")));
		aliases.put("forgive", new ArrayList<String>(Arrays.asList("forgive", "f")));
		aliases.put("reload", new ArrayList<String>(Arrays.asList("reload", "l")));
		aliases.put("reset", new ArrayList<String>(Arrays.asList("reset", "s")));

		// command usages
		usage.put("help", "/suitcase help [command]");
		usage.put("info", "/suitcase info");
		usage.put("rate", "/suitcase rate [name] [rating]");
		usage.put("top", "/suitcase top");
		usage.put("warn", "/suitcase warn name");
		usage.put("forgive", "/suitcase forgive name");
		usage.put("reload", "/suitcase reload");
		usage.put("reset", "/suitcase reset");
		
		// argument/command information
		info.put("help", "Shows help for commands.");
		info.put("info", "Views current version and authors.");
		info.put("rate", "Rates or views rating of players.");
		info.put("top", "Gets the top ten best rated players.");
		info.put("warn", "Warns a player.");
		info.put("forgive", "Resets a player's warning counter.");
		info.put("reload", "Reloads this plugin.");
		info.put("reset", "Resets configuration and player ratings.");
		info.put("command", "Views help for a specific command.");
		info.put("name", "Selects a player by name.");
		info.put("rating", "Sets a rating.");
		
		// link permissions
		permission.put("help", "help");
		permission.put("info", "help");
		permission.put("rate", "rate");
		permission.put("top", "rate");
		permission.put("warn", "warn");
		permission.put("forgive", "warn");
		permission.put("reload", "admin");
		permission.put("reset", "admin");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// execute commands
		// args[0] -> sub command (help, info etc.)
		// args[1] -> player or command name
		// args[2] -> rating
		
		ArrayList<String> arguments = new ArrayList<String>();
		for (String arg : args) {
			arguments.add(arg);
		}
		
		// add default command
		if (arguments.size() == 0) {
			arguments.add("help");
		}
		String full = "/suitcase " + Suitcase.getStringFromList(arguments, " ");
		
		if (isAlias("help", arguments.get(0))) {
			if (arguments.size() == 1) {
				// /suitcase help
				help(sender);
			}
			else if (arguments.size() == 2) {
				if (aliases.keySet().contains(arguments.get(1))) {
					// /suitcase help command
					help(sender, arguments.get(1));
				}
				else {
					// invalid sub-command
					plugin.msg.send(sender, "error.argument.invalid", "argument", arguments.get(1));
					plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
				}
			}
			else {
				// too many arguments
				plugin.msg.send(sender, "error.argument.amount", "command", "help");
				plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
			}
		}
		else if (isAlias("info", arguments.get(0))) {
			if (arguments.size() == 1) {
				// /suitcase info
				info(sender);
			}
			else {
				// too many arguments
				plugin.msg.send(sender, "error.argument.amount", "command", "info");
				plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
			}
		}
		else if (isAlias("rate", arguments.get(0))) {
			if (arguments.size() == 1) {
				// /suitcase rate
				rate(sender);
			}
			else if (arguments.size() == 2) {
				// /suitcase rate player
				rate(sender, arguments.get(1));
			}
			else if (arguments.size() == 3) {
				try {
					// /suitcase rate player rating
					rate(sender, arguments.get(1), Integer.valueOf(arguments.get(2)));
				}
				catch (NumberFormatException e)
				{
					// invalid rating
					plugin.msg.send(sender, "error.argument.rating", "rating", arguments.get(2));
					plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
				}
			}
			else {
				// too many arguments
				plugin.msg.send(sender, "error.argument.amount", "command", "rate");
				plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
			}
		}
		else if (isAlias("top", arguments.get(0))) {
			if (arguments.size() == 1) {
				// /suitcase top
				top(sender);
			}
			else {
				// too many arguments
				plugin.msg.send(sender, "error.argument.amount", "command", "top");
				plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
			}
		}
		else if (isAlias("warn", arguments.get(0))) {
			if (arguments.size() < 2) {
				// missing argument player
				plugin.msg.send(sender, "error.argument.missing", "argument", "name");
				plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
			}
			else if (arguments.size() == 2) {
				// /suitcase warn player
				warn(sender, arguments.get(1));
			}
			else {
				// too many arguments
				plugin.msg.send(sender, "error.argument.amount", "command", "warn");
				plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
			}
		}
		else if (isAlias("forgive", arguments.get(0))) {
			if (arguments.size() < 2) {
				// missing argument player
				plugin.msg.send(sender, "error.argument.missing", "argument", "name");
				plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
			}
			else if (arguments.size() == 2) {
				// /suitcase forgive player
				forgive(sender, arguments.get(1));
			}
			else {
				// too many arguments
				plugin.msg.send(sender, "error.argument.amount", "command", "forgive");
				plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
			}
		}
		else if (isAlias("reload", arguments.get(0))) {
			if (arguments.size() == 1) {
				// /suitcase reload
				reload(sender);
			}
			else {
				// too many arguments
				plugin.msg.send(sender, "error.argument.amount", "command", "reload");
				plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
			}
		}
		else if (isAlias("reset", arguments.get(0))) {
			if (arguments.size() == 1) {
				// /suitcase reset
				reset(sender);
			}
			else {
				// too many arguments
				plugin.msg.send(sender, "error.argument.amount", "command", "reset");
				plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
			}
		}
		else {
		// command not found
			plugin.msg.send(sender, "error.command.unknown", "command", full);
			plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
		}
		
		// clear reset variable if another command was entered by that player
		if (!aliases.get("reset").contains(arguments.get(0)) && reset.equals(sender.getName())) {
			reset = ""; 
		}
		
		// always return true
		return true;
	}
	
	/**
	 * Returns true if command contains alias
	 * @param command Suitcase command
	 * @param alias Entered alias
	 */
	private boolean isAlias(String command, String alias) {
		return aliases.get(command).contains(alias.toLowerCase());
	}
	
	/**
	 * Executes /suitcase help
	 * @param sender Command sender
	 */
	public void help(CommandSender sender) {
		String full = "/suitcase help";
		// check permission
		if (plugin.hasPermission(sender.getName(), permission.get("help"))) {
			// send full help
			plugin.msg.send(sender, "help.header", "command", "help");
			// parse permissions for each command
			for (String cmd : aliases.keySet()) {
				if (plugin.hasPermission(sender.getName(), permission.get(cmd)) || plugin.cfg.getBoolean("mechanics.full-help")) {
					plugin.msg.send(sender, "help.info", "object", usage.get(cmd), "info", info.get(cmd));
				}
			}
			plugin.msg.send(sender, "help.optional");
			plugin.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), full);
		}
		else {
			plugin.msg.send(sender, "error.command.deny", "command", full);
			plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), full);
		}
	}

	/**
	 * Executes /suitcase help command
	 * @param sender Command sender
	 * @param command Sub-command
	 */
	public void help(CommandSender sender, String command) {
		String full = "/suitcase help " + command;
		if (plugin.hasPermission(sender.getName(), permission.get("help"))) {
			// send help for given command
			if (plugin.hasPermission(sender.getName(), permission.get(command)) || plugin.cfg.getBoolean("mechanics.full-help")) {
				// send header, usage and aliases
				plugin.msg.send(sender, "help.header", "command", command);
				plugin.msg.send(sender, "help.usage", "usage", usage.get(command));
				plugin.msg.send(sender, "help.aliases", "aliases", Suitcase.getStringFromList(aliases.get(command), ", "));
				// get each argument
				for (String argument : usage.get(command).split(" ")) {
					if (info.containsKey(argument.replaceAll("[\\[\\]]", ""))) {
						plugin.msg.send(sender, "help.info", "object", argument, "info", info.get(argument.replaceAll("[\\[\\]]", "")));
					}
				}
				plugin.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), full);
			}
			else {
				// no permission to that command
				plugin.msg.send(sender, "error.command.deny", "command", full);
				plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), full);
			}
		}
		else {
			plugin.msg.send(sender, "error.command.deny", "command", full);
			plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), full);
		}
	}
	
	/**
	 * Executes /suitcase info
	 * @param sender Command sender
	 */
	public void info(CommandSender sender) {
		if (plugin.hasPermission(sender.getName(), permission.get("info"))) {
			// send plugin info
			plugin.msg.send(sender, "info.header");
			plugin.msg.send(sender, "info.version", "version", plugin.name + " " + plugin.getDescription().getFullName());
			plugin.msg.send(sender, "info.description", "description", plugin.getDescription().getDescription());
			plugin.msg.send(sender, "info.authors", "authors", Suitcase.getStringFromList(plugin.getDescription().getAuthors(), ", "));
			plugin.msg.send(sender, "info.website", "website", plugin.getDescription().getWebsite());
			plugin.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), "/suitcase info");
		}
		else {
			// no permission to info command
			plugin.msg.send(sender, "error.command.deny", "command", "/suitcase info");
			plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase info");
		}
	}
	
	/**
	 * Executes /suitcase rate
	 * @param sender Command sender
	 */
	public void rate(CommandSender sender) {
		rate(sender, sender.getName());
	}
	
	/**
	 * Executes /suitcase rate player
	 * @param sender Command sender
	 * @param target View rating and warnings of target
	 */
	public void rate(CommandSender sender, String target) {
		String full = "/suitcase rate " + target;
		if (plugin.hasPermission(sender.getName(), permission.get("rate"))) {
			// check if rating is enabled
			if (plugin.cfg.getBoolean("rating.enable")) {
				if (plugin.con.isRegistered(target)) {
					// show rating and warnings
					if (sender instanceof Player) {
						// send target's rating and warnings
						if (target.equals(sender.getName())) {
							plugin.msg.send(sender, "rate.header", "player", "Your");
						}
						else {
							plugin.msg.send(sender, "rate.header", "player", target + "'s");
						}
						
						plugin.msg.send(sender, "rate.rating", "rating", String.valueOf(plugin.con.getRating(sender.getName())));
						plugin.msg.send(sender, "rate.warnings", "warnings", String.valueOf(plugin.con.getWarnings(sender.getName())));

						plugin.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), full);
					}
					else {
						// console has no rating
						plugin.msg.send(sender, "error.command.console", "command", full);
						plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), full);
					}
				}
				else {
					// player not registered
					plugin.msg.send(sender, "error.player.name", "player", target);
					plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
				}
			}
			else {
				// rating is not enabled
				plugin.msg.send(sender, "error.command.disabled", "command", full);
				plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), full);
			}
		}
		else {
			// player doesn't have permission
			plugin.msg.send(sender, "error.command.deny", "command", "/suitcase rate");
			plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), full);
		}
	}
	
	/**
	 * Executes /suitcase rate player rating
	 * @param sender Command sender
	 * @param target Player to be rated
	 * @param rating Rating value
	 */
	public void rate(CommandSender sender, String target, int rating) {
		String full = "/suitcase rate " + target + " " + rating;
		if (!target.equals(sender.getName())) {
			// check if player exists
			if (plugin.con.isRegistered(target)) {
				if (rating >= 0 && rating <= plugin.cfg.getInt("rating.maximum")) {
					if (plugin.con.setRating(sender.getName(), target, rating)) {
						plugin.msg.send(sender, "rate.done", "player", target);
						plugin.msg.sendAll("rate",
								"target", target,
								"rating", String.valueOf(rating),
								"sender", sender.getName());
						plugin.event.call("rate " + rating, sender.getName(), target);
						plugin.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), full);
					}
					else {
						// player can't be rated
						plugin.msg.send(sender, "error.player.rate", "player", target);
						plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), full);
					}
				}
				else {
					// rating out of range
					plugin.msg.send(sender, "error.argument.rating", "rating", String.valueOf(rating));
					plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
				}
			}
			else {
				// player doesn't exist
				plugin.msg.send(sender, "error.player.name", "player", target);
				plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
			}
		}
		else {
			// player can't rate himself
			plugin.msg.send(sender, "error.player.self");
			plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
		}
	}
	
	/**
	 * Executes /suitcase top
	 * @param sender Command sender
	 */
	public void top(CommandSender sender) {
		String full = "/suitcase top";
		if (plugin.hasPermission(sender.getName(), permission.get("top"))) {
			// check if rating is enabled
			if (plugin.cfg.getBoolean("rating.enable")) {
				plugin.msg.send(sender, "top.header");
				
				// show top 10 players
				ArrayList<String> values = plugin.con.getTopRatings();
				if (values.size() > 0) {
					for (int i = 0; i < values.size(); i++) {
						plugin.msg.send(sender, "top.stats",
								"rank", String.valueOf(i + 1),
								"player", values.get(i),
								"rating", String.valueOf(plugin.con.getRating(values.get(i))),
								"warnings", String.valueOf(plugin.con.getWarnings(values.get(i))));
					}
				}
				else {
					// no registered players
					plugin.msg.send(sender, "stats.empty");
				}
			}
			else {
				plugin.msg.send(sender, "error.command.disabled", "command", "/suitcase top");
				plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase top");
			}
		}
		else {
			// no permission
			plugin.msg.send(sender, "error.command.deny", "command", full);
			plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), full);
		}
	}
	
	/**
	 * Executes /suitcase warn player
	 * @param sender Command sender
	 * @param target Player to be warned
	 */
	public void warn(CommandSender sender, String target) {
		String full = "/suitcase warn " + target;
		if (plugin.hasPermission(sender.getName(), permission.get("warn"))) {
			// we don't have to check if the given player is the sender himself because he has the permission to warn
			// check if warning is enabled
			if (plugin.cfg.getBoolean("warnings.enable")) {
				// check if targeted player exists
				if (plugin.con.isRegistered(target)) {
					if (plugin.con.setWarnings(target, true)) {
						plugin.msg.send(sender, "warn.done", "player", target);
						plugin.msg.sendAll("warn", "target", target);
						plugin.event.call("warn " + plugin.con.getWarnings(target), sender.getName(), target);
						plugin.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), full);
					}
					else {
						// warning failed
						plugin.msg.send(sender, "error.player.warn", "player", target);
						plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), full);
					}
				}
				else {
					// player doesn't exist
					plugin.msg.send(sender, "error.player.name", "player", target);
					plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
				}
			}
			else {
				// warning is not enabled
				plugin.msg.send(sender, "error.command.disabled", "command", "/suitcase warn");
				plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase warn");
			}
		}
		else {
			// player isn't allowed to warn
			plugin.msg.send(sender, "error.command.deny", "command", full);
			plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), full);
		}
	}

	/**
	 * Executes /suitcase forgive player
	 * @param sender Command sender
	 * @param target Player to be warned
	 */
	public void forgive(CommandSender sender, String target) {
		String full = "/suitcase forgive " + target;
		if (plugin.hasPermission(sender.getName(), permission.get("forgive"))) {
			// check if forgiving is enabled
			if (plugin.cfg.getBoolean("warnings.enable")) {
				// check if targeted player exists
				if (plugin.con.isRegistered(target)) {
					if (plugin.con.setWarnings(target, false)) {
						plugin.msg.send(sender, "forgive.done", "player", target);
						plugin.msg.sendAll("forgive", "target", target);
						plugin.event.call("forgive", sender.getName(), target);
						plugin.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), full);
					}
					else {
						// forgiving failed
						plugin.msg.send(sender, "error.player.warn", "player", target);
						plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), full);
					}
				}
				else {
					// player doesn't exist
					plugin.msg.send(sender, "error.player.name", "player", target);
					plugin.log(Action.PLAYER_COMMAND_INVALID, sender.getName(), full);
				}
			}
			else {
				// forgiving is not enabled
				plugin.msg.send(sender, "error.command.disabled", "command", "/suitcase forgive");
				plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase forgive");
			}
		}
		else {
			// player isn't allowed to forgive
			plugin.msg.send(sender, "error.command.deny", "command", full);
			plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), full);
		}
	}

	/**
	 * Executes /suitcase reload
	 * @param sender Command sender
	 */
	public void reload(CommandSender sender) {
		if (plugin.hasPermission(sender.getName(), permission.get("reload"))) {
			// be sure that we reload the plugin after we sent a message to console and before we send a message to the executing player or console
			plugin.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), "/suitcase reload");
			
			// reload plugin
			if (plugin.reload()) {
				plugin.msg.send(sender, "reload.done");
				plugin.msg.sendAll("reload");
			}
			else {
				plugin.log(Action.PLAYER_COMMAND_ERROR, sender.getName(), "/suitcase reload", "reloadFailed");
				plugin.msg.send(sender, "error.command.internal", "command", "/suitcase reload");
			}
			
		}
		else {
			// no permission
			plugin.msg.send(sender, "error.command.deny", "command", "/suitcase reload");
			plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase reload");
		}
	}
	
	/** Plugin reset confirmation */
	private String reset = "";
	
	/**
	 * Executes /suitcase reset
	 * @param sender Command sender
	 */
	public void reset(CommandSender sender) {
		if (plugin.hasPermission(sender.getName(), permission.get("reset"))) {
			if (reset.equals(sender.getName())) {
				// delete all files
				if (plugin.reset()) {
					plugin.log(Action.PLAYER_COMMAND_EXECUTED, sender.getName(), "/suitcase reset");
					plugin.msg.send(sender, "reset.done");
					plugin.msg.sendAll("reset");
					reset = "";
				}
				else {
					plugin.log(Action.PLAYER_COMMAND_ERROR, sender.getName(), "/suitcase reset", "resetFailed");
					plugin.msg.send(sender, "error.command.internal", "command", "/suitcase reset");
				}
			}
			else {
				reset = sender.getName();
				plugin.msg.send(sender, "reset.confirm");
			}
		}
		else {
			// no permission
			plugin.msg.send(sender, "error.command.deny", "command", "/suitcase reset");
			plugin.log(Action.PLAYER_COMMAND_DENIED, sender.getName(), "/suitcase reset");
		}
	}
}
