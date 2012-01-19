package me.eighth.suitcase.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.log.SuitcaseConsole.actionType;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SuitcaseConfig {
	
	private Suitcase plugin;
	private Map<String, Object> defaults = new HashMap<String, Object>();
	public FileConfiguration data;
	
	
	public SuitcaseConfig(Suitcase plugin) {
		this.plugin = plugin;
		
		// load default values
		defaults.put("mechanics.locale", "en_EN");
		defaults.put("mechanics.full-help", false);
		defaults.put("mechanics.op-permissions", true);
		defaults.put("mechanics.rating.enable", true);
		defaults.put("mechanics.rating.maximum", 10);
		defaults.put("mechanics.rating.default", 5);
		defaults.put("mechanics.warnings.enable", true);
		defaults.put("mechanics.warnings.maximum", 3);
		defaults.put("log.console.command", true);
		defaults.put("log.console.file", true);
		defaults.put("log.console.debug", false);
		defaults.put("log.database.enable", true);
		defaults.put("log.database.type", "MySQL");
		defaults.put("log.database.database-name", "minecraft");
		defaults.put("log.database.port", "12345");
		defaults.put("log.database.table", "suitcase");
		defaults.put("log.database.username", "root");
		defaults.put("log.database.password", "root");
		defaults.put("log.file.enable", true);
		defaults.put("log.file.max-players", 100);
		defaults.put("stats.enable", false);
	}
	
	// get config file
	public boolean init() {
		if (plugin.file.load("config.yml", defaults, false)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/config.yml"));
			return true;
		}
		else {
			plugin.con.log(actionType.INIT_ERROR, new ArrayList<String>(Arrays.asList("SuitcaseConfig", "FileNotLoaded")));
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
