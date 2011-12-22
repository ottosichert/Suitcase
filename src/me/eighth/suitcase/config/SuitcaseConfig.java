package me.eighth.suitcase.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseConsole.actionType;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SuitcaseConfig {
	
	// define variables
	private FileConfiguration cfConfig;
	private File cfFile;
	
	// get config file
	public boolean initConfig() {
		
		// load default values
		loadConfig();
		
		// read file
		cfFile = new File(Suitcase.plugin.getDataFolder(), "config.yml");
		if (cfFile.exists()) {
			cfConfig = YamlConfiguration.loadConfiguration(cfFile);
			// add property if missing
			for (String path : Suitcase.configKeys.getKeys(true)) {
				if (!cfConfig.contains(path)) {
					cfConfig.set(path, Suitcase.configKeys.get(path));
				}
				// compare object types
				// TODO: check if this is working
				else if (cfConfig.get(path).getClass() != (Suitcase.configKeys.get(path).getClass())) {
					cfConfig.set(path, Suitcase.configKeys.get(path));
				}
			}
			// save and use verified configKeys
			try {
				cfConfig.save(cfFile);
				Suitcase.configKeys = cfConfig;
				return true;
			} catch (IOException e) {
				Suitcase.utConsole.sendAction(actionType.FILE_SAVE_ERROR, (ArrayList<String>) Arrays.asList("config.yml", e.toString()));
				return false;
			}
		}
		else {
			Suitcase.utConsole.sendAction(actionType.FILE_NOT_FOUND, (ArrayList<String>) Arrays.asList("config.yml"));
			try {
				cfFile.createNewFile();
				cfConfig = Suitcase.configKeys;
				cfConfig.save(cfFile);
				return true;
			} catch (IOException e) {
				Suitcase.utConsole.sendAction(actionType.FILE_SAVE_ERROR, (ArrayList<String>) Arrays.asList("config.yml", e.toString()));
				return false;
			}
		}
	}
	
	private void loadConfig() {
		Suitcase.configKeys.set("mechanics.language", "en");
		Suitcase.configKeys.set("mechanics.full-help", false);
		Suitcase.configKeys.set("mechanics.op-permissions", true);
		Suitcase.configKeys.set("mechanics.rating.enable", true);
		Suitcase.configKeys.set("mechanics.rating.multiple-rating", false);
		Suitcase.configKeys.set("mechanics.rating.interval", "3d");
		Suitcase.configKeys.set("mechanics.rating.minimum", 0);
		Suitcase.configKeys.set("mechanics.rating.maximum", 100);
		Suitcase.configKeys.set("mechanics.rating.default", 0);
		Suitcase.configKeys.set("mechanics.warning.enable", true);
		Suitcase.configKeys.set("mechanics.warning.maximum", 3);
		Suitcase.configKeys.set("log.rate", true);
		Suitcase.configKeys.set("log.warn", true);
		Suitcase.configKeys.set("log.system", true);
		Suitcase.configKeys.set("log.database.enable", true);
		Suitcase.configKeys.set("log.database.type", "MySQL");
		Suitcase.configKeys.set("log.database.database-name", "minecraft");
		Suitcase.configKeys.set("log.database.table", "suitcase");
		Suitcase.configKeys.set("log.database.username", "root");
		Suitcase.configKeys.set("log.database.password", "root");
		Suitcase.configKeys.set("log.file.enable", true);
		Suitcase.configKeys.set("log.file.max-players", 100);
		Suitcase.configKeys.set("stats.enable", false);
	}
}
