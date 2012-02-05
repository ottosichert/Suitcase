package me.eighth.suitcase.log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseConsole.Action;

public class SuitcaseYMLFile {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/** Allocate ~/Suitcase/player.yml */
	protected FileConfiguration data;
	
	/**
	 * YAML file interface for player data
	 * @param plugin Instance of Suitcase
	 */
	public SuitcaseYMLFile(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Recalculates the rating of a player
	 * @param target Selected player
	 */
	private boolean calculateRating(String target) {
		target = target.toLowerCase();
		if (isRegistered(target)) {
			FileConfiguration playerData = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/players/" + target + ".yml"));
			// add default
			int totalRating = plugin.cfg.data.getInt("mechanics.rating.default");
			int count = 1;
			for (String ratingPlayers : playerData.getKeys(false)) {
				totalRating += playerData.getInt(ratingPlayers);
				count++;
			}
			
			data.set(target + ".rating", Math.round(Double.valueOf(totalRating) / Double.valueOf(count) * 100.0 ) / 10.0);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Returns the rating of a player
	 * @param target Selected player
	 */
	protected double getRating(String target) {
		target = target.toLowerCase();
		if (isRegistered(target)) {
			return data.getDouble(target + ".rating");
		}
		else {
			return 0.0; // player doesn't exist
		}
	}
	
	/**
	 * Sets the rating of a player
	 * @param sender Rating player
	 * @param target Rated player
	 * @param rating Rating for this player
	 */
	protected boolean setRating(String sender, String target, int rating) {
		target = target.toLowerCase();
		if (isRegistered(sender) && isRegistered(target)) {
			Map<String, Object> playerMap = new HashMap<String, Object>();
			playerMap.put(sender, rating);
			if (plugin.file.load("plugins/Suitcase/players/" + target + ".yml", playerMap)) {
				return calculateRating(target);
			}
			else {
				plugin.console.log(Action.FILE_SAVE_ERROR, "players/" + target + ".yml", "FileNotLoaded");
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * Checks if a player is registered
	 * @param target Selected player
	 */
	protected boolean isRegistered(String target) {
		target = target.toLowerCase();
		if (data.getKeys(false).contains(target)) {
			if (plugin.perm.hasPermission(target, "suitcase.rate")) {
				return true;
			}
			else {
				// don't forget to remove unauthorized player
				plugin.console.log(Action.PLAYER_UNREGISTER, target);
				unregister(target);
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	/**
	 * Registers a player
	 * @param target Selected player
	 */
	protected boolean register(String target) {
		target = target.toLowerCase();
		data.set(target + ".rating", plugin.cfg.data.getDouble("mechanics.rating.default"));
		data.set(target + ".warnings", 0);
		if (plugin.file.load("plugins/Suitcase/player.yml", data)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/player.yml"));
			return true;
		}
		else {
			plugin.console.log(Action.FILE_SAVE_ERROR, "player.yml", "FileNotLoaded");
			return false;
		}
	}
	
	/**
	 * Unregisters a player
	 * @param target Selected player
	 */
	private void unregister(String target) {
		target = target.toLowerCase();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		// remove player from player.yml
		for (String key : data.getKeys(false)) {
			if (!key.equals(target)) {
				dataMap.put(key + ".rating", data.get(key + ".rating"));
				dataMap.put(key + ".warnings", data.get(key + ".warnings"));
			}
		}
		// remove player file
		new File("plugins/Suitcase/players/" + target + ".yml").delete();
		// save file and use new FileConfig
		if (plugin.file.load("plugins/Suitcase/player.yml", dataMap)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/player.yml"));
		}
		else {
			plugin.console.log(Action.FILE_SAVE_ERROR, "player.yml", "FileNotLoaded");
		}
	}
	
	/**
	 * Returns the warning counter of a player
	 * @param target Selected player
	 */
	protected int getWarnings(String target) {
		target = target.toLowerCase();
		if (isRegistered(target)) {
			return data.getInt(target + ".warnings");
		}
		else {
			return 0; // player doesn't exist
		}
	}
	
	/**
	 * Increases the warning counter of a player by one or resets it
	 * @param target Player to be warned or forgiven
	 * @param warning True if player is warned or false if player is forgiven
	 */
	protected boolean setWarnings(String target, boolean warning) {
		target = target.toLowerCase();
		if (isRegistered(target)) {
			int value;
			value = getWarnings(target);
			if (warning) { // increase counter by one
				if (value < plugin.cfg.data.getInt("mechanics.warnings.maximum")) {
					value++;
				}
			}
			else { // reset counter
				value = 0;
			}
			data.set(target + ".warnings", value);
			
			if (plugin.file.load("plugins/Suitcase/player.yml", data)) {
				data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/player.yml"));
				return true;
			}
			else {
				plugin.console.log(Action.FILE_SAVE_ERROR, "player.yml", "FileNotLoaded");
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	/** Resets player data */
	protected void reset() {
		// clear 'player.yml' and 'players/'
		new File("plugins/Suitcase/player.yml").delete();
		new File("plugins/Suitcase/players/").delete();
		if (plugin.file.load("plugins/Suitcase/player.yml")) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/player.yml"));
			plugin.console.log(Action.RESET);
		}
		else {
			// saving new empty file failed
			plugin.console.log(Action.FILE_SAVE_ERROR, "player.yml", "FileNotLoaded");
		}
	}
	
	/** Gets and loads player file  */
	protected boolean init() {
		if (plugin.file.load("plugins/Suitcase/player.yml")) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/player.yml"));
			
			// recalculate rating if mechanics.rating.default changed
			for (String player : data.getKeys(false)) {
				calculateRating(player);
			}
			return true;
		}
		else {
			plugin.console.log(Action.INIT_ERROR, "SuitcaseYMLFile", "FileNotLoaded");
			return false;
		}
	}
	
	/** Disposes player file */
	protected boolean free() {
		data = null;
		return true;
	}
}
