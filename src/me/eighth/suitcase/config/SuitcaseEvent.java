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

		eventDefault.put("event.test.condition", "");
		eventDefault.put("event.test.action", "[test, test2]");
		eventDefault.put("action.test.execute", "server"); // server/sender/target
		eventDefault.put("action.test.permission", "op"); // op/target
		eventDefault.put("action.test.command", "broadcast {sender} has rated {target}.");
		eventDefault.put("action.test2.execute", "target");
		eventDefault.put("action.test2.permission", "op");
		eventDefault.put("action.test2.command", "give {target} cookie 1");
		
		
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
