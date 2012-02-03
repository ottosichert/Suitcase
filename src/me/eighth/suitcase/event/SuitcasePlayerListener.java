package me.eighth.suitcase.event;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import me.eighth.suitcase.Suitcase;

public class SuitcasePlayerListener extends PlayerListener {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/** Handles onPlayerJoin to register new players */
	public SuitcasePlayerListener(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (!plugin.con.isRegistered(event.getPlayer().getName())) {
			plugin.con.register(event.getPlayer().getName());
		}
		plugin.msg.send(event.getPlayer(), "join", "player", event.getPlayer().getName(), "rating", String.valueOf(plugin.con.getRating(event.getPlayer().getName())), "warnings", String.valueOf(plugin.con.getWarnings(event.getPlayer().getName())));
	}
}
