package me.eighth.suitcase.event;

import java.util.ArrayList;

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
		ArrayList<String> lines = new ArrayList<String>();
		for (int i = 0; i < plugin.msg.data.getStringList("join").size(); i++) {
			lines.add(plugin.msg.parse(plugin.msg.data.getStringList("join").get(i), "player", event.getPlayer().getName(), "", "rating", String.valueOf(plugin.con.getRating(event.getPlayer().getName())), "", "warnings", String.valueOf(plugin.con.getWarnings(event.getPlayer().getName()))));
		}
		plugin.msg.sendMessage(event.getPlayer(), lines);
	}
}
