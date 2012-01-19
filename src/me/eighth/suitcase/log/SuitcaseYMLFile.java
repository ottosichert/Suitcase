package me.eighth.suitcase.log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.log.SuitcaseConsole.actionType;

public class SuitcaseYMLFile {

	private Suitcase plugin;
	protected FileConfiguration data;
	
	public SuitcaseYMLFile(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	protected double getRating(String target) {
		if (data.contains(target)) {
			return data.getInt(target + "rating");
		}
		else {
			return 0; // player doesn't exist
		}
	}
	
	protected boolean setRating(String sender, String target, int rating) {
		return true;
	}
	
	protected boolean isRegistered(String name) {
		if (data.contains(name)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	protected boolean init() {
		if (plugin.file.load("player.yml")) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/player.yml"));
			return true;
		}
		else {
			plugin.con.log(actionType.INIT_ERROR, new ArrayList<String>(Arrays.asList("SuitcaseYMLFile", "FileNotLoaded")));
			return false;
		}
	}

	protected boolean free() {
		data = null;
		return true;
	}

	protected boolean reload() {
		if (free() && init()) {
			return true;
		}
		else {
			return false;
		}
	}
}
