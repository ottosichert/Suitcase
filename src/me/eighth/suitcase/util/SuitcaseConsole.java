package me.eighth.suitcase.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import me.eighth.suitcase.Suitcase;

public class SuitcaseConsole {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/** Minecraft logger */
	private final Logger mcLogger = Logger.getLogger("Minecraft");
	
	/** Message types linked to logger settings */
	private Map<Action, String> dependences = new HashMap<Action, String>();
	
	/** Provides different plugin actions to be logged */
	public enum Action {
		
		// plugin startup
		PLUGIN_ENABLE_START,
		PLUGIN_ENABLE_FINISH,
		PLUGIN_ENABLE_ERROR,
		
		// plugin reload
		PLUGIN_RELOAD_START,
		PLUGIN_RELOAD_FINISH,
		PLUGIN_RELOAD_ERROR,
		
		// plugin unload
		PLUGIN_DISABLE_START,
		PLUGIN_DISABLE_FINISH,
		PLUGIN_DISABLE_ERROR,
		
		// player command
		PLAYER_COMMAND_EXECUTED,
		PLAYER_COMMAND_DENIED,
		PLAYER_COMMAND_INVALID,
		PLAYER_COMMAND_ERROR,
		
		// registration
		PLAYER_REGISTER,
		PLAYER_UNREGISTER,
		
		// config property/path handling
		PROPERTY_MISSING,
		PROPERTY_REDUNDANT,
		
		// file handling
		FILE_NOT_FOUND,
		FILE_SAVE_ERROR,
		
		// internal objects
		RESET,
		INIT_ERROR,
		FREE_ERROR,
		
		// debug
		DEBUG,
		ARGUMENTS_INVALID,
		TYPE_NOT_HANDLED
	}
	
	/** Minecraft console logger with different levels */
	public SuitcaseConsole(Suitcase plugin) {
		this.plugin = plugin;
		
		// set dependences and always show severe errors and default plugin messages
		dependences.put(Action.PLAYER_COMMAND_EXECUTED, "command");
		dependences.put(Action.PLAYER_COMMAND_DENIED, "command");
		dependences.put(Action.PLAYER_COMMAND_INVALID, "command");
		dependences.put(Action.PLAYER_REGISTER, "command");
		dependences.put(Action.PLAYER_UNREGISTER, "command");
		
		dependences.put(Action.PROPERTY_MISSING, "file"); // errors with 'config.yml' will always be shown
		dependences.put(Action.PROPERTY_REDUNDANT, "file");
		dependences.put(Action.FILE_NOT_FOUND, "file");
		
		dependences.put(Action.DEBUG, "debug");
	}

	private boolean checkArguments(Action action, ArrayList<String> arguments, int size) {
		if (arguments.size() == size) {
			if (dependences.containsKey(action)) {
				if (plugin.cfg.data != null && plugin.cfg.data.contains("log.console." + dependences.get(action))) {
					if (plugin.cfg.data.getBoolean("log.console." + dependences.get(action))) {
						return true;
					}
					else {
						return false;
					}
				}
				else {
					return true;
				}
			}
			else {
				return true;
			}
		}
		else {
			log(Action.ARGUMENTS_INVALID, new ArrayList<String>(Arrays.asList(action.toString(), plugin.getString(arguments, true))));
			return false;
		}
	}
	

	public boolean log(Action action) {
		return log(action, new ArrayList<String>());
	}
	
	public boolean log(Action action, String...arguments) {
		return log(action, new ArrayList<String>(Arrays.asList(arguments)));
	}
	
	public boolean log(Action action, ArrayList<String> arguments) {
		// log actions to console
		switch (action) {
		
			// no arguments
		case PLUGIN_ENABLE_START:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.pluginTag + plugin.name + " " + plugin.getDescription().getFullName() + " by " + plugin.getString(plugin.getDescription().getAuthors(), true) + " enabling...");
			}
			return true;
			// argument format 0 -> 'errorName'
		case PLUGIN_ENABLE_ERROR:
			if (checkArguments(action, arguments, 1)) {
				mcLogger.severe(plugin.pluginTag + "Error '" + arguments.get(0) + "' occured while enabling plugin!");
			}
			return true;
			// no arguments
		case PLUGIN_ENABLE_FINISH:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.pluginTag + "Suitcase successfully enabled.");
			}
			return true;

			
			// no arguments
		case PLUGIN_RELOAD_START:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.pluginTag + "Reloading plugin...");
			}
			return true;
			// argument format 0 -> 'errorName'
		case PLUGIN_RELOAD_ERROR:
			if (checkArguments(action, arguments, 1)) {
				mcLogger.severe(plugin.pluginTag + "Error '" + arguments.get(0) + "' occured while reloading plugin!");
			}
			return true;
			// no arguments
		case PLUGIN_RELOAD_FINISH:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.pluginTag + "Suitcase successfully reloaded.");
			}
			return true;
				
			
			// no arguments
		case PLUGIN_DISABLE_START:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.pluginTag + "Disabling plugin...");
			}
			return true;
			// argument format 0 -> 'errorName'
		case PLUGIN_DISABLE_ERROR:
			if (checkArguments(action, arguments, 1)) {
				mcLogger.severe(plugin.pluginTag + "Error '" + arguments.get(0) + "' occured while disabling plugin!");
			}
			return true;
			// no arguments
		case PLUGIN_DISABLE_FINISH:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.pluginTag + "Suitcase successfully disabled.");
			}
			return true;
			

			// argument format 0 -> 'Player'
			// argument format 1 -> '/suitcase help rate'
		case PLAYER_COMMAND_EXECUTED:
			if (checkArguments(action, arguments, 2)) {
				String tag = "";
				if (arguments.get(0).equals("CONSOLE")) {
					tag = plugin.consoleTag + "Console";
				}
				else {
					tag = plugin.playerTag + "'" + arguments.get(0) + "'";
				}
				mcLogger.info(tag + " used command '" + arguments.get(1) + "'.");
			}
			return true;
			// argument format ^
		case PLAYER_COMMAND_DENIED:
			if (checkArguments(action, arguments, 2)) {
				String tag = "";
				if (arguments.get(0).equals("CONSOLE")) {
					tag = plugin.consoleTag + "Console";
				}
				else {
					tag = plugin.playerTag + "'" + arguments.get(0) + "'";
				}
				mcLogger.info(tag + " was denied command '" + arguments.get(1) + "'.");
			}
			return true;
			// argument format ^
		case PLAYER_COMMAND_INVALID:
			if (checkArguments(action, arguments, 2)) {
				String tag = "";
				if (arguments.get(0).equals("CONSOLE")) {
					tag = plugin.consoleTag + "Console";
				}
				else {
					tag = plugin.playerTag + "'" + arguments.get(0) + "'";
				}
				mcLogger.info(tag + " tried invalid command '" + arguments.get(1) + "'.");
			}
			return true;
			// argument format ^
			// argument format 2 -> 'errorName'
		case PLAYER_COMMAND_ERROR:
			if (checkArguments(action, arguments, 3)) {
				String tag = "";
				if (arguments.get(0).equals("CONSOLE")) {
					tag = plugin.consoleTag + "Console";
				}
				else {
					tag = plugin.playerTag + "'" + arguments.get(0) + "'";
				}
				mcLogger.severe(tag + " caused error '" + arguments.get(2) + "' by executing '" + arguments.get(1) + "'!");
			}
			return true;
			

			// argument format 0 -> 'player'
		case PLAYER_REGISTER:
			if (checkArguments(action, arguments, 1)) {
				mcLogger.info(plugin.pluginTag + "Player '" + arguments.get(0) + "' was successfully registered!");
			}
			return true;
			// argument format ^
		case PLAYER_UNREGISTER:
			if (checkArguments(action, arguments, 1)) {
				mcLogger.info(plugin.pluginTag + "Player '" + arguments.get(0) + "' was successfully unregistered!");
			}
			return true;
			
			
			// argument format 0 -> 'path.property'
			// argument format 1 -> 'filename.ext'
			// argument format 2 -> 'value'
		case PROPERTY_MISSING:
			if (checkArguments(action, arguments, 3)) {
				mcLogger.warning(plugin.pluginTag + "Missing property '" + arguments.get(0) + "' in '" + arguments.get(1) + "'! Set to default: '" + arguments.get(2) + "'.");
			}
			return true;
			// argument format ^
		case PROPERTY_REDUNDANT:
			if (checkArguments(action, arguments, 3)) {
				mcLogger.warning(plugin.pluginTag + "Redundant property '" + arguments.get(0) + "' in '" + arguments.get(1) + "'! Removed value '" + arguments.get(2) + "'.");
			}
			return true;
			
			
			// argument format 0 -> 'filename.ext'
		case FILE_NOT_FOUND:
			if (checkArguments(action, arguments, 1)) {
				mcLogger.warning(plugin.pluginTag + "File '" + arguments.get(0) + "' was not found. Attempting to create default...");
			}
			return true;
			// argument format ^
			// argument format 1 -> 'class'
		case FILE_SAVE_ERROR:
			if (checkArguments(action, arguments, 2)) {
				mcLogger.severe(plugin.pluginTag + "Error '" + arguments.get(1) + "' occured while saving '" + arguments.get(0) + "'!");
			}
			return true;
			

			// no arguments
		case RESET:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.pluginTag + "Configuration and player data was reset.");
			}
			return true;
			// argument format 0 -> 'init-class'
			// argument format 1 -> 'error type'
		case INIT_ERROR:
			if (checkArguments(action, arguments, 2)) {
				mcLogger.severe(plugin.pluginTag + "Error '" + arguments.get(1) + "' occured while initiating class '" + arguments.get(0) + "'!");
			}
			return true;
			// argument format 0 -> 'free-class'
			// argument format 1 -> 'error type'
		case FREE_ERROR:
			if (checkArguments(action, arguments, 2)) {
				mcLogger.severe(plugin.pluginTag + "Error '" + arguments.get(1) + "' occured while freeing class '" + arguments.get(0) + "'!");
			}
			return true;
			
			
			// argument format * -> 'text'
		case DEBUG:
			if (checkArguments(action, arguments, arguments.size())) {
				for (int i = 0; i < arguments.size(); i++) {
					mcLogger.info(plugin.pluginTag + "### DEBUG: '" + arguments.get(i) + "' ###");
				}
			}
			return true;
			// argument format 0 -> 'ACTION_TYPE' 
			// argument format 1 -> 'argument1, argument2'
		case ARGUMENTS_INVALID:
			if (checkArguments(action, arguments, 2)) {
				mcLogger.severe(plugin.pluginTag + "Action '" + arguments.get(0) + "' was passed an invalid amount of arguments: '" + arguments.get(1) + "'!");
			}
			plugin.disable();
			return false;
			// argument format ^
		case TYPE_NOT_HANDLED:
			if (checkArguments(action, arguments, arguments.size())) {
				mcLogger.severe(plugin.pluginTag + "Type '" + arguments.get(0) + "' was not handled! Arguments: '" + plugin.getString(arguments, true) + "'");
			}
			plugin.disable();
			return false;
			
			
			// type was not handled
		default:
			arguments.add(0, action.toString());
			return log(Action.TYPE_NOT_HANDLED, arguments);
		}
	}
	
}
