package me.eighth.suitcase.log;

import java.util.ArrayList;
import java.util.Arrays;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.log.SuitcaseConsole.actionType;

public class SuitcaseConnector {

	private Suitcase plugin;
	
	public SuitcaseConnector(Suitcase plugin) {
		this.plugin = plugin;
	}

	public int getRating(String target) {
		if (plugin.cfg.data.getBoolean("log.database.enable")) {
			return 9;
		}
		else if (plugin.cfg.data.getBoolean("log.file.enable")) {
			return plugin.yml.getRating(target);
		}
		else {
			return 0;
		}
	}
	
	public boolean setRating(String sender, String target, int rating) {
		if (plugin.cfg.data.getBoolean("log.database.enable")) {
			return true;
		}
		else if (plugin.cfg.data.getBoolean("log.file.enable")) {
			return plugin.yml.setRating(sender, target, rating);
		}
		else {
			return false;
		}
	}
	
	public boolean hasRated(String sender, String target) {
		if (plugin.cfg.data.getBoolean("log.database.enable")) {
			return true;
		}
		else if (plugin.cfg.data.getBoolean("log.file.enable")) {
			return true;
		}
		else {
			return false;
		}
	}

	public int getWarnings(String target) {
		if (plugin.cfg.data.getBoolean("log.database.enable")) {
			return 2;
		}
		else if (plugin.cfg.data.getBoolean("log.file.enable")) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	public boolean setWarnings(String sender, String target, boolean increase) {
		if (plugin.cfg.data.getBoolean("log.database.enable")) {
			return true;
		}
		else if (plugin.cfg.data.getBoolean("log.file.enable")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean log(actionType action) {
		return log(action, new ArrayList<String>());
	}
	
	public boolean log(actionType action, ArrayList<String> arguments) {
		return plugin.console.sendAction(action, arguments);
	}
	
	public boolean init() {
		if (plugin.yml.init() && plugin.db.init()) {
			return true;
		}
		else {
			plugin.con.log(actionType.INIT_ERROR, new ArrayList<String>(Arrays.asList("SuitcaseConnector", "FileNotLoaded")));
			return false;
		}
	}

	public boolean free() {
		if (plugin.yml.free() && plugin.db.free()) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean reload() {
		if (free() && init() && plugin.yml.reload() && plugin.db.reload()) {
			return true;
		}
		else {
			return false;
		}
	}
}
