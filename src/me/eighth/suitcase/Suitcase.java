package me.eighth.suitcase;

import java.util.HashMap;
import java.util.Map;

import me.eighth.suitcase.config.SuitcaseConfig;
import me.eighth.suitcase.config.SuitcaseMessage;
import me.eighth.suitcase.event.SuitcaseCommand;
import me.eighth.suitcase.util.SuitcaseColor;
import me.eighth.suitcase.util.SuitcaseLog;
import me.eighth.suitcase.util.SuitcasePermission;
import me.eighth.suitcase.util.SuitcaseLog.SystemType;

import org.bukkit.plugin.java.JavaPlugin;

public class Suitcase extends JavaPlugin {

	// static classes
	public static Suitcase plugin;
	public static SuitcaseConfig scConfig = new SuitcaseConfig();
	public static SuitcaseMessage scMessage = new SuitcaseMessage();
	public static SuitcaseColor scColor = new SuitcaseColor();
	public static SuitcaseCommand scCommand = new SuitcaseCommand();
	public static SuitcaseLog scLogger = new SuitcaseLog();
	public static SuitcasePermission scPermission = new SuitcasePermission();
	
	// define variables
	public static String version = "Leather Suitcase v0.1";
	public static Map<String, Object> configKeys = new HashMap<String, Object>();
	public static Map<String, Object> messagesKeys = new HashMap<String, Object>();
	
	@Override
	public void onDisable() {
		// TODO: disposing some variables and stuff
		
		// disabling finished, send to log
		scLogger.sendSystem(SystemType.PLUGIN_DISABLED);
	}
	
	@Override
	public void onEnable() {
		// set command executor classes
		getCommand("suitcase").setExecutor(scCommand);
		
		// load and check configuration
		scConfig.initConfig();
		scMessage.initConfig();
		
		// enabling finished, send to log
		scLogger.sendSystem(SystemType.PLUGIN_ENABLED);
	}
	
	public void reload() {
		reloadConfig();

		// enabling finished, send to log
		scLogger.sendSystem(SystemType.PLUGIN_RELOADED);
	}
}
