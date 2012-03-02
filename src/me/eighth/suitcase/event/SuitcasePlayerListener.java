package me.eighth.suitcase.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.eighth.suitcase.Suitcase;

public class SuitcasePlayerListener implements Listener {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/**
	 * Register joining players and execute sign commands
	 * @param plugin Instance of Suitcase
	 */
	public SuitcasePlayerListener(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		// register joining player
		plugin.con.register(event.getPlayer().getName());
		// send join message
		plugin.msg.send(event.getPlayer(), "join",
				"player", event.getPlayer().getName(),
				"rating", String.valueOf(plugin.con.getRating(event.getPlayer().getName())),
				"warnings", String.valueOf(plugin.con.getWarnings(event.getPlayer().getName())));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		// TODO: Sign interact
	}
}
