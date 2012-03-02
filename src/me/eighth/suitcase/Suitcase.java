package me.eighth.suitcase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.eighth.suitcase.config.SuitcaseConfig;
import me.eighth.suitcase.config.SuitcaseEvent;
import me.eighth.suitcase.config.SuitcaseMessage;
import me.eighth.suitcase.event.SuitcaseBlockListener;
import me.eighth.suitcase.event.SuitcaseCommandExecutor;
import me.eighth.suitcase.event.SuitcasePlayerListener;
import me.eighth.suitcase.log.SuitcaseConnector;
import me.eighth.suitcase.log.SuitcaseDatabase;
import me.eighth.suitcase.log.SuitcaseYMLFile;
import me.eighth.suitcase.util.SuitcaseConsole;
import me.eighth.suitcase.util.SuitcaseFile;
import me.eighth.suitcase.util.SuitcasePermission;
import me.eighth.suitcase.util.SuitcaseConsole.Action;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Suitcase extends JavaPlugin {
	
	/** Current version name */
	public final String name = "Leather";
	
	/** Prefix for console notifications */
	public final String pluginTag = "[Suitcase] ";
	
	/** Prefix for player commands */
	public final String playerTag = "[PLAYER_COMMAND] ";
	
	/** Prefix for console commands */
	public final String consoleTag = "[CONSOLE_COMMAND] ";
	
	// Suitcase classes
	public final SuitcaseConfig cfg = new SuitcaseConfig(this);
	public final SuitcaseEvent event = new SuitcaseEvent(this);
	public final SuitcaseMessage msg = new SuitcaseMessage(this);
	
	public final SuitcaseCommandExecutor command = new SuitcaseCommandExecutor(this);
	public final SuitcasePlayerListener player = new SuitcasePlayerListener(this);
	public final SuitcaseBlockListener block = new SuitcaseBlockListener(this);
	
	public final SuitcaseConnector con = new SuitcaseConnector(this);
	public final SuitcaseDatabase db = new SuitcaseDatabase(this);
	public final SuitcaseYMLFile yml = new SuitcaseYMLFile(this);
	
	private final SuitcaseConsole console = new SuitcaseConsole(this);
	private final SuitcaseFile file = new SuitcaseFile(this);
	private final SuitcasePermission perm = new SuitcasePermission(this);

	@Override
	public void onEnable() {
		// set command executors and event listeners
		getCommand("suitcase").setExecutor(command);
		
		// register event
		PluginManager manager = getServer().getPluginManager();
		manager.registerEvents(player, this);
		manager.registerEvents(block, this);
		
		// load and check configuration
		if (!cfg.init() || !msg.init() || !event.init() || !con.init()) {
			disable();
			return;
		}
		
		// add online players
		con.registerAll();
		
		// enabling finished, send to log
		log(Action.PLUGIN_ENABLE);
	}
	
	@Override
	public void onDisable() {
		// save and dispose configuration in reverse order, ignore errors and continue disabling
		con.free();
		event.free();
		msg.free();
		cfg.free();
		
		// disabling finished, send to log
		log(Action.PLUGIN_DISABLE);
	}
	
	/**
	 * Sends a debug message to console
	 * @param arguments Debug information
	 */
	public boolean d(String...arguments) {
		return log(Action.DEBUG, arguments);
	}
	
	/**
	 * Gets string from ArrayList and removes brackets
	 * @param list An ArrayList of Strings to be converted to a single String
	 * @param separator Set an item separator
	 */
	public static String getStringFromList(List<String> list, String separator) {
		return getStringFromList(new ArrayList<String>(list), separator);
	}
	
	/**
	 * Gets string from ArrayList and removes brackets
	 * @param list An ArrayList of Strings to be converted to a single String
	 * @param separator Set an item separator
	 */
	public static String getStringFromList(ArrayList<String> list, String separator) {
		return list.toString().replaceAll("^\\[|\\]$", "").replaceAll(", ", separator);
	}
	
	/**
	 * Removes Strings from an ArrayList
	 * @param defaults ArrayList to remove Strings from
	 * @param remove Strings to be removed
	 */
	public static ArrayList<String> removeStringsFromList(String[] defaults, String...remove) {
		return removeStringsFromList(new ArrayList<String>(Arrays.asList(defaults)), remove);
	}
	
	/**
	 * Removes Strings from an ArrayList
	 * @param defaults ArrayList to remove Strings from
	 * @param remove Strings to be removed
	 */
	public static ArrayList<String> removeStringsFromList(Set<String> defaults, String...remove) {
		return removeStringsFromList(new ArrayList<String>(Arrays.asList(defaults.toArray(new String[0]))), remove);
	}
	
	/**
	 * Removes Strings from an ArrayList
	 * @param defaults ArrayList to remove Strings from
	 * @param remove Strings to be removed
	 */
	public static ArrayList<String> removeStringsFromList(List<String> defaults, String...remove) {
		return removeStringsFromList(new ArrayList<String>(defaults), remove);
	}
	
	/**
	 * Removes Strings from an ArrayList
	 * @param defaults ArrayList to remove Strings from
	 * @param remove Strings to be removed
	 */
	public static ArrayList<String> removeStringsFromList(ArrayList<String> defaults, String...remove) {
		for (String defaultsString : defaults) {
			for (String removeString : remove) {
				if (defaultsString.equals(removeString)) {
					defaults.remove(defaultsString);
					break;
				}
			}
		}
		return defaults;
	}
	
	/** Reloads plugin */
	public boolean reload() {
		// reload settings and connector
		if (cfg.reload() && msg.reload() && event.reload() && con.reload()) {
			// reloading finished, send to log
			log(Action.PLUGIN_RELOAD);
			return true;
		}
		else {
			return false;
		}
		
	}
	
	/** Resets configuration and player data */
	public boolean reset() {
		// reset player ratings
		if (cfg.reset() && msg.reset() && event.reset() && con.reset()) {
			log(Action.RESET);
			return true;
		}
		else {
			return false;
		}
	}
	
	/** Disables plugin due to an internal error */
	public void disable() {
		setEnabled(false);
	}

	
	/**
	 * Logs an action to console
	 * @param action Action type to be logged
	 */
	public boolean log(Action action) {
		return log(action, new ArrayList<String>());
	}
	
	/**
	 * Logs an action to console
	 * @param action Action type to be logged
	 * @param arguments Action arguments
	 */
	public boolean log(Action action, String...arguments) {
		return log(action, new ArrayList<String>(Arrays.asList(arguments)));
	}

	/**
	 * Logs an action to console
	 * @param action Action type to be logged
	 * @param arguments Action arguments
	 */
	public boolean log(Action action, ArrayList<String> arguments) {
		return console.log(action, arguments);
	}
	
	/**
	 * Loads a file and merges its value with its defaults
	 * @param filename File to be loaded
	 */
	public boolean load(String filename) {
		return load(filename, new HashMap<String, Object>());
	}
	
	/**
	 * Loads a file and merges its value with its defaults
	 * @param filename File to be loaded
	 * @param defaults Path keys and values
	 */
	public boolean load(String filename, FileConfiguration defaults) {
		return load(filename, defaults, true);
	}
	
	/**
	 * Loads a file and merges its value with its defaults
	 * @param filename File to be loaded
	 * @param defaults Path keys and values
	 */
	public boolean load(String filename, Map<String, Object> defaults) {
		return load(filename, defaults, true);
	}
	
	/**
	 * Loads a file and merges its value with its defaults
	 * @param filename File to be loaded
	 * @param defaults Path keys and values
	 * @param optional Keep redundant keys and hide console messages
	 */
	public boolean load(String filename, FileConfiguration defaults, boolean optional) {
		Map<String, Object> defaultsMap = new HashMap<String, Object>();
		for (String key : defaults.getKeys(true)) {
			if (!defaults.isConfigurationSection(key)) {
				d(key, defaults.get(key).toString());
				defaultsMap.put(key, defaults.get(key));
			}
		}
		return load(filename, defaultsMap, optional);
	}

	/**
	 * Loads a file and merges its value with its defaults
	 * @param filename File to be loaded
	 * @param defaults Path keys and values
	 * @param optional Keep redundant keys and hide console messages
	 */
	public boolean load(String filename, Map<String, Object> defaults, boolean optional) {
		return file.load(filename, defaults, optional);
	}
	
	
	/**
	 * Checks if player or console has permission to a Suitcase action
	 * @param sender Command sender or target
	 * @param permission Suitcase permission
	 */
	public boolean hasPermission(String sender, String permission) {
		return perm.hasPermission(sender, permission);
	}
}
