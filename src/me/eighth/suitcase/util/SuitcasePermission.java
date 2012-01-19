package me.eighth.suitcase.util;

import java.util.ArrayList;
import java.util.Arrays;

import me.eighth.suitcase.Suitcase;

import org.bukkit.entity.Player;

public class SuitcasePermission {
	
	private Suitcase plugin;
	
	private ArrayList<String> defaultPermissions = new ArrayList<String>(Arrays.asList("suitcase.help", "suitcase.info", "suitcase.rate"));
	
	public SuitcasePermission(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	// returns true if sender has permission, otherwise false
	public boolean hasPermission(String sender, String permission) {
		if (sender.equals("CONSOLE")) {
			return true;
		}
		else {
			Player player = plugin.getServer().getPlayer(sender);
			if (plugin.cfg.data.getBoolean("mechanics.op-permissions")) {
				if (player.isOp()) return true; // OPs have all permissions for suitcase if mechanics.op-permissions is enabled
				else if (defaultPermissions.contains(permission)) return true; // check if user has default permission
				else return false; // user doesn't have permission
			}
			else if (player.hasPermission(permission) || player.hasPermission("suitcase.*") || player.hasPermission("*")) return true; // check regular permissions (including super permissions)
			else return false;
		}
	}
}
