package me.eighth.suitcase.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.log.SuitcaseConsole.Action;

public class SuitcaseEvent {
	
	private Suitcase plugin;
	private Map<String, Object> defaults = new HashMap<String, Object>();
	private Map<String, ArrayList<String>> eventTag = new HashMap<String, ArrayList<String>>();
	public FileConfiguration data;
	
	public SuitcaseEvent(Suitcase plugin) {
		this.plugin = plugin;
		
		// set default events
		// TODO: Make them optional.
		defaults.put("event.rate.condition", "playerRate && (timeTick >= 1000)");
		defaults.put("event.rate.action", new ArrayList<String>(Arrays.asList("broadcast()", "thanks()")));
		defaults.put("event.warn.condition", "playerWarn");
		defaults.put("event.warn.action", "kick('You have been warned.')");
		defaults.put("action.broadcast.execute", "server"); // all/server/sender/target
		defaults.put("action.broadcast.command", "broadcast {sender} has rated {target}.");
		defaults.put("action.thanks.execute", "target");
		defaults.put("action.thanks.command", "msg {sender} &7Thank you!");
		defaults.put("action.kick.execute", "sender");
		defaults.put("action.kick.command", "kick {target} {0}");
		
		// define eventTags for event.NAME.condition
		eventTag.put("playerRate", new ArrayList<String>(Arrays.asList("positive", "negative"))); // 'all' is always possible
		eventTag.put("playerWarn", new ArrayList<String>(Arrays.asList("warn", "forgive")));
		eventTag.put("pluginStatus", new ArrayList<String>(Arrays.asList("enable", "reload", "disable")));
	}
	
	public void reset() {
		new File("plugins/Suitcase/event.yml").delete();
		if (plugin.file.load("plugins/Suitcase/event.yml", defaults, true)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/event.yml"));
		}
		else {
			plugin.con.log(Action.FILE_SAVE_ERROR, "event.yml", "FileNotLoaded");
		}
	}
	
	// get event file
	public boolean init() {
		if (plugin.file.load("plugins/Suitcase/event.yml", defaults)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/event.yml"));
			return true;
		}
		else {
			plugin.con.log(Action.INIT_ERROR, "SuitcaseEvent", "FileNotLoaded");
			return false;
		}
	}
	
	public boolean free() {
		data = null;
		return true;
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
