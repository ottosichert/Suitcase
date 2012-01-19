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
	
	protected int getRating(String target) {
		if (data.contains(target)) {
			File playerFile = new File("plugins/Suitcase/player/" + target + ".yml");
			if (playerFile.exists()) {
				FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
				
				// get rating
				if (playerData.contains("rating")) {
					return playerData.getInt("rating");
				}
				else {
					// rating is empty
					return 0;
				}
			}
			else {
				// file doesn't exist -> player doesn't exist
				return 0;
			}
		}
		else {
			return 0; // player doesn't exist
		}
	}
	
	protected boolean setRating(String sender, String target, int rating) {
		return true;
	}
	
	protected boolean init() {
		if (plugin.file.load("plugins/Suitcase/player.yml")) {
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
