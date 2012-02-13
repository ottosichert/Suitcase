package me.eighth.suitcase.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseConsole.Action;

public class SuitcaseConfig {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/** Stores default config.yml */
	private Map<String, Object> defaults = new HashMap<String, Object>();
	
	/** Allocates ~/Suitcase/config.yml */
	private FileConfiguration data;
	
	/**
	 * File interface of basic plugin settings
	 * @param plugin Instance of Suitcase
	 */
	public SuitcaseConfig(Suitcase plugin) {
		this.plugin = plugin;
		
		// load default values
		defaults.put("mechanics.locale", "en_EN");
		defaults.put("mechanics.full-help", false);
		defaults.put("mechanics.op-permissions", true);
		defaults.put("mechanics.rating.enable", true);
		defaults.put("mechanics.rating.maximum", 10.0);
		defaults.put("mechanics.rating.default", 5.0);
		defaults.put("mechanics.warnings.enable", true);
		defaults.put("mechanics.warnings.maximum", 3);
		defaults.put("log.console.command", true);
		defaults.put("log.console.debug", false);
		defaults.put("log.console.broadcast", false);
		defaults.put("log.database.enable", false);
		defaults.put("log.database.type", "MySQL");
		defaults.put("log.database.database-name", "minecraft");
		defaults.put("log.database.port", "12345");
		defaults.put("log.database.table", "suitcase");
		defaults.put("log.database.username", "root");
		defaults.put("log.database.password", "root");
		defaults.put("log.file.enable", true);
		defaults.put("stats.enable", false);
		defaults.put("stats.time", "30d");
		defaults.put("stats.show-warnings", true);
		defaults.put("broadcast.rate", false);
		defaults.put("broadcast.warn", true);
		defaults.put("broadcast.forgive", true);
		defaults.put("broadcast.reload", false);
		defaults.put("broadcast.reset", true);
	}
	
	/**
	 * Returns a String
	 * @param key Config.yml key
	 */
	public String getString(String key) {
		return data.getString(key);
	}
	
	/**
	 * Returns an Integer
	 * @param key Config.yml key
	 */
	public int getInt(String key) {
		return data.getInt(key);
	}
	
	/**
	 * Returns a Boolean
	 * @param key Config.yml key
	 */
	public boolean getBoolean(String key) {
		return data.getBoolean(key);
	}
	
	/**
	 * Returns a Double
	 * @param key Config.yml key
	 */
	public double getDouble(String key) {
		return data.getDouble(key);
	}
	
	/**
	 * Sets a Boolean
	 * @param key Config.yml key
	 * @param value Boolean value
	 */
	public void setBoolean(String key, boolean value) {
		data.set(key, value);
	}
	
	/** Resets plugin configuration */
	public boolean reset() {
		new File("plugins/Suitcase/config.yml").delete();
		if (plugin.file.load("plugins/Suitcase/config.yml", defaults, true)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/config.yml"));
			return true;
		}
		else {
			return false;
		}
	}

	/** Gets and reads plugin configuration file */
	public boolean init() {
		if (plugin.file.load("plugins/Suitcase/config.yml", defaults, false)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/config.yml"));
			return true;
		}
		else {
			plugin.console.log(Action.INIT_ERROR, "SuitcaseConfig");
			return false;
		}
	}
	
	/** Disposes plugin configuration */
	public boolean free() {
		data = null;
		return true;
	}
	
	/** Reloads plugin configuration */
	public boolean reload() {
		if (free() && init()) {
			return true;
		}
		else {
			return false;
		}
	}
}
