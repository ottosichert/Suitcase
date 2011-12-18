package me.eighth.suitcase.util;

import org.bukkit.ChatColor;

public class SuitcaseColor {
	
	// returns colored message
	public String parseColor(String message) {
		String hex = "0123456789abcdef";
		String[] split = message.split("&[^&]");
		String result = split[0];
		String firstChar = "";
		for (int i = 1; i < split.length; i++) {
			firstChar = split[i].substring(0, 1).toLowerCase();
			if (hex.contains(firstChar)) {
				result += ChatColor.getByCode(hex.indexOf(firstChar)) + split[i].substring(1);
			}
			else {
				result += "&" + split[i];
			}
		}
		return result;
	}
}
