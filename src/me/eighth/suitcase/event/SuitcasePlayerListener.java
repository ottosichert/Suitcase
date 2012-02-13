package me.eighth.suitcase.event;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import me.eighth.suitcase.Suitcase;

public class SuitcasePlayerListener extends PlayerListener {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/**
	 * Register joining players and execute sign commands
	 * @param plugin Instance of Suitcase
	 */
	public SuitcasePlayerListener(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		// register joining player
		plugin.con.register(event.getPlayer().getName());
		// send join message
		plugin.msg.send(event.getPlayer(), "join", "player", event.getPlayer().getName(), "rating", String.valueOf(plugin.con.getRating(event.getPlayer().getName())), "warnings", String.valueOf(plugin.con.getWarnings(event.getPlayer().getName())));
	}
	
	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		// TODO: Sign interact
	}
}
