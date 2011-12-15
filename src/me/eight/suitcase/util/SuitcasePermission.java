package me.eight.suitcase.util;

import me.eight.suitcase.Suitcase;

import org.bukkit.entity.Player;

public class SuitcasePermission {
	
	public boolean hasPermission (Player player, String permission) {
		if (Suitcase.scConfig.getBool() && player.isOp()) return true;
	    else if (player.hasPermission(permission)) return true;
		else return false;
	}
}
