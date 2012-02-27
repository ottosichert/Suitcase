package me.eighth.suitcase.util;

import java.util.ArrayList;
import java.util.Arrays;

import me.eighth.suitcase.Suitcase;

import org.bukkit.OfflinePlayer;

public class SuitcasePermission {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/** Stores default permissions for players if mechanics.op-permissions is enabled */
	private ArrayList<String> defaultPermissions;
	
	/**
	 * Handles player and console permissions
	 * @param plugin Instance of Suitcase
	 */
	public SuitcasePermission(Suitcase plugin) {
		this.plugin = plugin;
		
		// load default permissions
		defaultPermissions = new ArrayList<String>(Arrays.asList("help", "broadcast", "rate"));
	}
	
	/**
	 * Checks if player or console has permission to a Suitcase action
	 * @param sender Command sender or target
	 * @param permission Suitcase permission
	 */
	public boolean hasPermission(String sender, String permission) {
		if (sender.equals("CONSOLE")) { // console has permission to all commands
			return true;
		}
		else {
			OfflinePlayer player = plugin.getServer().getOfflinePlayer(sender); // we need permissions for offline players as well
			if (player != null) {
				if (plugin.cfg.getBoolean("mechanics.op-permissions")) {
					if (player.isOp()) {
						return true; // OPs have all permissions for suitcase if mechanics.op-permissions is enabled
					}
					else if (defaultPermissions.contains(permission)) {
						return true; // check if user has default permission
					}
					else {
						return false; // regular user doesn't have permission
					}
				}
				else if (plugin.getServer().getPluginManager().getPermissionSubscriptions("suitcase." + permission).contains(sender)) {
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
