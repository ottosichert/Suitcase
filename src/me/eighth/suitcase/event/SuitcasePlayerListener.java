package me.eighth.suitcase.event;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerPreLoginEvent;

import me.eighth.suitcase.Suitcase;

public class SuitcasePlayerListener extends PlayerListener {
	
	private Suitcase plugin;
	
	public SuitcasePlayerListener(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		if (!plugin.con.isRegistered(event.getName())) {
			plugin.con.register(event.getName());
		}
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		plugin.msg.sendMessage(event.getPlayer(), plugin.msg.parse(plugin.msg.data.getString("join"), "player", event.getPlayer().getName(), "", "rating", String.valueOf(plugin.con.getRating(event.getPlayer().getName())), "", "warnings", String.valueOf(plugin.con.getWarnings(event.getPlayer().getName()))));
	}
}
