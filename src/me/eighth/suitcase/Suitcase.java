package me.eighth.suitcase;

import java.util.ArrayList;

import me.eighth.suitcase.config.SuitcaseConfig;
import me.eighth.suitcase.config.SuitcaseEvent;
import me.eighth.suitcase.config.SuitcaseMessage;
import me.eighth.suitcase.event.SuitcaseCommandExecutor;
import me.eighth.suitcase.event.SuitcasePlayerListener;
import me.eighth.suitcase.log.SuitcaseConnector;
import me.eighth.suitcase.log.SuitcaseDatabase;
import me.eighth.suitcase.log.SuitcaseYMLFile;
import me.eighth.suitcase.util.SuitcaseBroadcast;
import me.eighth.suitcase.util.SuitcaseConsole;
import me.eighth.suitcase.util.SuitcaseFile;
import me.eighth.suitcase.util.SuitcasePermission;
import me.eighth.suitcase.util.SuitcaseConsole.Action;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

public class Suitcase extends JavaPlugin {
	
	/** Current version name */
	public final String name = "Leather";
	
	/** Prefix for console notifications */
	public final String pluginTag = "[Suitcase] ";
	
	/** Prefix for player commands */
	public final String playerTag = "[PLAYER_COMMAND] ";
	
	/** Prefix for console commands */
	public final String consoleTag = "[CONSOLE_COMMAND] ";
	
	// Suitcase classes
	public final SuitcaseConfig cfg = new SuitcaseConfig(this);
	public final SuitcaseEvent event = new SuitcaseEvent(this);
	public final SuitcaseMessage msg = new SuitcaseMessage(this);
	private final SuitcaseCommandExecutor command = new SuitcaseCommandExecutor(this);
	private final SuitcasePlayerListener player = new SuitcasePlayerListener(this);
	public final SuitcaseBroadcast broad = new SuitcaseBroadcast(this);
	public final SuitcaseConnector con = new SuitcaseConnector(this);
	public final SuitcaseConsole console = new SuitcaseConsole(this);
	public final SuitcaseDatabase db = new SuitcaseDatabase(this);
	public final SuitcaseYMLFile yml = new SuitcaseYMLFile(this);
	
	/** Manages file writing */
	public final SuitcaseFile file = new SuitcaseFile(this);
	
	/** Handles permissions for commands */
	public final SuitcasePermission perm = new SuitcasePermission(this);

	@Override
	public void onEnable() {
		// plugin startup
		console.log(Action.PLUGIN_ENABLE_START);
		
		// set command executors and event listeners
		getCommand("suitcase").setExecutor(command);
		
		// register events
		getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, player, Event.Priority.Lowest, this);
		
		// load and check configuration
		if (!cfg.init()) {
			console.log(Action.PLUGIN_ENABLE_ERROR, "ConfigInitFailed");
			disable();
			return;
		}
		else if (!msg.init()) {
			console.log(Action.PLUGIN_ENABLE_ERROR, "MessageInitFailed");
			disable();
			return;
		}
		else if (!event.init()) {
			console.log(Action.PLUGIN_ENABLE_ERROR, "EventInitFailed");
			disable();
			return;
		}
		else if (!con.init()) {
			console.log(Action.PLUGIN_ENABLE_ERROR, "ConnectorInitFailed");
			disable();
			return;
		}
		
		// add online players
		for (Player player : getServer().getOnlinePlayers()) {
			if (!con.isRegistered(player.getName())) {
				if (con.register(player.getName())) {
					console.log(Action.PLAYER_REGISTER, player.getName());
				}
			}
		}
		
		// enabling finished, send to log
		console.log(Action.PLUGIN_ENABLE_FINISH);
	}
	
	@Override
	public void onDisable() {
		// plugin unload
		console.log(Action.PLUGIN_DISABLE_START);
		
		// save and dispose configuration in reverse order
		if (!con.free()) {
			console.log(Action.PLUGIN_DISABLE_ERROR, "ConnectorFreeFailed");
			return;
		}
		else if (!event.free()) {
			console.log(Action.PLUGIN_DISABLE_ERROR, "EventFreeFailed");
			return;
		}
		else if (!msg.free()) {
			console.log(Action.PLUGIN_DISABLE_ERROR, "MessageFreeFailed");
			return;
		}
		else if (!cfg.free()) {
			console.log(Action.PLUGIN_DISABLE_ERROR, "ConfigFreeFailed");
			return;
		}
		
		// disabling finished, send to log
		console.log(Action.PLUGIN_DISABLE_FINISH);
	}
	
	/** Gets string from ArrayList and removes brackets */
	public String getString(ArrayList<String> list, boolean commas) {
		if (commas) {
			return list.toString().replaceAll("^\\[|\\]$", "");
		}
		else {
			return list.toString().replaceAll("^\\[|\\]$|, ", " ").trim();
		}
	}
	
	/** Send a debug message to console */
	public void debug(String...arguments) {
		console.log(Action.DEBUG, arguments);
	}
	
	/** Reload plugin */
	public void reload() {
		// plugin reload
		console.log(Action.PLUGIN_RELOAD_START);
		
		// reload settings and connector
		if (!cfg.reload()) {
			console.log(Action.PLUGIN_RELOAD_ERROR, "ConfigReloadFailed");
			disable();
			return;
		}
		else if (!msg.reload()) {
			console.log(Action.PLUGIN_RELOAD_ERROR, "MessageReloadFailed");
			disable();
			return;
		}
		else if (!event.reload()) {
			console.log(Action.PLUGIN_RELOAD_ERROR, "EventReloadFailed");
			disable();
			return;
		}
		else if (!con.reload()) {
			console.log(Action.PLUGIN_RELOAD_ERROR, "ConnectorReloadFailed");
			disable();
			return;
		}
		
		// reloading finished, send to log
		console.log(Action.PLUGIN_RELOAD_FINISH);
	}
	
	/** Reset configuration and player data */
	public void reset() {
		// reset player ratings
		cfg.reset();
		msg.reset();
		event.reset();
		con.reset();
	}
	
	/** Disable plugin due to an internal error */
	public void disable() {
		setEnabled(false);
	}
}
