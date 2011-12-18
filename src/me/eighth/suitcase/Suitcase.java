package me.eighth.suitcase;

import java.util.HashMap;
import java.util.Map;

import me.eighth.suitcase.config.SuitcaseConfig;
import me.eighth.suitcase.config.SuitcaseEvent;
import me.eighth.suitcase.config.SuitcaseMessage;
import me.eighth.suitcase.event.SuitcaseCommand;
import me.eighth.suitcase.log.SuitcaseDatabase;
import me.eighth.suitcase.log.SuitcaseFile;
import me.eighth.suitcase.util.SuitcaseColor;
import me.eighth.suitcase.util.SuitcaseLog;
import me.eighth.suitcase.util.SuitcasePermission;

import org.bukkit.plugin.java.JavaPlugin;

public class Suitcase extends JavaPlugin {

	// static classes
	public static Suitcase plugin;
	
	// config
	public static SuitcaseConfig cfConfig = new SuitcaseConfig();
	public static SuitcaseEvent cfEvent = new SuitcaseEvent();
	public static SuitcaseMessage cfMessage = new SuitcaseMessage();
	
	// event
	public static SuitcaseCommand evCommand = new SuitcaseCommand();
	
	// log
	public static SuitcaseDatabase lgDatabase = new SuitcaseDatabase();
	public static SuitcaseFile lgFile = new SuitcaseFile();
	
	// util
	public static SuitcaseColor utColor = new SuitcaseColor();
	public static SuitcaseLog utLog = new SuitcaseLog();
	public static SuitcasePermission utPermission = new SuitcasePermission();
	
	// define variables
	public static String version = "Leather Suitcase v0.1";
	public static Map<String, Object> configKeys = new HashMap<String, Object>();
	public static Map<String, Object> messagesKeys = new HashMap<String, Object>();
	
	@Override
	public void onDisable() {
		// TODO: disposing some variables and stuff
		
		// disabling finished, send to log
		// scLogger.sendSystem(SystemType.PLUGIN_DISABLED);
	}
	
	@Override
	public void onEnable() {
		// set command executor classes
		getCommand("suitcase").setExecutor(evCommand);
		
		// load and check configuration
		cfConfig.initConfig();
		cfMessage.initConfig();
		
		// enabling finished, send to log
		// scLogger.sendSystem(SystemType.PLUGIN_ENABLED);
	}
	
	public void reload() {
		reloadConfig();

		// enabling finished, send to log
		// scLogger.sendSystem(SystemType.PLUGIN_RELOADED);
	}
}
