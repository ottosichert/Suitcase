package me.eighth.suitcase.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import me.eighth.suitcase.Suitcase;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SuitcaseConfig {
	
	private Suitcase plugin;
	private Map<String, Object> defaults = new HashMap<String, Object>();
	public FileConfiguration data;
	
	
	public SuitcaseConfig(Suitcase plugin) {
		this.plugin = plugin;
		
		// load default values
		defaults.put("mechanics.language", "en");
		defaults.put("mechanics.full-help", false);
		defaults.put("mechanics.op-permissions", true);
		defaults.put("mechanics.rating.enable", true);
		defaults.put("mechanics.rating.minimum", 0);
		defaults.put("mechanics.rating.maximum", 100);
		defaults.put("mechanics.rating.default", 0);
		defaults.put("mechanics.warning.enable", true);
		defaults.put("mechanics.warning.maximum", 3);
		defaults.put("log.rate", true);
		defaults.put("log.warn", true);
		defaults.put("log.system", true);
		defaults.put("log.database.enable", true);
		defaults.put("log.database.type", "MySQL");
		defaults.put("log.database.database-name", "minecraft");
		defaults.put("log.database.table", "suitcase");
		defaults.put("log.database.username", "root");
		defaults.put("log.database.password", "root");
		defaults.put("log.file.enable", true);
		defaults.put("log.file.max-players", 100);
		defaults.put("stats.enable", false);
	}
	
	// get config file
	public boolean init() {
		if (plugin.file.load("config.yml", defaults)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/config.yml"));
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean free() {
		data = null;
		return true;
	}
	
	public boolean reload() {
		if (free() && init()) {
			return true;
		}
		else {
			return false;
		}
	}
}
