package me.eighth.suitcase.event;

import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import me.eighth.suitcase.Suitcase;

public class SuitcaseBlockListener implements Listener {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/**
	 * Handles sign creation
	 * @param plugin Instance of Suitcase
	 */
	public SuitcaseBlockListener(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	public void onSignChange(SignChangeEvent event) {
		if (!event.isCancelled() && plugin.cfg.getBoolean("sign.enable")) {
			// parse lines
			
		}
	}
	
	
}
