package me.eighth.suitcase.log;

import java.io.File;
import java.util.ArrayList;
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
		if (isRegistered(target)) {
			FileConfiguration playerData = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/players/" + target + ".yml"));
			// add default
			int totalRating = plugin.cfg.getInt("rating.default");
			int count = 1;
			// add other players
			for (String ratingPlayers : playerData.getKeys(false)) {
				totalRating += playerData.getInt(ratingPlayers);
				count++;
			}
			
			data.set(target + ".rating", Math.round(Double.valueOf(totalRating) / Double.valueOf(count) * 10.0) / 10.0);
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the rating of a player
	 * @param target Selected player
	 */
	protected double getRating(String target) {
		if (isRegistered(target)) {
			return data.getDouble(target + ".rating");
		}
		else {
			return -1.0; // player doesn't exist
		}
	}
	
	/**
	 * Returns an ArrayList of the top ten best rated players
	 */
	protected ArrayList<String> getTopRatings() {
		ArrayList<String> players = new ArrayList<String>(); // all registered players
		ArrayList<Double> ratings = new ArrayList<Double>(); // rating of those
		ArrayList<String> values = new ArrayList<String>(); // final list of players
		double rating;
		int index;
		
		for (String player : data.getKeys(false)) {
			rating = data.getDouble(player + ".rating");
			if (rating >= 0.0) {
				// add player and his rating
				players.add(player);
				ratings.add(rating);
			}
		}
		
		for (int i = 0; i < 10; i++) {
			rating = -1.0;
			index = -1;
			
			for (int j = 0; j < players.size(); j++) {
				// get top rating
				if (ratings.get(j) > rating) {
					rating = ratings.get(j);
					index = j;
				}
			}
			
			if (index >= 0) {
				// add player to top list
				values.add(players.get(index));

				// temporary reset player's rating
				ratings.set(index, -1.0);
			}
			else {
				break;
			}
		}
		
		return values;
	}
	
	/**
	 * Sets the rating of a player
	 * @param sender Rating player
	 * @param target Rated player
	 * @param rating Rating for this player
	 */
	protected boolean setRating(String sender, String target, int rating) {
		if (isRegistered(sender) && isRegistered(target)) {
			Map<String, Object> playerMap = new HashMap<String, Object>();
			playerMap.put(sender, rating);
			if (plugin.load("plugins/Suitcase/players/" + target + ".yml", playerMap)) {
				return calculateRating(target);
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
	 * Checks if a player is registered
	 * @param target Selected player
	 */
	protected boolean isRegistered(String target) {
		if (data.contains(target) && data.contains(target + ".rating") && data.contains(target + ".warnings")) {
			return true;
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
		if (!isRegistered(target)) {
			
			// set rating
			if (plugin.hasPermission(target, "rate")) {
				data.set(target + ".rating", plugin.cfg.getDouble("rating.default"));
			}
			else {
				data.set(target + ".rating", -1.0);
			}
			
			// set warnings
			if (!plugin.hasPermission(target, "warn")) {
				data.set(target + ".warnings", 0);
			}
			else {
				data.set(target + ".warnings", -1);
			}
			
			// save file
			if (plugin.load("plugins/Suitcase/player.yml", data)) {
				data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/player.yml"));
				plugin.log(Action.PLAYER_REGISTER, target);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Unregisters a player
	 * @param target Selected player
	 */
	public boolean unregister(String target) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		// remove player from player.yml
		for (String key : data.getKeys(false)) {
			if (!key.equals(target)) {
				dataMap.put(key + ".rating", data.get(key + ".rating"));
				dataMap.put(key + ".warnings", data.get(key + ".warnings"));
			}
		}
		// remove player file, save players and use new FileConfig
		if (new File("plugins/Suitcase/players/" + target + ".yml").delete() && plugin.load("plugins/Suitcase/player.yml", dataMap)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/player.yml"));
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the warning counter of a player
	 * @param target Selected player
	 */
	protected int getWarnings(String target) {
		if (isRegistered(target)) {
			return data.getInt(target + ".warnings");
		}
		// player doesn't exist
		return -1;
	}
	
	/**
	 * Increases the warning counter of a player by one or resets it
	 * @param target Player to be warned or forgiven
	 * @param warning True if player is warned or false if player is forgiven
	 */
	protected boolean setWarnings(String target, boolean warning) {
		if (isRegistered(target)) {
			int value;
			value = getWarnings(target);
			if (warning) { // increase counter by one
				if (value < plugin.cfg.getInt("warnings.maximum")) {
					value++;
				}
			}
			else { // reset counter
				value = 0;
			}
			data.set(target + ".warnings", value);
			
			if (plugin.load("plugins/Suitcase/player.yml", data)) {
				data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/player.yml"));
				return true;
			}
		}
		return false;
	}
	
	/** Resets player data */
	protected boolean reset() {
		// clear 'player.yml' and 'players/'
		new File("plugins/Suitcase/player.yml").delete();
		if (new File("plugins/Suitcase/players/").exists()) {
			for (File playerFile : new File("plugins/Suitcase/players/").listFiles()) {
				playerFile.delete();
			}
		}
		if (plugin.load("plugins/Suitcase/player.yml")) {
			plugin.log(Action.RESET);
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/player.yml"));
			// re-register all online players
			plugin.con.registerAll();
			return true;
		}
		// saving new empty file failed
		return false;
	}
	
	/** Gets and loads player file  */
	protected boolean init() {
		if (plugin.load("plugins/Suitcase/player.yml")) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/player.yml"));
			
			// recalculate rating if rating.default changed
			for (String player : data.getKeys(false)) {
				calculateRating(player);
			}
			return true;
		}
		plugin.log(Action.INIT_ERROR, "YMLFileConfig");
		return false;
	}
	
	/** Disposes player file */
	protected boolean free() {
		if (plugin.load("plugins/Suitcase/player.yml", data)) {
			data = null;
			return true;
		}
		plugin.log(Action.FREE_ERROR, "YMLFileConfig");
		return false;
	}
}
