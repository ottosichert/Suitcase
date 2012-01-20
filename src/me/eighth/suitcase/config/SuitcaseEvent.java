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
	private Map<String, Object> eventDefault = new HashMap<String, Object>();
	private Map<String, ArrayList<String>> eventTag = new HashMap<String, ArrayList<String>>();
	public FileConfiguration data;
	
	public SuitcaseEvent(Suitcase plugin) {
		this.plugin = plugin;
		
		// set default events
		// TODO: Make them optional.
		eventDefault.put("event.rate.condition", "playerRate && (timeTick >= 1000)");
		eventDefault.put("event.rate.action", new ArrayList<String>(Arrays.asList("broadcast()", "thanks()")));
		eventDefault.put("event.warn.condition", "playerWarn");
		eventDefault.put("event.warn.action", "kick('You have been warned.')");
		eventDefault.put("action.broadcast.execute", "server"); // all/server/sender/target
		eventDefault.put("action.broadcast.command", "broadcast {sender} has rated {target}.");
		eventDefault.put("action.thanks.execute", "target");
		eventDefault.put("action.thanks.command", "msg {sender} &7Thank you!");
		eventDefault.put("action.kick.execute", "sender");
		eventDefault.put("action.kick.command", "kick {target} {0}");
		
		// define eventTags for event.NAME.condition
		eventTag.put("playerRate", new ArrayList<String>(Arrays.asList("positive", "negative"))); // 'all' is always possible
		eventTag.put("playerWarn", new ArrayList<String>(Arrays.asList("warn", "forgive")));
		eventTag.put("pluginStatus", new ArrayList<String>(Arrays.asList("enable", "reload", "disable")));
	}
	
	// get event file
	public boolean init() {
		if (plugin.file.load("plugins/Suitcase/event.yml", eventDefault)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/event.yml"));
			return true;
		}
		else {
			plugin.con.log(Action.INIT_ERROR, new ArrayList<String>(Arrays.asList("SuitcaseEvent", "FileNotLoaded")));
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
