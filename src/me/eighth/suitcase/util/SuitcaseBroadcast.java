package me.eighth.suitcase.util;

import org.bukkit.entity.Player;

import me.eighth.suitcase.Suitcase;

public class SuitcaseBroadcast {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/** Sends messages to all authorized players */
	public SuitcaseBroadcast(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	public void send(String key, String...arguments) {
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (plugin.perm.hasPermission(player.getName(), "suitcase.broadcast")) {
				plugin.msg.send(player, key, arguments);
			}
		}
	}
}
