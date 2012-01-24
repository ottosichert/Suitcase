package me.eighth.suitcase.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.log.SuitcaseConsole.Action;

public class SuitcaseConnector {

	private Suitcase plugin;
	
	public SuitcaseConnector(Suitcase plugin) {
		this.plugin = plugin;
	}

	public double getRating(String target) {
		if (plugin.perm.hasPermission(target, "suitcase.rate")) {
			if (plugin.cfg.data.getBoolean("log.database.enable")) {
				return Math.round(new Random().nextDouble() * 100.0) / 10.0;
			}
			else if (plugin.cfg.data.getBoolean("log.file.enable")) {
				return plugin.yml.getRating(target);
			}
			else {
				return 0.0;
			}
		}
		else {
			// targeted player must have permission to be rated
			return 0.0;
		}
	}
	
	public boolean setRating(String sender, String target, int rating) {
		if (plugin.perm.hasPermission(target, "suitcase.rate")) {
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
		else {
			return false;
		}
	}

	public int getWarnings(String target) {
		if (!plugin.perm.hasPermission(target, "suitcase.warn")) {
			if (plugin.cfg.data.getBoolean("log.database.enable")) {
				return 2;
			}
			else if (plugin.cfg.data.getBoolean("log.file.enable")) {
				return plugin.yml.getWarnings(target);
			}
			else {
				return 1;
			}
		}
		else {
			// players with permission to warn can't be warned themselves
			return new Random().nextInt(4);
		}
	}
	
	public boolean setWarnings(String target, boolean warning) {
		if (!plugin.perm.hasPermission(target, "suitcase.warn")) {
			if (plugin.cfg.data.getBoolean("log.database.enable")) {
				return true;
			}
			else if (plugin.cfg.data.getBoolean("log.file.enable")) {
				return plugin.yml.setWarnings(target, warning);
			}
			else {
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public boolean isRegistered(String target) {
		if (plugin.cfg.data.getBoolean("log.database.enable")) {
			return true;
		}
		else if (plugin.cfg.data.getBoolean("log.file.enable")) {
			return plugin.yml.isRegistered(target);
		}
		else {
			return false;
		}
	}
	
	public boolean register(String target) {
		if (plugin.cfg.data.getBoolean("log.database.enable")) {
			return true;
		}
		else if (plugin.cfg.data.getBoolean("log.file.enable")) {
			return plugin.yml.register(target);
		}
		else {
			return false;
		}
	}
	
	public boolean log(Action action) {
		return log(action, new ArrayList<String>());
	}
	
	public boolean log(Action action, String...arguments) {
		return log(action, new ArrayList<String>(Arrays.asList(arguments)));
	}
	
	public boolean log(Action action, ArrayList<String> arguments) {
		return plugin.console.sendAction(action, arguments);
	}
	
	public void broadcast(String...lines) {
		broadcast(new ArrayList<String>(Arrays.asList(lines)));
	}
	
	public void broadcast(ArrayList<String> lines) {
		plugin.broad.broadcastMessage(lines);
	}
	
	public void reset() {
		if (plugin.cfg.data.getBoolean("log.database.enable")) {
			
		}
		if (plugin.cfg.data.getBoolean("log.file.enable")) {
			plugin.yml.reset();
		}
	}
	
	public boolean init() {
		if (plugin.cfg.data.getBoolean("log.database.enable")) {
			return plugin.db.init();
		}
		else if (plugin.cfg.data.getBoolean("log.file.enable")) {
			return plugin.yml.init();
		}
		else {
			plugin.con.log(Action.INIT_ERROR, new ArrayList<String>(Arrays.asList("SuitcaseConnector", "NoLogMethodEnabled")));
			return false;
		}
	}

	public boolean free() {
		if (plugin.cfg.data.getBoolean("log.database.enable")) {
			return plugin.db.free();
		}
		else if (plugin.cfg.data.getBoolean("log.file.enable")) {
			return plugin.yml.free();
		}
		else {
			plugin.con.log(Action.FREE_ERROR, new ArrayList<String>(Arrays.asList("SuitcaseConnector", "NoLogMethodEnabled")));
			return false;
		}
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
