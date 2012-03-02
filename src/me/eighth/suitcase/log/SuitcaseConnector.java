package me.eighth.suitcase.log;

import java.util.ArrayList;

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
		if (plugin.cfg.getBoolean("log.database.enable")) {
			return plugin.db.getRating(target);
		}
		else if (plugin.cfg.getBoolean("log.file.enable")) {
			return plugin.yml.getRating(target);
		}
		return -1.0;
	}
	
	/**
	 * Returns an ArrayList of the top ten best rated players
	 */
	public ArrayList<String> getTopRatings() {
		if (plugin.cfg.getBoolean("log.database.enable")) {
			return plugin.db.getTopRatings();
		}
		else if (plugin.cfg.getBoolean("log.file.enable")) {
			return plugin.yml.getTopRatings();
		}
		return new ArrayList<String>();
	}
	
	/**
	 * Sets the rating of a player
	 * @param sender Rating player
	 * @param target Rated player
	 * @param rating Rating value
	 */
	public boolean setRating(String sender, String target, int rating) {
		if (plugin.cfg.getBoolean("log.database.enable")) {
			return plugin.db.setRating(sender, target, rating);
		}
		else if (plugin.cfg.getBoolean("log.file.enable")) {
			return plugin.yml.setRating(sender, target, rating);
		}
		return false;
	}
	
	/**
	 * Returns the amount of warnings of a player
	 * @param target Selected player
	 */
	public int getWarnings(String target) {
		if (plugin.cfg.getBoolean("log.database.enable")) {
			return plugin.yml.getWarnings(target);
		}
		else if (plugin.cfg.getBoolean("log.file.enable")) {
			return plugin.yml.getWarnings(target);
		}
		return -1;
	}
	
	/**
	 * Increases the warning counter of a player by one or resets it
	 * @param target Warned player
	 * @param warning True if player is warned or false if player is forgiven
	 */
	public boolean setWarnings(String target, boolean warning) {
		if (plugin.cfg.getBoolean("log.database.enable")) {
			return plugin.db.setWarnings(target, warning);
		}
		else if (plugin.cfg.getBoolean("log.file.enable")) {
			return plugin.yml.setWarnings(target, warning);
		}
		return false;
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
		return false;
	}
	
	/**
	 * Registers a player
	 * @param target Selected player
	 */
	public boolean register(String target) {
		if (plugin.cfg.getBoolean("log.database.enable")) {
			return false;
		}
		else if (plugin.cfg.getBoolean("log.file.enable")) {
			return plugin.yml.register(target);
		}
		return false;
	}
	
	/** Registers all online players */
	public boolean registerAll() {
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (!register(player.getName())) {
				return false;
			}
		}
		return true;
	}
	
	/** Resets all player ratings and warnings */
	public boolean reset() {
		if (plugin.cfg.getBoolean("log.database.enable")) {
			return true;
		}
		if (plugin.cfg.getBoolean("log.file.enable")) {
			return plugin.yml.reset();
		}
		return false;
	}
	
	/** Initializes file or database logger */
	public boolean init() {
		if (plugin.cfg.getBoolean("log.database.enable")) {
			return plugin.db.init();
		}
		else if (plugin.cfg.getBoolean("log.file.enable")) {
			return plugin.yml.init();
		}
		// no log method enabled
		plugin.log(Action.LOG_ERROR);
		plugin.cfg.setBoolean("log.file.enable", true);
		return true;
	}
	
	/** Disposes file or closes database connection */
	public boolean free() {
		if (plugin.cfg.getBoolean("log.database.enable")) {
			return plugin.db.free();
		}
		else if (plugin.cfg.getBoolean("log.file.enable")) {
			return plugin.yml.free();
		}
		return false;
	}
	
	/** Reloads file or database connection */
	public boolean reload() {
		if (free() && init()) {
			return true;
		}
		return false;
	}
}
