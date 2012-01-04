package me.eighth.suitcase.log;

import me.eighth.suitcase.Suitcase;

public class SuitcaseConnector {

	public String getRating(String target) {
		if (Suitcase.configKeys.getBoolean("log.database.enable")) {
			return "db";
		}
		else if (Suitcase.configKeys.getBoolean("log.file.enable")) {
			return "file";
		}
		else {
			return "none";
		}
	}
	
	public void setRating(String sender, String target, boolean positive) {
		if (Suitcase.configKeys.getBoolean("log.database.enable")) {
			
		}
		else if (Suitcase.configKeys.getBoolean("log.file.enable")) {
			
		}
		else {
			
		}
	}
	
	public boolean hasRated(String sender, String target) {
		if (Suitcase.configKeys.getBoolean("log.database.enable")) {
			return true;
		}
		else if (Suitcase.configKeys.getBoolean("log.file.enable")) {
			return true;
		}
		else {
			return false;
		}
	}

	public String getWarnings(String target) {
		if (Suitcase.configKeys.getBoolean("log.database.enable")) {
			return "db";
		}
		else if (Suitcase.configKeys.getBoolean("log.file.enable")) {
			return "file";
		}
		else {
			return "none";
		}
	}
	
	public void setWarnings(String sender, String target, boolean warn) {
		if (Suitcase.configKeys.getBoolean("log.database.enable")) {
			
		}
		else if (Suitcase.configKeys.getBoolean("log.file.enable")) {
			
		}
		else {
			
		}
	}

	public boolean initLog() {
		// TODO: check database/file availability
		return true;
	}

	public boolean freeLog() {
		// TODO: dispose log cache and close database/file connection
		return true;
	}

	public boolean reloadLog() {
		if (freeLog() && initLog()) {
			return true;
		}
		else {
			return false;
		}
	}
}
