package me.eighth.suitcase.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseConsole.Action;

public class SuitcaseEvent {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/** Stores default events */
	private Map<String, Object> defaults = new HashMap<String, Object>();
	
	/**  */
	private ArrayList<String> conditions = new ArrayList<String>();
	
	/** Allocates ~/Suitcase/event.yml */
	private FileConfiguration data;
	
	/**
	 * Event system file interface
	 * @param plugin Instance of Suitcase
	 */
	public SuitcaseEvent(Suitcase plugin) {
		this.plugin = plugin;
		
		// set default events
		defaults.put("kick.condition", new ArrayList<String>(Arrays.asList("warn 1")));
		defaults.put("kick.executor", "server");
		defaults.put("kick.command", new ArrayList<String>(Arrays.asList("kick {target} You have been warned.")));
		defaults.put("kick.index", 0);
		defaults.put("tempban.condition", new ArrayList<String>(Arrays.asList("warn 2")));
		defaults.put("tempban.executor", "server");
		defaults.put("tempban.command", new ArrayList<String>(Arrays.asList("tempban {target} 3d")));
		defaults.put("tempban.index", 1);
		defaults.put("permaban.condition", new ArrayList<String>(Arrays.asList("warn 3")));
		defaults.put("permaban.executor", "server");
		defaults.put("permaban.command", new ArrayList<String>(Arrays.asList("banip {target_ip}")));
		defaults.put("permaban.index", 2);
		
		// add available conditions
		conditions.add("rate");
		conditions.add("warn");
		conditions.add("forgive");
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
	
	/**
	 * Calls all matching events for an action
	 * @param action Command action
	 * @param sender Name of CommandSender
	 * @param target Targeted player
	 * @param argument Matching argument
	 */
	public void call(String action, String sender, String target, String argument) {
		// sort events
		ArrayList<String> order = new ArrayList<String>();
		int max = 0;
		
		for (int i = 0; i < data.getKeys(false).size(); i++) {
			// find highest index
			for (int j = 0; j < data.getKeys(false).size(); j++) {
				// set max to first free event index
				for (int k = 0; k < data.getKeys(false).size(); k++) {
					if (!order.contains(data.getKeys(false).toArray()[k])) {
						max = k;
						break;
					}
				}
				if ((data.getInt(data.getKeys(false).toArray()[j] + ".index") > data.getInt(data.getKeys(false).toArray()[max] + ".index"))
						&& (!order.contains(data.getKeys(false).toArray()[max].toString()))) {
					max = j;
				}
			}
			order.add(data.getKeys(false).toArray()[max].toString());
		}
		
		// parse event conditions
		boolean conditions = false;
		CommandSender executor;
		String executorName = "";
		
		for (String eventName : data.getKeys(false)) {
			for (String conditionsAnd : data.getStringList(eventName + ".condition")) {
				// parse AND conditions
				for (String condition : conditionsAnd.split(" && ")) {
					String[] split = condition.split(" ");
					if ((split.length == 1 && split[0].equals(action)) || (split.length == 2 && split[0].equals(action) && split[1].equals(argument))) {
						conditions = true;
					}
					else {
						conditions = false;
						break;
					}
				}
				
				if (conditions) {
					for (String command : data.getStringList(eventName + ".command")) {
						executorName = data.getString(eventName + ".executor");
						if (executorName.equals("server")) {
							executor = plugin.getServer().getConsoleSender();
						}
						else if (executorName.equals("sender")) {
							executor = plugin.getServer().getPlayer(sender);
						}
						else if (executorName.equals("target")) {
							executor = plugin.getServer().getPlayer(target);
						}
						else {
							break;
						}
						// execute command and send to console
						plugin.getServer().dispatchCommand(executor, plugin.msg.parse(command,
								"event", action,
								"sender", sender,
								"target", target,
								"argument", argument,
								"sender_ip", plugin.getServer().getPlayer(sender).getAddress().getHostName(),
								"target_ip", plugin.getServer().getPlayer(target).getAddress().getHostName()));
						if (plugin.cfg.getBoolean("log.console.event")) {
							plugin.log(Action.EVENT_EXECUTED, eventName, action);
						}
					}
				}
			}
		}
	}
	
	/** Resets event configuration */
	public boolean reset() {
		new File("plugins/Suitcase/event.yml").delete();
		if (plugin.load("plugins/Suitcase/event.yml", defaults, true)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/event.yml"));
			return true;
		}
		else {
			return false;
		}
	}

	/** Gets and reads event configuration */
	public boolean init() {
		if (plugin.load("plugins/Suitcase/event.yml", defaults)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/event.yml"));
			return true;
		}
		else {
			plugin.log(Action.INIT_ERROR, "SuitcaseEvent");
			return false;
		}
	}
	
	/** Disposes event configuration */
	public boolean free() {
		if (plugin.load("plugins/Suitcase/event.yml", data)) {
			data = null;
			return true;
		}
		else {
			plugin.log(Action.FREE_ERROR, "SuitcaseEvent");
			return false;
		}
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
