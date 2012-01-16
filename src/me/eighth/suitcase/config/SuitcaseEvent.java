package me.eighth.suitcase.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.eighth.suitcase.Suitcase;

public class SuitcaseEvent {
	
	private Suitcase plugin;
	private Map<String, Object> eventDefault = new HashMap<String, Object>();
	public FileConfiguration config;
	
	public SuitcaseEvent(Suitcase plugin) {
		this.plugin = plugin;

		eventDefault.put("event.foobar.condition", "{player} rate {sender}");
		eventDefault.put("event.foobar.action", "foo bar");
		eventDefault.put("event.blah.condition", "{player} rate {sender}");
		eventDefault.put("event.blah.action", "foo bar");
		eventDefault.put("action.foo.execute", "server"); // server/sender/target
		eventDefault.put("action.foo.command", "broadcast {sender} has rated {target}.");
		eventDefault.put("action.bar.execute", "target");
		eventDefault.put("action.bar.command", "msg {sender} &7Thank you!");
		
		
		eventDefault.put("", "");
		
	}
	
	// get event file
	public boolean initEvent() {
		if (plugin.file.loadFile("event.yml", eventDefault)) {
			config = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/event.yml"));
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean freeEvent() {
		config = null;
		return true;
	}
	
	public boolean reloadEvent() {
		if (freeEvent() && initEvent()) {
			return true;
		}
		else {
			return false;
		}
	}
	
}
