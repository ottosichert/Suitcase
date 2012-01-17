package me.eighth.suitcase.log;

import me.eighth.suitcase.Suitcase;

public class SuitcaseConnector {

	private Suitcase plugin;
	
	public SuitcaseConnector(Suitcase plugin) {
		this.plugin = plugin;
	}

	public String getRating(String target) {
		if (plugin.config.data.getBoolean("log.database.enable")) {
			return "db";
		}
		else if (plugin.config.data.getBoolean("log.file.enable")) {
			return "file";
		}
		else {
			return "none";
		}
	}
	
	public void setRating(String sender, String target, boolean positive) {
		if (plugin.config.data.getBoolean("log.database.enable")) {
			
		}
		else if (plugin.config.data.getBoolean("log.file.enable")) {
			
		}
		else {
			
		}
	}
	
	public boolean hasRated(String sender, String target) {
		if (plugin.config.data.getBoolean("log.database.enable")) {
			return true;
		}
		else if (plugin.config.data.getBoolean("log.file.enable")) {
			return true;
		}
		else {
			return false;
		}
	}

	public String getWarnings(String target) {
		if (plugin.config.data.getBoolean("log.database.enable")) {
			return "db";
		}
		else if (plugin.config.data.getBoolean("log.file.enable")) {
			return "file";
		}
		else {
			return "none";
		}
	}
	
	public void setWarnings(String sender, String target, boolean warn) {
		if (plugin.config.data.getBoolean("log.database.enable")) {
			
		}
		else if (plugin.config.data.getBoolean("log.file.enable")) {
			
		}
		else {
			
		}
	}

	public boolean init() {
		// TODO: check database/file availability
		return true;
	}

	public boolean free() {
		// TODO: dispose log cache and close database/file connection
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
