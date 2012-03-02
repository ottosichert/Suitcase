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
		defaults.put("rating.enable", true);
		defaults.put("rating.maximum", 10.0);
		defaults.put("rating.default", 5.0);
		defaults.put("warnings.enable", true);
		defaults.put("warnings.maximum", 3);
		defaults.put("sign.enable", true);
		defaults.put("sign.text", "[Suitcase]");
		defaults.put("sign.command.help", true);
		defaults.put("sign.command.info", true);
		defaults.put("sign.command.rate", true);
		defaults.put("sign.command.top", true);
		defaults.put("sign.command.warn", true);
		defaults.put("sign.command.forgive", true);
		defaults.put("sign.command.reload", true);
		defaults.put("sign.command.reset", true);
		defaults.put("log.console.command", true);
		defaults.put("log.console.event", true);
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
		if (new File("plugins/Suitcase/config.yml").delete() && plugin.load("plugins/Suitcase/config.yml", defaults)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/config.yml"));
			return true;
		}
		return false;
	}

	/** Gets and reads plugin configuration file */
	public boolean init() {
		if (plugin.load("plugins/Suitcase/config.yml", defaults, false)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/config.yml"));
			return true;
		}
		plugin.log(Action.INIT_ERROR, "SuitcaseConfig");
		return false;
	}
	
	/** Disposes plugin configuration */
	public boolean free() {
		if (plugin.load("plugins/Suitcase/config.yml", data)) {
			data = null;
			return true;
		}
		plugin.log(Action.FREE_ERROR, "SuitcaseConfig");
		return false;
	}
	
	/** Reloads plugin configuration */
	public boolean reload() {
		if (free() && init()) {
			return true;
		}
		return false;
	}
}
