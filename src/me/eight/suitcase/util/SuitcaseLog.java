package me.eight.suitcase.util;

import java.util.logging.Logger;

import me.eight.suitcase.Suitcase;

import org.bukkit.plugin.PluginDescriptionFile;

public class SuitcaseLog {

	// define variables
	private final Logger mcLogger = Logger.getLogger("Minecraft");
	private PluginDescriptionFile pdf = Suitcase.plugin.getDescription();
	private String tag = "[" + pdf.getName() + "] "; // this should be '[Suitcase] ' 
	
	// system message types
	public enum SystemType {
		PLUGIN_ENABLED,
		PLUGIN_DISABLED,
		PLUGIN_RELOADED
	}
	
	// default message types
	public enum MessageType {
		PLAYER_COMMAND,
		PLAYER_COMMAND_DENY
	}
	
	// warning types
	public enum WarningType {
		CONFIG_FILE_NOT_FOUND,
		MESSAGE_FILE_NOT_FOUND
	}
	
	// error types
	public enum ErrorType {
		COLOR_NOT_FOUND,
		VARIABLE_NOT_FOUND,
		TYPE_NOT_FOUND
	}
	
	// send system message (info level)
	public void sendSystem (SystemType type) {
		switch (type) {
		case PLUGIN_ENABLED:
			mcLogger.info(tag + Suitcase.version + " by " + pdf.getAuthors().toString().replaceAll("^[\\[\\]]|[\\[\\]]$", "") + " enabled."); // remove brackets
			break;
		case PLUGIN_DISABLED:
			mcLogger.info(tag + "disabled.");
			break;
		case PLUGIN_RELOADED:
			mcLogger.info(tag + "reloaded.");
			break;
		default:
			sendError(ErrorType.TYPE_NOT_FOUND, type.toString());
		}
	}
	
	// send default message (info level)
	public void sendMessage(MessageType type, String argument) {
		switch (type) { // argument = Player123: /sc vote Player456 +
		case PLAYER_COMMAND:
			mcLogger.info(tag + "Executing command for " + argument);
			break;
		case PLAYER_COMMAND_DENY:
			mcLogger.info(tag + "Denied command for " + argument);
		default:
			sendError(ErrorType.TYPE_NOT_FOUND, type.toString());
		}
	}
	
	// send warning message (warning level)
	public void sendWarning(WarningType type, String argument) {
		switch (type) {
		case CONFIG_FILE_NOT_FOUND:
			mcLogger.warning(tag + "Cannot find config.yml! Creating default...");
			// TODO: scConfig.setDefaultPlugin();
			break;
		case MESSAGE_FILE_NOT_FOUND:
			mcLogger.warning(tag + "Cannot find messages.yml! Creating default...");
			// TODO: scConfig.setDefaultMessage();
			break;
		default:
			sendError(ErrorType.TYPE_NOT_FOUND, type.toString());
		}
	}
	
	// send error message (severe level)
	public void sendError (ErrorType type, String argument) {
		switch (type) {
		case COLOR_NOT_FOUND:
			mcLogger.severe(tag + "Cannot find color: " + argument);
			break;
		case VARIABLE_NOT_FOUND:
			mcLogger.severe(tag + "Cannot find variable: " + argument);
		case TYPE_NOT_FOUND:
			mcLogger.severe(tag + "Cannot find type: " + argument);
			break;
		default:
			sendError(ErrorType.TYPE_NOT_FOUND, type.toString());
		}
	}
}
