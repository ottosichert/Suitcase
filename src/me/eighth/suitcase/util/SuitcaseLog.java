package me.eighth.suitcase.util;

import java.util.logging.Logger;

import me.eighth.suitcase.Suitcase;

import org.bukkit.plugin.PluginDescriptionFile;

public class SuitcaseLog {
	
	// define variables
	private final Logger mcLogger = Logger.getLogger("Minecraft");
	private PluginDescriptionFile pdf = Suitcase.plugin.getDescription();
	private String tag = "[" + pdf.getName() + "] "; // this should be '[Suitcase] '
	
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
		PLAYER_COMMAND_EXECUTE,
		PLAYER_COMMAND_DENY,
		PLAYER_COMMAND_ERROR,
		
		// file handling
		FILE_NOT_FOUND,
		FILE_CREATE_ERROR,
		FILE_PROPERTY_MISSING,
		FILE_PROPERTY_BAD_TYPE,
		
		// other actions
		VARIABLE_NOT_FOUND,
		TYPE_NOT_HANDLED,
		
	}
	
	public void logAction(actionType action, Object...arguments) {
		// TODO: call console, file and database log-events
		switch (action) {
		
			// no arguments
		case PLUGIN_ENABLE_START:
			mcLogger.info(tag + Suitcase.version + " by " + pdf.getAuthors().toString().replaceAll("^[\\[\\]]|[\\[\\]]$", "") + " enabling...");
			break;
			// argument format 0 -> 'errorName'
		case PLUGIN_ENABLE_ERROR:
			mcLogger.severe(tag + "Error '" + arguments[0] + "' occured while enabling plugin!");
			break;
		case PLUGIN_ENABLE_FINISH:
			mcLogger.info(tag + "Suitcase successfully enabled.");
			break;

			
			// no arguments
		case PLUGIN_RELOAD_START:
			mcLogger.info(tag + "Reloading plugin...");
			break;
			// argument format 0 -> 'errorName'
		case PLUGIN_RELOAD_ERROR:
			mcLogger.severe(tag + "Error '" + arguments[0] + "' occured while reloading plugin!");
			break;
		case PLUGIN_RELOAD_FINISH:
			mcLogger.info(tag + "Successfully reloaded.");
			break;
				
			
			// no arguments
		case PLUGIN_DISABLE_START:
			mcLogger.info(tag + "Disabling plugin...");
			break;
			// argument format 0 -> 'errorName'
		case PLUGIN_DISABLE_ERROR:
			mcLogger.severe(tag + "Error '" + arguments[0] + "' occured while disabling plugin!");
			break;
		case PLUGIN_DISABLE_FINISH:
			mcLogger.info(tag + "Suitcase successfully disabled.");
			break;
			
			
			// argument format 0 -> 'Player'
			// argument format 1 -> '/suitcase help rate'
		case PLAYER_COMMAND_EXECUTE:
			mcLogger.info(tag + "[PLAYER_COMMAND] " + arguments[0] + " executed '" + arguments[1] + "'.");
			break;
			// argument format ^
		case PLAYER_COMMAND_DENY:
			mcLogger.info(tag + "[PLAYER_COMMAND] " + arguments[0] + " was denied '" + arguments[1] + "'!");
			break;
			// argument format ^
			// argument format 2 -> 'errorName'
		case PLAYER_COMMAND_ERROR:
			mcLogger.warning(tag + "[PLAYER_COMMAND] " + arguments[0] + " caused error '" + arguments[2] + "' by executing '" + arguments[1] + "'!");
			break;
			
			
			// argument format 0 -> '/directory' without ~/plugins/Suitcase
			// argument format 1 -> 'filename.ext'
		case FILE_NOT_FOUND:
			mcLogger.warning(tag + "Cannot find file '" + arguments[0] + "/" + arguments[1] + "'! Attempting to create '" + arguments[1] + "'...");
			break;
			// argument format ^
			// argument format 2 -> 'errorName'
		case FILE_CREATE_ERROR:
			mcLogger.severe(tag + "Error '" + arguments[2] + "' occured while creating file '" + arguments[0] + "/" + arguments[1] + "'!");
			break;
			// argument format 0 -> 'property.path'
			// argument format 1 -> 'default value'
		case FILE_PROPERTY_MISSING:
			mcLogger.warning(tag + "Missing property '" + arguments[0] + "'! Set to default: " + arguments[1]);
			break;
			// argument format 0 -> 'property.path.int'
			// argument format 1 -> 'boolean'
			// argument format 2 -> 'int'
		case FILE_PROPERTY_BAD_TYPE:
			mcLogger.warning(tag + "Bad type of '" + arguments[0] + "'! Found " + arguments[1] + " instead of " + arguments[2]);
			break;
			
			
			// argument format 0 -> 'variable-name'
		case VARIABLE_NOT_FOUND:
			mcLogger.severe(tag + "Unknown variable '" + arguments[0] + "'!");
			break;
			// argument format 0 -> 'BLA_ENUM_ACTION'
			// argument format . -> 'Object...arguments' of original action
		case TYPE_NOT_HANDLED:
			// return {'arg1', 'arg2'}
			String args = "{'";
			if (arguments.length > 1) {
				args += (String) arguments[1];
				if (arguments.length > 2) {
					for (int i = 2; i < arguments.length; i++) {
						args += "', '" + arguments[i];
					}
				}
			}
			args += "'}";
			mcLogger.severe(tag + "Type " + arguments[0] + " was not handled! Arguments: " + args + "");
			break;
			
			
			// if type was not handled...
		default:
			logAction(actionType.TYPE_NOT_HANDLED, action.toString(), arguments);
			break;
		
		}
	}
	
}
