package me.eighth.suitcase.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import me.eighth.suitcase.Suitcase;

public class SuitcaseConsole {
	
	private Suitcase plugin;
	private final Logger mcLogger = Logger.getLogger("Minecraft");
	private Map<actionType, String> dependences = new HashMap<actionType, String>();
	
	public enum actionType {
		
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
		
		// config property handling
		PROPERTY_MISSING,
		PROPERTY_REDUNDANT,
		
		// file handling
		FILE_NOT_FOUND,
		FILE_SAVE_ERROR,
		
		// debug
		DEBUG,
		ARGUMENTS_INVALID,
		TYPE_NOT_HANDLED
	}
	
	public SuitcaseConsole(Suitcase plugin) {
		this.plugin = plugin;
		
		// set dependences and always show errors
		dependences.put(actionType.PLAYER_COMMAND_EXECUTED, "command");
		dependences.put(actionType.PLAYER_COMMAND_DENIED, "command");
		dependences.put(actionType.PLAYER_COMMAND_INVALID, "command");
		
		dependences.put(actionType.PROPERTY_MISSING, "file"); // error for 'config.yml' will always be shown
		dependences.put(actionType.PROPERTY_REDUNDANT, "file");
		dependences.put(actionType.FILE_NOT_FOUND, "file");
		
		dependences.put(actionType.DEBUG, "debug");
	}

	private boolean checkArguments(actionType action, ArrayList<String> arguments, int size) {
		if (arguments.size() == size) {
			if (dependences.containsKey(action)) {
				if (plugin.config.data != null && plugin.config.data.contains("log.console." + dependences.get(action))) {
					if (plugin.config.data.getBoolean("log.console." + dependences.get(action))) {
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
			sendAction(actionType.ARGUMENTS_INVALID, new ArrayList<String>(Arrays.asList(action.toString(), plugin.messages.getString(arguments, true))));
			return false;
		}
	}
	
	public void sendAction(actionType action) {
		sendAction(action, new ArrayList<String>());
	}
	
	public void sendAction(actionType action, ArrayList<String> arguments) {
		// log actions to console
		switch (action) {
		
			// no arguments
		case PLUGIN_ENABLE_START:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.tag + plugin.name + " " + plugin.getDescription().getFullName() + " by " + plugin.messages.getString(plugin.getDescription().getAuthors(), true) + " enabling...");
			}
			break;
			// argument format 0 -> 'errorName'
		case PLUGIN_ENABLE_ERROR:
			if (checkArguments(action, arguments, 1)) {
				mcLogger.severe(plugin.tag + "Error '" + arguments.get(0) + "' occured while enabling plugin!");
			}
			break;
			// no arguments
		case PLUGIN_ENABLE_FINISH:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.tag + "Suitcase successfully enabled.");
			}
			break;

			
			// no arguments
		case PLUGIN_RELOAD_START:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.tag + "Reloading plugin...");
			}
			break;
			// argument format 0 -> 'errorName'
		case PLUGIN_RELOAD_ERROR:
			if (checkArguments(action, arguments, 1)) {
				mcLogger.severe(plugin.tag + "Error '" + arguments.get(0) + "' occured while reloading plugin!");
			}
			break;
			// no arguments
		case PLUGIN_RELOAD_FINISH:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.tag + "Suitcase successfully reloaded.");
			}
			break;
				
			
			// no arguments
		case PLUGIN_DISABLE_START:
			if (checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.tag + "Disabling plugin...");
			}
			break;
			// argument format 0 -> 'errorName'
		case PLUGIN_DISABLE_ERROR:
			if (checkArguments(action, arguments, 1)) {
				mcLogger.severe(plugin.tag + "Error '" + arguments.get(0) + "' occured while disabling plugin!");
			}
			break;
			// no arguments
		case PLUGIN_DISABLE_FINISH:
			if (!checkArguments(action, arguments, 0)) {
				mcLogger.info(plugin.tag + "Suitcase successfully disabled.");
			}
			break;
			

			// argument format 0 -> 'Player'
			// argument format 1 -> '/suitcase help rate'
		case PLAYER_COMMAND_EXECUTED:
			if (checkArguments(action, arguments, 2)) {
				mcLogger.info(plugin.cmd + "'" + arguments.get(0) + "' used command '" + arguments.get(1) + "'.");
			}
			break;
			// argument format ^
		case PLAYER_COMMAND_DENIED:
			if (checkArguments(action, arguments, 2)) {
				mcLogger.info(plugin.cmd + "'" + arguments.get(0) + "' was denied command '" + arguments.get(1) + "'.");
			}
			break;
			// argument format ^
		case PLAYER_COMMAND_INVALID:
			if (checkArguments(action, arguments, 2)) {
				mcLogger.info(plugin.cmd + "'" + arguments.get(0) + "' tried invalid command '" + arguments.get(1) + "'.");
			}
			break;
			// argument format ^
			// argument format 2 -> 'errorName'
		case PLAYER_COMMAND_ERROR:
			if (checkArguments(action, arguments, 3)) {
				mcLogger.warning(plugin.cmd + "'" + arguments.get(0) + "' caused error '" + arguments.get(2) + "' by executing '" + arguments.get(1) + "'!");
			}
			break;
			
			
			// argument format 0 -> 'property.path'
			// argument format 1 -> 'filename.ext'
			// argument format 2 -> 'default value'
		case PROPERTY_MISSING:
			if (checkArguments(action, arguments, 3)) {
				mcLogger.warning(plugin.tag + "Missing property '" + arguments.get(0) + "' in '" + arguments.get(1) + "'! Set to default: '" + arguments.get(2) + "'.");
			}
			break;
			// argument format ^
		case PROPERTY_REDUNDANT:
			if (checkArguments(action, arguments, 3)) {
				mcLogger.warning(plugin.tag + "Redundant property '" + arguments.get(0) + "' in '" + arguments.get(1) + "'! Removed value '" + arguments.get(2) + "'.");
			}
			break;
			
			// argument format 0 -> 'filename.ext'
		case FILE_NOT_FOUND:
			if (checkArguments(action, arguments, 1)) {
				mcLogger.warning(plugin.tag + "File '" + arguments.get(0) + "' was not found. Attempting to create default...");
			}
			break;
			// argument format ^
			// argument format 1 -> 'error type'
		case FILE_SAVE_ERROR:
			if (checkArguments(action, arguments, 2)) {
				mcLogger.severe(plugin.tag + "Error while saving '" + arguments.get(0) + "'!");
			}
			break;
			
			// argument format 0 -> 'ACTION_TYPE' 
			// argument format 1 -> 'argument1, argument2'
		case ARGUMENTS_INVALID:
			if (checkArguments(action, arguments, 2)) { // this could cause an infinite loop of errors
				mcLogger.severe(plugin.tag + "Action '" + arguments.get(0) + "' was passed an invalid amount of arguments: '" + arguments.get(1) + "'!");
			}
			plugin.disable();
			break;
			// argument format ^
		case TYPE_NOT_HANDLED:
			if (checkArguments(action, arguments, 2)) {
				mcLogger.severe(plugin.tag + "Type '" + action.toString() + "' was not handled! Arguments: '" + plugin.messages.getString(arguments, true) + "'");
			}
			plugin.disable();
			break;
			
			// argument format * -> 'text'
		case DEBUG:
			if (checkArguments(action, arguments, arguments.size())) {
				mcLogger.info(plugin.tag + "### DEBUG ###");
				for (int i = 0; i < arguments.size(); i++) {
					mcLogger.info(plugin.tag + "Line " + (i + 1) + ": '" + arguments.get(i) + "'");
				}
			}
			break;
			
			// type was not handled
		default:
			arguments.add(0, action.toString());
			sendAction(actionType.TYPE_NOT_HANDLED, arguments);
			break;
		}
	}
	
}
