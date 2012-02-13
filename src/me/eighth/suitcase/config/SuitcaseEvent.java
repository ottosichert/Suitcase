package me.eighth.suitcase.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseConsole.Action;

public class SuitcaseEvent {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/** Stores default events */
	private Map<String, Object> defaults = new HashMap<String, Object>();
	
	/** Allocates ~/Suitcase/event.yml */
	private FileConfiguration data;
	
	/**
	 * Event system file interface
	 * @param plugin Instance of Suitcase
	 */
	public SuitcaseEvent(Suitcase plugin) {
		this.plugin = plugin;
		
		// set default events
		defaults.put("test1.condition", new ArrayList<String>(Arrays.asList("rate", "warn")));
		defaults.put("test1.executor", "server"); // all/server/sender/target
		defaults.put("test1.command", "broadcast {sender} has rated {target}.");
	}
	
	/**
	 * Returns a String
	 * @param key Config.yml key
	 */
	public String getString(String key) {
		return data.getString(key);
	}
	
	/**
	 * Returns an Integer
	 * @param key Config.yml key
	 */
	public int getInt(String key) {
		return data.getInt(key);
	}
	
	/**
	 * Returns a Boolean
	 * @param key Config.yml key
	 */
	public boolean getBoolean(String key) {
		return data.getBoolean(key);
	}
	
	/**
	 * Returns a Double
	 * @param key Config.yml key
	 */
	public double getDouble(String key) {
		return data.getDouble(key);
	}
	
	public void call(String event, String...arguments) {
		
		for (String eventName : data.getKeys(false)) {
			
			for (String condition : data.getStringList(eventName + ".condition")) {
				
				if (condition.contains(event)) {
					
					
					
					
				}
				
			}
			
		}
		
	}
	
	/** Resets event configuration */
	public boolean reset() {
		new File("plugins/Suitcase/event.yml").delete();
		if (plugin.file.load("plugins/Suitcase/event.yml", defaults, true)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/event.yml"));
			return true;
		}
		else {
			return false;
		}
	}

	/** Gets and reads event configuration */
	public boolean init() {
		if (plugin.file.load("plugins/Suitcase/event.yml", defaults)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/event.yml"));
			return true;
		}
		else {
			plugin.console.log(Action.INIT_ERROR, "SuitcaseEvent");
			return false;
		}
	}
	
	/** Disposes event configuration */
	public boolean free() {
		data = null;
		return true;
	}
	
	/** Reloads event configuration */
	public boolean reload() {
		if (free() && init()) {
			return true;
		}
		else {
			return false;
		}
	}
}
