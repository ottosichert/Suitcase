package me.eight.suitcase.util;

import me.eight.suitcase.Suitcase;

import org.bukkit.command.CommandSender;

public class SuitcasePermission {
	public boolean hasPermission (CommandSender sender, String permission) { // checks if sender has permission or is OP
		if (Suitcase.cfMechanics.op_permissions && sender.isOp()) return true;
		else if (sender.hasPermission(permission)) return true;
		else return false;
	}
}
