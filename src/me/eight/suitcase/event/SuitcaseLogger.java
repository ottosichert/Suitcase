package me.eight.suitcase.event;

import java.util.logging.Logger;

import me.eight.suitcase.Suitcase;

import org.bukkit.plugin.PluginDescriptionFile;

public class SuitcaseLogger {

	// define variables
	private final Logger mcLogger = Logger.getLogger("Minecraft");
	private PluginDescriptionFile pdf = Suitcase.plugin.getDescription();
	private String tag = "[" + pdf.getName() + "] ";
	
	// system message types
	public enum SystemType {
		PLUGIN_ENABLED,
		PLUGIN_DISABLED,
		PLUGIN_RELOADED
	}
	
	// default message types
	public enum MessageType {
		PLAYER_COMMAND
	}
	
	// warning types
	public enum WarningType {
		CONFIG_FILE_NOT_FOUND,
		MESSAGE_FILE_NOT_FOUND
	}
	
	// error types
	public enum ErrorType {
		COLOR_NOT_FOUND,
		COLOR_TYPE_NOT_FOUND,
		SYSTEM_TYPE_NOT_FOUND,
		MESSAGE_TYPE_NOT_FOUND,
		WARNING_TYPE_NOT_FOUND,
		ERROR_TYPE_NOT_FOUND
	}
	
	// send system message (info level)
	public void sendSystem (SystemType type) {
		switch (type) {
		case PLUGIN_ENABLED:
			mcLogger.info(tag + pdf.getVersion() + "by" + pdf.getAuthors().toString().replaceAll("^[\\[\\]]|[\\[\\]]$", " ") + "enabled.");
			break;
		case PLUGIN_DISABLED:
			mcLogger.info(tag + "disabled.");
			break;
		case PLUGIN_RELOADED:
			mcLogger.info(tag + "reloaded.");
			break;
		default:
			sendError(ErrorType.SYSTEM_TYPE_NOT_FOUND, type.toString());
		}
	}
	
	// send default message (info level)
	public void sendMessage(MessageType type, String argument) {
		switch (type) {
		case PLAYER_COMMAND:
			mcLogger.info(tag + "Executing command for " + argument);
			break;
		default:
			sendError(ErrorType.MESSAGE_TYPE_NOT_FOUND, type.toString());
		}
		
	}
	
	// send warning message (warning level)
	public void sendWarning(WarningType type, String argument) {
		switch (type) {
		case CONFIG_FILE_NOT_FOUND:
			mcLogger.warning(tag + "Cannot find plugin.yml! Creating default...");
			// TODO: scConfig.setDefaultPlugin();
			break;
		case MESSAGE_FILE_NOT_FOUND:
			mcLogger.warning(tag + "Cannot find messages.yml! Creating default...");
			// TODO: scConfig.setDefaultMessage();
			break;
		default:
			sendError(ErrorType.WARNING_TYPE_NOT_FOUND, type.toString());
		}
	}
	
	// send error message (severe level)
	public void sendError (ErrorType type, String argument) {
		switch (type) {
		case COLOR_NOT_FOUND:
			mcLogger.severe(tag + "Cannot find color: " + argument);
			break;
		case COLOR_TYPE_NOT_FOUND:
			mcLogger.severe(tag + "Cannot find color type: " + argument);
			break;
		case SYSTEM_TYPE_NOT_FOUND:
			mcLogger.severe(tag + "Cannot find system message type '" + argument + "'!");
			break;
		case MESSAGE_TYPE_NOT_FOUND:
			mcLogger.severe(tag + "Cannot find message type '" + argument + "'!");
			break;
		case WARNING_TYPE_NOT_FOUND:
			mcLogger.severe(tag + "Cannot find warning type '" + argument + "'!");
			break;
		case ERROR_TYPE_NOT_FOUND:
			mcLogger.severe(tag + "Cannot find error type '" + argument + "'!");
			break;
		default:
			sendError(ErrorType.ERROR_TYPE_NOT_FOUND, type.toString());
		}
		
	}
}
