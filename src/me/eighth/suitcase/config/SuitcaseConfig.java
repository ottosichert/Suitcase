package me.eighth.suitcase.config;

import java.io.File;

import me.eighth.suitcase.Suitcase;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SuitcaseConfig {
	
	// define variables
	private FileConfiguration cfConfig;
	private File cfFile;
	
	// get config file
	public void initConfig() {
		// read file
		cfFile = new File(Suitcase.plugin.getDataFolder(), "config.yml");
		cfConfig = YamlConfiguration.loadConfiguration(cfFile);
		
		// load default values
		loadConfig();
		
		// add property if missing
		for (String path : Suitcase.configKeys.keySet()) {
			if (!cfConfig.contains(path)) {
				cfConfig.set(path, Suitcase.configKeys.get(path));
			}
			// compare object types
			else if (!cfConfig.get(path).equals(Suitcase.configKeys.get(path))) {
				cfConfig.set(path, Suitcase.configKeys.get(path));
			}
		}
		
		// set verified configKeys eventually
		Suitcase.configKeys = cfConfig.getValues(true);
	}
	
	// TODO: Add new entries here
	private void loadConfig() {
		Suitcase.configKeys.put("mechanics.language", "en");
		Suitcase.configKeys.put("mechanics.full-help", false);
		Suitcase.configKeys.put("mechanics.op-permissions", true);
		Suitcase.configKeys.put("mechanics.rating.enable", true);
		Suitcase.configKeys.put("mechanics.rating.multiple-rating", false);
		Suitcase.configKeys.put("mechanics.rating.interval", "3d");
		Suitcase.configKeys.put("mechanics.rating.minimum", 0);
		Suitcase.configKeys.put("mechanics.rating.maximum", 100);
		Suitcase.configKeys.put("mechanics.rating.default", 0);
		Suitcase.configKeys.put("mechanics.warning.enable", true);
		Suitcase.configKeys.put("mechanics.warning.maximum", 3);
		Suitcase.configKeys.put("log.rate", true);
		Suitcase.configKeys.put("log.warn", true);
		Suitcase.configKeys.put("log.system", true);
		Suitcase.configKeys.put("log.database.enable", true);
		Suitcase.configKeys.put("log.database.type", "MySQL");
		Suitcase.configKeys.put("log.database.database-name", "minecraft");
		Suitcase.configKeys.put("log.database.table", "suitcase");
		Suitcase.configKeys.put("log.database.username", "root");
		Suitcase.configKeys.put("log.database.password", "root");
		Suitcase.configKeys.put("log.file.enable", true);
		Suitcase.configKeys.put("log.file.file-name", "suitcase_{date}_{number}.txt");
		Suitcase.configKeys.put("log.file.max-lines", 10000);
		Suitcase.configKeys.put("stats.enable", false);
	}
}
