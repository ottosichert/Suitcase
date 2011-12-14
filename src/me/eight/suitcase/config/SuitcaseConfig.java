package me.eight.suitcase.config;

import org.bukkit.configuration.file.FileConfiguration;

public class SuitcaseConfig {
	
	// define variables
	protected FileConfiguration config;

	// define basic classes
	public boolean setConfig(FileConfiguration conf) {
		config = conf;
		return true;
	}
}
