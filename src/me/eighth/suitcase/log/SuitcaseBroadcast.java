package me.eighth.suitcase.log;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.eighth.suitcase.Suitcase;

public class SuitcaseBroadcast {
	
	private Suitcase plugin;
	
	public SuitcaseBroadcast(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	protected void broadcastMessage(ArrayList<String> lines) {
		for (Player player : plugin.getServer().getOnlinePlayers()) {
			if (plugin.perm.hasPermission(player.getName(), "suitcase.broadcast")) {
				plugin.msg.sendMessage(player, lines);
			}
		}
	}
}
