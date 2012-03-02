package me.eighth.suitcase.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import me.eighth.suitcase.Suitcase;

public class SuitcaseConsole {
	
	/** Provides different plugin actions to be logged */
	public enum Action {
		/** Plugin enabling finished. No arguments */
		PLUGIN_ENABLE,
		/** Plugin reloading finished. No arguments */
		PLUGIN_RELOAD,
		/** Plugin disabling finished. No arguments */
		PLUGIN_DISABLE,
		
		
		/** Player successfully executed a command. Arguments: player name, command */
		PLAYER_COMMAND_EXECUTED,
		/** Player was denied a command. Arguments: player name, command */
		PLAYER_COMMAND_DENIED,
		/** Player tried an invalid command. Arguments: player name, command */
		PLAYER_COMMAND_INVALID,
		/** Error occurred while executing a command. Arguments: player name, command, error name */
		PLAYER_COMMAND_ERROR,
		
		
		/** Player was registered. Arguments: player name */
		PLAYER_REGISTER,
		/** Player was unregistered. Arguments: player name */
		PLAYER_UNREGISTER,
		
		
		/** Event successfully executed. Arguments: event name, action */
		EVENT_EXECUTED,
		
		
		/** Property missing in configuration file. Argument: key, file path, value */
		PROPERTY_MISSING,
		/** Redundant property found. Arguments: key, file path, value */
		PROPERTY_REDUNDANT,
		/** Property option or value doesn't match. Arguments: key, file path, value */
		PROPERTY_BAD_TYPE,
		
		
		/** Configuration file is missing. Arguments: file name */
		FILE_NOT_FOUND,
		/** Error occurred while saving file. Arguments: file name, error class */
		FILE_SAVE_ERROR,
		
		/** No log method found. No arguments */
		LOG_ERROR,
		/** Error occurred while initializing class. Arguments: init class */
		INIT_ERROR,
		/** Error occurred while freeing class. Arguments: free class */
		FREE_ERROR,
		/** Spotted invalid amount of arguments. Arguments: action, arguments */
		ARGUMENTS_INVALID,
		
		
		/** Reset Suitcase. No arguments */
		RESET,
		/** Send debug information. Arguments: information */
		DEBUG
	}
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/** Minecraft logger */
	private final Logger mcLogger = Logger.getLogger("Minecraft");
	
	/** Message types linked to logger settings */
	private Map<Action, String> dependences = new HashMap<Action, String>();
	
	/**
	 * Minecraft console logger with different levels
	 * @param plugin Instance of Suitcase
	 */
	public SuitcaseConsole(Suitcase plugin) {
		this.plugin = plugin;
		
		// set dependences and always show severe errors and default plugin messages
		dependences.put(Action.PLAYER_COMMAND_EXECUTED, "command");
		dependences.put(Action.PLAYER_COMMAND_DENIED, "command");
		dependences.put(Action.PLAYER_COMMAND_INVALID, "command");
		
		dependences.put(Action.EVENT_EXECUTED, "event");

		dependences.put(Action.RESET, "debug");
		dependences.put(Action.DEBUG, "debug");
	}
	
	/**
	 * Checks if the amount of given arguments is equal to the amount of needed arguments and checks if the given action is enabled
	 * @param action Action type to be logged
	 * @param arguments Action arguments
	 * @param size Expected amount of arguments
	 */
	private boolean checkArguments(Action action, ArrayList<String> arguments, int size) {
		if (arguments.size() == size) {
			if (dependences.containsKey(action)) {
				// only send if action is enabled
				if (plugin.cfg.getBoolean("log.console." + dependences.get(action))) {
					return true;
				}
			}
		}
		else {
			log(Action.ARGUMENTS_INVALID, new ArrayList<String>(Arrays.asList(action.toString(), Suitcase.getStringFromList(arguments, ", "))));
		}
		return false;
	}
	
	/**
	 * Logs an action to console
	 * @param action Action type to be logged
	 * @param arguments Action arguments
	 */
	public boolean log(Action action, ArrayList<String> arguments) {
		// log actions to console
		switch (action) {
			
		case PLUGIN_ENABLE:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.pluginTag + plugin.name + " " + plugin.getDescription().getFullName() + " by " + Suitcase.getStringFromList(plugin.getDescription().getAuthors(), ", ") + " was enabled.");
			}
			return true;
		case PLUGIN_RELOAD:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.pluginTag + "Suitcase was reloaded.");
			}
			return true;
		case PLUGIN_DISABLE:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.pluginTag + "Suitcase was disabled.");
			}
			return true;
			
			
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
			
			
		case PLAYER_REGISTER:
			if (checkArguments(action, arguments, 1)) {
				mcLogger.info(plugin.pluginTag + "Player '" + arguments.get(0) + "' was successfully registered!");
			}
			return true;
		case PLAYER_UNREGISTER:
			if (checkArguments(action, arguments, 1)) {
				mcLogger.info(plugin.pluginTag + "Player '" + arguments.get(0) + "' was successfully unregistered!");
			}
			return true;
			

		case EVENT_EXECUTED:
			if (checkArguments(action, arguments, 2)) {
				mcLogger.info(plugin.pluginTag + "Event '" + arguments.get(0) + "' was successfully executed by matching action '" + arguments.get(1) + "'!");
			}
			return true;
			
			
		case PROPERTY_MISSING:
			if (checkArguments(action, arguments, 3)) {
				mcLogger.warning(plugin.pluginTag + "Missing property '" + arguments.get(0) + "' in '" + arguments.get(1) + "'! Set to default: '" + arguments.get(2) + "'.");
			}
			return true;
		case PROPERTY_REDUNDANT:
			if (checkArguments(action, arguments, 3)) {
				mcLogger.warning(plugin.pluginTag + "Redundant property '" + arguments.get(0) + "' in '" + arguments.get(1) + "'! Removed value '" + arguments.get(2) + "'.");
			}
			return true;
		case PROPERTY_BAD_TYPE:
			if (checkArguments(action, arguments, 3)) {
				mcLogger.warning(plugin.pluginTag + "Bad type of property '" + arguments.get(0) + "' in '" + arguments.get(1) + "'! Ignoring value '" + arguments.get(2) + "'.");
			}
			return true;
			
			
		case FILE_NOT_FOUND:
			if (checkArguments(action, arguments, 1)) {
				mcLogger.warning(plugin.pluginTag + "File '" + arguments.get(0) + "' was not found. Attempting to create default...");
			}
			return true;
		case FILE_SAVE_ERROR:
			if (checkArguments(action, arguments, 2)) {
				mcLogger.severe(plugin.pluginTag + "Error '" + arguments.get(1) + "' occured while saving '" + arguments.get(0) + "'!");
			}
			return true;
			

		case LOG_ERROR:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.severe(plugin.pluginTag + "No log method was found! Temporary enabling YAML-file logging...");
			}
			return true;
		case INIT_ERROR:
			if (checkArguments(action, arguments, 1)) {
				mcLogger.severe(plugin.pluginTag + "Error occured while initiating class '" + arguments.get(0) + "'!");
			}
			return true;
		case FREE_ERROR:
			if (checkArguments(action, arguments, 1)) {
				mcLogger.severe(plugin.pluginTag + "Error occured while freeing class '" + arguments.get(0) + "'!");
			}
			return true;
		case ARGUMENTS_INVALID:
			if (checkArguments(action, arguments, 2)) {
				mcLogger.severe(plugin.pluginTag + "Action '" + arguments.get(0) + "' was passed an invalid amount of arguments: '" + arguments.get(1) + "'!");
			}
			return false;
			
			
		case RESET:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.pluginTag + "Configuration and player data was reset.");
			}
			return true;
		case DEBUG:
			if (checkArguments(action, arguments, arguments.size())) {
				for (int i = 0; i < arguments.size(); i++) {
					mcLogger.info(plugin.pluginTag + "### DEBUG(" + (i + 1) + "/" + arguments.size() + "): '" + arguments.get(i) + "' ###");
				}
			}
			return true;
			
			
		// type was not handled
		default:
			mcLogger.severe(plugin.pluginTag + "Action '" + action.toString() + "' was not handled! Arguments: '" + Suitcase.getStringFromList(arguments, ",") + "'");
		}
		return false;
	}
	
}
