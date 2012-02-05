package me.eighth.suitcase.event;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseConsole.Action;

public class SuitcasePlayerListener extends PlayerListener {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/**
	 * Handles onPlayerJoin to register new players
	 * @param plugin Instance of Suitcase
	 */
	public SuitcasePlayerListener(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		// get joining player and register him
		if (!plugin.con.isRegistered(event.getPlayer().getName())) {
			if (plugin.con.register((event.getPlayer().getName()))) {
				plugin.console.log(Action.PLAYER_REGISTER, (event.getPlayer().getName()));
			}
		}
		// send join message
		plugin.msg.send(event.getPlayer(), "join", "player", event.getPlayer().getName(), "rating", String.valueOf(plugin.con.getRating(event.getPlayer().getName())), "warnings", String.valueOf(plugin.con.getWarnings(event.getPlayer().getName())));
	}
}
