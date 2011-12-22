package me.eighth.suitcase.util;

import java.util.ArrayList;
import java.util.Arrays;

import me.eighth.suitcase.Suitcase;

import org.bukkit.command.CommandSender;

public class SuitcasePermission {
	// default permissions for users if mecanics.op-permissions is enabled
	private ArrayList<String> defaultPermissions = (ArrayList<String>) Arrays.asList("suitcase.help", "suitcase.info", "suitcase.rate");
	
	// returns true if sender has permission, otherwise false
	public boolean hasPermission (CommandSender sender, String permission) {
		if (Suitcase.configKeys.getBoolean("mechanics.op-permissions")) {
			if (sender.isOp()) return true; // OPs have all permissions for suitcase if mechanics.op-permissions is enabled
			else if (defaultPermissions.contains(permission)) return true; // check if user has default permission
			else return false; // user doesn't have permission
		}
		else if (sender.hasPermission(permission) || sender.hasPermission("suitcase.*") || sender.hasPermission("*")) return true; // check regular permissions (including super permissions)
		else return false;
	}
}
