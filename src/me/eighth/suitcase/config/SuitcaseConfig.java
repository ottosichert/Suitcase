package me.eighth.suitcase.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import me.eighth.suitcase.Suitcase;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SuitcaseConfig {
	
	private Suitcase plugin;
	private Map<String, Object> configDefault = new HashMap<String, Object>();
	
	public FileConfiguration config;
	
	
	public SuitcaseConfig(Suitcase plugin) {
		this.plugin = plugin;
		
		// load default values
		configDefault.put("mechanics.language", "en");
		configDefault.put("mechanics.full-help", false);
		configDefault.put("mechanics.op-permissions", true);
		configDefault.put("mechanics.rating.enable", true);
		configDefault.put("mechanics.rating.multiple-rating", false);
		configDefault.put("mechanics.rating.interval", "3d");
		configDefault.put("mechanics.rating.minimum", 0);
		configDefault.put("mechanics.rating.maximum", 100);
		configDefault.put("mechanics.rating.default", 0);
		configDefault.put("mechanics.warning.enable", true);
		configDefault.put("mechanics.warning.maximum", 3);
		configDefault.put("log.rate", true);
		configDefault.put("log.warn", true);
		configDefault.put("log.system", true);
		configDefault.put("log.database.enable", true);
		configDefault.put("log.database.type", "MySQL");
		configDefault.put("log.database.database-name", "minecraft");
		configDefault.put("log.database.table", "suitcase");
		configDefault.put("log.database.username", "root");
		configDefault.put("log.database.password", "root");
		configDefault.put("log.file.enable", true);
		configDefault.put("log.file.max-players", 100);
		configDefault.put("stats.enable", false);
	}

	// get config file
	public boolean initConfig() {
		if (plugin.file.loadFile("config.yml", configDefault)) {
			config = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/config.yml"));
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean freeConfig() {
		config = null;
		return true;
	}
	
	public boolean reloadConfig() {
		if (freeConfig() && initConfig()) {
			return true;
		}
		else {
			return false;
		}
	}
}
