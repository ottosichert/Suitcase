package me.eighth.suitcase.util;

import me.eighth.suitcase.Suitcase;

import org.bukkit.command.CommandSender;

public class SuitcasePermission {
	public boolean hasPermission (CommandSender sender, String permission) { // checks if sender has permission or is OP
		if (Boolean.valueOf((String)Suitcase.configKeys.get("mechanics.op-permissions")) && sender.isOp()) return true;
		else if (sender.hasPermission(permission)) return true;
		else return false;
	}
}
