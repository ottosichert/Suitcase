package me.eighth.suitcase.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
	
	/** Available events */
	private String patterns;
	
	/** Separates OR-conditions */
	private String separator;
	
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
		defaults.put("kick.index", 1);
		defaults.put("tempban.condition", new ArrayList<String>(Arrays.asList("warn 2")));
		defaults.put("tempban.executor", "server");
		defaults.put("tempban.command", new ArrayList<String>(Arrays.asList("tempban {target} 3d")));
		defaults.put("tempban.index", 2);
		defaults.put("permaban.condition", new ArrayList<String>(Arrays.asList("warn 3")));
		defaults.put("permaban.executor", "server");
		defaults.put("permaban.command", new ArrayList<String>(Arrays.asList("banip {target_ip}")));
		defaults.put("permaban.index", 3);
		defaults.put("rate.condition", new ArrayList<String>(Arrays.asList("rate 0, rate 1, rate 2")));
		defaults.put("rate.executor", "target");
		defaults.put("rate.command", new ArrayList<String>(Arrays.asList("suicide")));
		defaults.put("rate.index", 0);
		
		// add available condition patterns
		patterns = "((rate|warn) [0-9]+)|forgive";
		
		// set separator
		separator = ", ";
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
	public void call(String action, String sender, String target) {
		// sort events
		ArrayList<String> order = new ArrayList<String>();
		int max = 0;
		
		for (int i = 0; i < data.getKeys(false).size(); i++) {
			// set max to first free event index
			for (int j = 0; j < data.getKeys(false).size(); j++) {
				if (!order.contains(data.getKeys(false).toArray()[j])) {
					max = j;
					break;
				}
			}
			// find highest index
			for (int j = max + 1; j < data.getKeys(false).size(); j++) {
				if ((data.getInt(data.getKeys(false).toArray()[j] + ".index") > data.getInt(data.getKeys(false).toArray()[max] + ".index"))
						&& (!order.contains(data.getKeys(false).toArray()[max].toString()))) {
					max = j;
				}
			}
			order.add(data.getKeys(false).toArray()[max].toString());
		}

		// parse event conditions
		CommandSender executor;
		String executorName = "";
		boolean match;

		for (String eventName : data.getKeys(false)) {
			// parse AND-conditions
			match = true;
			for (String conditionsAnd : data.getStringList(eventName + ".condition")) {
				for (String condition : conditionsAnd.split(separator)) {
					if (condition.matches(patterns)) {
						if (!condition.equalsIgnoreCase(action)) {
							match = false;
							break;
						}
					}
					else {
						plugin.log(Action.PROPERTY_BAD_TYPE, eventName + ".condition." + data.getStringList(eventName + ".condition").indexOf(conditionsAnd), "plugins/Suitcase/event.yml", condition);

						// repair conditions
						List<String> conditions = data.getStringList(eventName + ".condition");
						for (String repair : conditions) {
							conditions.set(conditions.indexOf(repair), Suitcase.getStringFromList(Suitcase.removeStringsFromList(repair.split(separator), condition), separator));
						}
						
						data.set(eventName + ".condition", conditions);
						
						match = false;
						break;
					}
				}
				if (!match) {
					break;
				}
			}
			
			if (!match) {
				continue;
			}
			// set command executor
			executorName = data.getString(eventName + ".executor");
			if (executorName.equalsIgnoreCase("server")) {
				executor = plugin.getServer().getConsoleSender();
			}
			else if (executorName.equalsIgnoreCase("sender")) {
				executor = plugin.getServer().getPlayer(sender);
			}
			else if (executorName.equalsIgnoreCase("target")) {
				executor = plugin.getServer().getPlayer(target);
			}
			else {
				if (!executorName.equalsIgnoreCase("none")) {
					plugin.log(Action.PROPERTY_BAD_TYPE, eventName + ".executor", "event.yml", executorName);
					data.set(eventName + ".executor", "none");
				}
				continue;
			}

			// execute command and send to console
			for (String command : data.getStringList(eventName + ".command")) {
				if (plugin.getServer().dispatchCommand(executor, plugin.msg.parse(String.valueOf(data.getStringList(eventName + ".command").indexOf(command)),
						"action", action,
						"sender", sender,
						"sender_ip", plugin.getServer().getPlayer(sender).getAddress().getHostName(),
						"target", target,
						"target_ip", plugin.getServer().getPlayer(target).getAddress().getHostName()))) {
					plugin.log(Action.EVENT_EXECUTED, eventName, action);
				}
				else {
					plugin.log(Action.PROPERTY_BAD_TYPE, eventName + ".command." + data.getStringList(eventName + ".command").indexOf(command), "plugins/Suitcase/event.yml", command);
					
					// remove command
					data.set(eventName + ".command", Suitcase.removeStringsFromList(data.getStringList(eventName + ".command"), command));
				}
			}
		}
	}
	
	/** Resets event configuration */
	public boolean reset() {
		if (new File("plugins/Suitcase/event.yml").delete() && plugin.load("plugins/Suitcase/event.yml", defaults, true)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/event.yml"));
			return true;
		}
		return false;
	}

	/** Gets event configuration */
	public boolean init() {
		if (plugin.load("plugins/Suitcase/event.yml", defaults)) {
			data = YamlConfiguration.loadConfiguration(new File("plugins/Suitcase/event.yml"));
			return true;
		}
		plugin.log(Action.INIT_ERROR, "SuitcaseEvent");
		return false;
	}
	
	/** Disposes event configuration */
	public boolean free() {
		if (plugin.load("plugins/Suitcase/event.yml", data)) {
			data = null;
			return true;
		}
		plugin.log(Action.FREE_ERROR, "SuitcaseEvent");
		return false;
	}
	
	/** Reloads event configuration */
	public boolean reload() {
		if (free() && init()) {
			return true;
		}
		return false;
	}
}
