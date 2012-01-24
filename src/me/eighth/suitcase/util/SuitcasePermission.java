package me.eighth.suitcase.util;

import java.util.ArrayList;
import java.util.Arrays;

import me.eighth.suitcase.Suitcase;

import org.bukkit.OfflinePlayer;

public class SuitcasePermission {
	
	private Suitcase plugin;
	
	private ArrayList<String> defaultPermissions = new ArrayList<String>(Arrays.asList("suitcase.help", "suitcase.broadcast", "suitcase.rate"));
	
	public SuitcasePermission(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	// returns true if sender has permission, otherwise false
	public boolean hasPermission(String sender, String permission) {
		if (sender.equals("CONSOLE")) {
			return true;
		}
		else {
			OfflinePlayer player = plugin.getServer().getOfflinePlayer(sender);
			if (player != null) {
				if (plugin.cfg.data.getBoolean("mechanics.op-permissions")) {
					if (player.isOp()) {
						return true; // OPs have all permissions for suitcase if mechanics.op-permissions is enabled
					}
					else if (defaultPermissions.contains(permission)) {
						return true; // check if user has default permission
					}
					else {
						return false; // user doesn't have permission
					}
				}
				else if (plugin.getServer().getPluginManager().getPermissionSubscriptions(permission).contains(sender)) {
					return true; // get player's permission
				}
				else {
					return false;
				}
			}
			else {
				return false; // player not found
			}
		}
	}
}
