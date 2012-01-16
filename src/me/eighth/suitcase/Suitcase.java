package me.eighth.suitcase;

import java.util.ArrayList;
import java.util.Arrays;

import me.eighth.suitcase.config.SuitcaseConfig;
import me.eighth.suitcase.config.SuitcaseEvent;
import me.eighth.suitcase.config.SuitcaseMessage;
import me.eighth.suitcase.event.SuitcaseCommand;
import me.eighth.suitcase.log.SuitcaseConnector;
import me.eighth.suitcase.log.SuitcaseDatabase;
import me.eighth.suitcase.log.SuitcaseYMLFile;
import me.eighth.suitcase.util.SuitcaseConsole;
import me.eighth.suitcase.util.SuitcaseConsole.actionType;
import me.eighth.suitcase.util.SuitcaseFile;
import me.eighth.suitcase.util.SuitcasePermission;

import org.bukkit.plugin.java.JavaPlugin;

public class Suitcase extends JavaPlugin {
	
	public String name = "Leather";
	public String tag = "[Suitcase] - ";
	public final SuitcaseConfig config = new SuitcaseConfig(this);
	public final SuitcaseEvent event = new SuitcaseEvent(this);
	public final SuitcaseMessage message = new SuitcaseMessage(this);
	public final SuitcaseCommand command = new SuitcaseCommand(this);
	public final SuitcaseConnector connector = new SuitcaseConnector(this);
	public final SuitcaseDatabase database = new SuitcaseDatabase(this);
	public final SuitcaseYMLFile YMLfile = new SuitcaseYMLFile(this);
	public final SuitcaseConsole console = new SuitcaseConsole(this);
	public final SuitcaseFile file = new SuitcaseFile(this);
	public final SuitcasePermission permission = new SuitcasePermission(this);
	
	@Override
	public void onDisable() {
		// plugin unload
		console.sendAction(actionType.PLUGIN_DISABLE_START);
		
		// save and dispose configuration
		// if something returns false -> disable anyway
		if (!config.freeConfig()) {
			console.sendAction(actionType.PLUGIN_DISABLE_ERROR, new ArrayList<String>(Arrays.asList("freeConfigError")));
		}
		if (!message.freeMessages()) {
			console.sendAction(actionType.PLUGIN_DISABLE_ERROR, new ArrayList<String>(Arrays.asList("freeMessagesError")));
		}
		if (!event.freeEvent()) {
			console.sendAction(actionType.PLUGIN_DISABLE_ERROR, new ArrayList<String>(Arrays.asList("freeEventError")));
		}
		if (!connector.freeLog()) {
			console.sendAction(actionType.PLUGIN_DISABLE_ERROR, new ArrayList<String>(Arrays.asList("freeConnectorError")));
		}	
		
		// disabling finished, send to log
		console.sendAction(actionType.PLUGIN_DISABLE_FINISH);
	}
	
	@Override
	public void onEnable() {
		// plugin startup
		console.sendAction(actionType.PLUGIN_ENABLE_START);
		
		// set commands
		getCommand("suitcase").setExecutor(command);
		
		// load and check configuration
		if (!config.initConfig()) {
			console.sendAction(actionType.PLUGIN_ENABLE_ERROR, new ArrayList<String>(Arrays.asList("initConfigError")));
			disable();
			return;
		}
		else if (!message.initMessages()) {
			console.sendAction(actionType.PLUGIN_ENABLE_ERROR, new ArrayList<String>(Arrays.asList("initMessagesError")));
			disable();
			return;
		}
		else if (!event.initEvent()) {
			console.sendAction(actionType.PLUGIN_ENABLE_ERROR, new ArrayList<String>(Arrays.asList("initEventError")));
			disable();
			return;
		}
		else if (!connector.initLog()) {
			console.sendAction(actionType.PLUGIN_ENABLE_ERROR, new ArrayList<String>(Arrays.asList("initConnectorError")));
			disable();
			return;
		}
		else {		
			// enabling finished, send to log
			console.sendAction(actionType.PLUGIN_ENABLE_FINISH);
		}
	}
	
	public void reload() {
		// plugin reload
		console.sendAction(actionType.PLUGIN_RELOAD_START);
		
		// reload configuration
		if (!config.reloadConfig()) {
			console.sendAction(actionType.PLUGIN_RELOAD_ERROR, new ArrayList<String>(Arrays.asList("reloadConfigError")));
			disable();
			return;
		}
		else if (!message.reloadMessages()) {
			console.sendAction(actionType.PLUGIN_RELOAD_ERROR, new ArrayList<String>(Arrays.asList("reloadMessagesError")));
			disable();
			return;
		}
		else if (!event.reloadEvent()) {
			console.sendAction(actionType.PLUGIN_RELOAD_ERROR, new ArrayList<String>(Arrays.asList("reloadEventError")));
			disable();
			return;
		}
		else if (!connector.reloadLog()) {
			console.sendAction(actionType.PLUGIN_RELOAD_ERROR, new ArrayList<String>(Arrays.asList("reloadConnectorError")));
			disable();
			return;
		}
		else {		
			// reloading finished, send to log
			console.sendAction(actionType.PLUGIN_RELOAD_FINISH);
		}
	}
	
	// disable plugin due to internal error
	public void disable() {
		setEnabled(false);
	}
}
