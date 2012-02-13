package me.eighth.suitcase.log;

import org.bukkit.entity.Player;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseConsole.Action;

public class SuitcaseConnector {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/**
	 * Database and YAML file interface for player data
	 * @param plugin Instance of Suitcase
	 */
	public SuitcaseConnector(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Returns the rating of a player
	 * @param target Selected player
	 */
	public double getRating(String target) {
		if (plugin.perm.hasPermission(target, "suitcase.rate")) {
			if (plugin.cfg.getBoolean("log.database.enable")) {
				return 4.2;
			}
			else if (plugin.cfg.getBoolean("log.file.enable")) {
				return plugin.yml.getRating(target);
			}
			else {
				return -1.0;
			}
		}
		else {
			// targeted player must have permission to be rated
			return -1.0;
		}
	}
	
	/**
	 * Sets the rating of a player
	 * @param sender Rating player
	 * @param target Rated player
	 * @param rating Rating value
	 */
	public boolean setRating(String sender, String target, int rating) {
		if (plugin.perm.hasPermission(target, "suitcase.rate")) {
			if (plugin.cfg.getBoolean("log.database.enable")) {
				return true;
			}
			else if (plugin.cfg.getBoolean("log.file.enable")) {
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
	
	/**
	 * Returns the amount of warnings of a player
	 * @param target Selected player
	 */
	public int getWarnings(String target) {
		if (!plugin.perm.hasPermission(target, "suitcase.warn")) {
			if (plugin.cfg.getBoolean("log.database.enable")) {
				return 2;
			}
			else if (plugin.cfg.getBoolean("log.file.enable")) {
				return plugin.yml.getWarnings(target);
			}
			else {
				return -1;
			}
		}
		else {
			// players with permission to warn can't be warned themselves
			return -1;
		}
	}
	
	/**
	 * Increases the warning counter of a player by one or resets it
	 * @param target Warned player
	 * @param warning True if player is warned or false if player is forgiven
	 */
	public boolean setWarnings(String target, boolean warning) {
		if (!plugin.perm.hasPermission(target, "suitcase.warn")) {
			if (plugin.cfg.getBoolean("log.database.enable")) {
				return true;
			}
			else if (plugin.cfg.getBoolean("log.file.enable")) {
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
	
	/**
	 * Check if a player is registered
	 * @param target Selected player
	 */
	public boolean isRegistered(String target) {
		if (plugin.cfg.getBoolean("log.database.enable")) {
			return true;
		}
		else if (plugin.cfg.getBoolean("log.file.enable")) {
			return plugin.yml.isRegistered(target);
		}
		else {
			return false;
		}
	}
	
	/**
	 * Registers a player
	 * @param target Selected player
	 */
	public void register(String target) {
		if (plugin.cfg.getBoolean("log.database.enable")) {
			
		}
		else if (plugin.cfg.getBoolean("log.file.enable")) {
			plugin.yml.register(target);
		}
	}
	
	/** Registers all online players */
	public void registerAll() {
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			register(player.getName());
		}
	}
	
	/** Resets all player ratings and warnings */
	public boolean reset() {
		if (plugin.cfg.getBoolean("log.database.enable")) {
			return true;
		}
		if (plugin.cfg.getBoolean("log.file.enable")) {
			return plugin.yml.reset();
		}
		else {
			return false;
		}
	}
	
	/** Initializes file or database logger */
	public boolean init() {
		if (plugin.cfg.getBoolean("log.database.enable")) {
			return plugin.db.init();
		}
		else if (plugin.cfg.getBoolean("log.file.enable")) {
			return plugin.yml.init();
		}
		else {
			// no log method enabled
			plugin.console.log(Action.LOG_ERROR);
			plugin.cfg.setBoolean("log.file.enable", true);
			return true;
		}
	}
	
	/** Disposes file or closes database connection */
	public boolean free() {
		if (plugin.cfg.getBoolean("log.database.enable")) {
			return plugin.db.free();
		}
		else if (plugin.cfg.getBoolean("log.file.enable")) {
			return plugin.yml.free();
		}
		else {
			return false;
		}
	}
	
	/** Reloads file or database connection */
	public boolean reload() {
		if (free() && init()) {
			return true;
		}
		else {
			return false;
		}
	}
}
