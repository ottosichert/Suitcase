package me.eight.suitcase;

import me.eight.suitcase.event.SuitcaseCommand;
import me.eight.suitcase.util.SuitcaseColor;
import me.eight.suitcase.util.SuitcaseLog;
import me.eight.suitcase.util.SuitcaseLog.SystemType;
import me.eight.suitcase.util.SuitcasePermission;
import me.eight.suitcase.config.SuitcaseConfig;
import me.eight.suitcase.config.SuitcaseConfig.config;
import me.eight.suitcase.config.SuitcaseMessage;
import me.eight.suitcase.config.SuitcaseMessage.messages;

import org.bukkit.plugin.java.JavaPlugin;

public class Suitcase extends JavaPlugin {

	// define static classes
	
	// general classes (prefix sc)
	public static Suitcase plugin;
	public static SuitcaseConfig scConfig = new SuitcaseConfig();
	public static SuitcaseMessage scMessage = new SuitcaseMessage();
	public static SuitcaseColor scColor = new SuitcaseColor();
	public static SuitcaseCommand scCommand = new SuitcaseCommand();
	public static SuitcaseLog scLogger = new SuitcaseLog();
	public static SuitcasePermission scPermission = new SuitcasePermission();
	
	// config classes (prefix cf)
	public static config.mechanics cfMechanics = new SuitcaseConfig.config.mechanics();
	public static config.mechanics.rating cfRating = new SuitcaseConfig.config.mechanics.rating();
	public static config.mechanics.warning cfWarning = new SuitcaseConfig.config.mechanics.warning();
	public static config.log cfLog = new SuitcaseConfig.config.log();
	public static config.log.database cfDatabase = new SuitcaseConfig.config.log.database();
	public static config.log.text cfText = new SuitcaseConfig.config.log.text();
	public static config.appearance cfAppearance = new SuitcaseConfig.config.appearance();
	public static config.appearance.color cfColor = new SuitcaseConfig.config.appearance.color();
	public static config.stats cfStats = new SuitcaseConfig.config.stats();
	
	// messages classes (prefix ms)
	public static messages.info msInfo = new SuitcaseMessage.messages.info();
	public static messages.header msHeader = new SuitcaseMessage.messages.header();
	public static messages.command msCommand = new SuitcaseMessage.messages.command();
	// TODO: Custom event messages
	
	// define variables
	public static String version = "Leather Suitcase v0.1";
	
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
