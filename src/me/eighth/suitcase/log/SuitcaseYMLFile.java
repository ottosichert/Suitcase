package me.eighth.suitcase.log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.log.SuitcaseConsole.Action;

public class SuitcaseYMLFile {

	private Suitcase plugin;
	protected FileConfiguration data;
	
	public SuitcaseYMLFile(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	protected double getRating(String target) {
		if (isRegistered(target)) {
			return data.getDouble(target + ".rating");
		}
		else {
			return 0.; // player doesn't exist
		}
	}
	
	protected boolean setRating(String sender, String target, int rating) {
		// recalculate rating
		
		
		return true;
	}
	
	protected boolean isRegistered(String target) {
		if (isRegistered(target)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	protected boolean register(String target) {
		data.set(target + ".rating", plugin.cfg.data.getInt("mechanics.rating.default"));
		data.set(target + ".warnings", 0);
		if (plugin.file.load("plugins/Suitcase/player.yml", data)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/player.yml"));
			return true;
		}
		else {
			plugin.con.log(Action.FILE_SAVE_ERROR, new ArrayList<String>(Arrays.asList("player.yml", "FileNotLoaded")));
			return false;
		}
	}
	
	protected int getWarnings(String target) {
		if (isRegistered(target)) {
			return data.getInt(target + ".warnings");
		}
		else {
			return 0; // player doesn't exist
		}
	}
	
	protected boolean setWarnings(String sender, String target, boolean warning) {
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
				plugin.con.log(Action.FILE_SAVE_ERROR, new ArrayList<String>(Arrays.asList("player.yml", "FileNotLoaded")));
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	protected boolean init() {
		if (plugin.file.load("plugins/Suitcase/player.yml")) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/player.yml"));
			return true;
		}
		else {
			plugin.con.log(Action.INIT_ERROR, new ArrayList<String>(Arrays.asList("SuitcaseYMLFile", "FileNotLoaded")));
			return false;
		}
	}

	protected boolean free() {
		data = null;
		return true;
	}
}
