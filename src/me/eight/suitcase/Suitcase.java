package me.eight.suitcase;

import me.eight.suitcase.event.SuitcaseCommand;
import me.eight.suitcase.util.SuitcaseLog;
import me.eight.suitcase.util.SuitcaseLog.SystemType;
import me.eight.suitcase.config.SuitcaseColor;
import me.eight.suitcase.config.SuitcaseConfig;

import org.bukkit.plugin.java.JavaPlugin;

public class Suitcase extends JavaPlugin {

	// define static variables
	public static Suitcase plugin;
	public static SuitcaseConfig scConfig = new SuitcaseConfig();
	public static SuitcaseColor scColor = new SuitcaseColor();
	public static SuitcaseCommand scCommand = new SuitcaseCommand();
	public static SuitcaseLog scLogger = new SuitcaseLog();
	
	// define other variables
	
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
		scConfig.setConfig(getConfig());
		
		// enabling finished, send to log
		scLogger.sendSystem(SystemType.PLUGIN_ENABLED);
	}
	
	public void reload() {
		reloadConfig();

		// enabling finished, send to log
		scLogger.sendSystem(SystemType.PLUGIN_RELOADED);
	}
}
