package me.eighth.suitcase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.eighth.suitcase.config.SuitcaseConfig;
import me.eighth.suitcase.config.SuitcaseEvent;
import me.eighth.suitcase.config.SuitcaseMessage;
import me.eighth.suitcase.event.SuitcaseCommand;
import me.eighth.suitcase.log.SuitcaseDatabase;
import me.eighth.suitcase.log.SuitcaseFile;
import me.eighth.suitcase.util.SuitcaseConsole;
import me.eighth.suitcase.util.SuitcaseConsole.actionType;
import me.eighth.suitcase.util.SuitcasePermission;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Suitcase extends JavaPlugin {

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
	public static SuitcaseConsole utConsole = new SuitcaseConsole();
	public static SuitcasePermission utPermission = new SuitcasePermission();
	
	// define other variables
	public static Suitcase plugin;
	public static String version = "Leather Suitcase v0.1";
	public static FileConfiguration configKeys = null;
	public static FileConfiguration messagesKeys = null;
	public static Map<String, ArrayList<String>> commandAliases = new HashMap<String, ArrayList<String>>();
	
	@Override
	public void onDisable() {
		// plugin unload
		utConsole.sendAction(actionType.PLUGIN_DISABLE_START);
		// TODO: save ratings and warnings
		
		// disabling finished, send to log
		utConsole.sendAction(actionType.PLUGIN_DISABLE_FINISH);
	}
	
	@Override
	public void onEnable() {
		// plugin startup
		utConsole.sendAction(actionType.PLUGIN_ENABLE_START);
		
		// set commands and aliases
		getCommand("suitcase").setExecutor(evCommand);
		getCommand("sc").setExecutor(evCommand);
		commandAliases.put("help", (ArrayList<String>) Arrays.asList("help", "h", "?"));
		commandAliases.put("info", (ArrayList<String>) Arrays.asList("info", "i", "about", "a"));
		commandAliases.put("rate", (ArrayList<String>) Arrays.asList("rate", "r", "vote", "v"));
		commandAliases.put("rate.positive", (ArrayList<String>) Arrays.asList("positive", "p", "good", "g", "+"));
		commandAliases.put("rate.negative", (ArrayList<String>) Arrays.asList("negative", "n", "bad", "b", "-"));
		commandAliases.put("warn", (ArrayList<String>) Arrays.asList("warn", "w", "!"));
		commandAliases.put("warn.forgive", (ArrayList<String>) Arrays.asList("forgive", "f"));
		commandAliases.put("reload", (ArrayList<String>) Arrays.asList("reload"));
		
		// load and check configuration
		if (!cfConfig.initConfig() || !cfMessage.initMessages()) {
			Suitcase.utConsole.sendAction(actionType.PLUGIN_ENABLE_ERROR, (ArrayList<String>) Arrays.asList("initFilesError"));
		}
		else {
			// enabling finished, send to log
			utConsole.sendAction(actionType.PLUGIN_ENABLE_FINISH);
		}
	}
	
	// reload plugin (user command)
	public static void reload() {
		
		// plugin reload
		utConsole.sendAction(actionType.PLUGIN_RELOAD_START);
		
		// reload and check configuration
		if (!cfConfig.initConfig() || !cfMessage.initMessages()) {
			Suitcase.utConsole.sendAction(actionType.PLUGIN_RELOAD_ERROR, (ArrayList<String>) Arrays.asList("initFilesError"));
		}
		else {
			// enabling reloading, send to log
			utConsole.sendAction(actionType.PLUGIN_RELOAD_FINISH);
		}
	}
	
	// disable plugin due to internal error
	public static void disable() {
		PluginManager pm = plugin.getServer().getPluginManager();
		pm.disablePlugin(plugin);
	}
}
