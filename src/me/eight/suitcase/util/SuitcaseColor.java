package me.eight.suitcase.util;

import me.eight.suitcase.Suitcase;
import me.eight.suitcase.util.SuitcaseLog.ErrorType;

import org.bukkit.ChatColor;

public class SuitcaseColor {
	
	// returns colored message
	/* Not used :/
	public String parseColor(String message) {
		String hex = "0123456789abcdef";
		String[] split = message.split("&");
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
	*/
	
	// converts color name to ChatColor 
	public ChatColor colorName(String color) { // TODO: merge this and variable section
		color = color.toLowerCase();
		if (color == "aqua") return ChatColor.AQUA;
		else if (color == "black") return ChatColor.BLACK;
		else if (color == "blue") return ChatColor.BLUE;
		else if (color == "dark_auqa") return ChatColor.DARK_AQUA;
		else if (color == "dark_blue") return ChatColor.DARK_BLUE;
		else if (color == "dark_gray") return ChatColor.DARK_GRAY;
		else if (color == "dark_green") return ChatColor.DARK_GREEN;
		else if (color == "dark_purple") return ChatColor.DARK_PURPLE;
		else if (color == "dark_red") return ChatColor.DARK_RED;
		else if (color == "gold") return ChatColor.GOLD;
		else if (color == "gray") return ChatColor.GRAY;
		else if (color == "green") return ChatColor.GREEN;
		else if (color == "light_purple") return ChatColor.LIGHT_PURPLE;
		else if (color == "red") return ChatColor.RED;
		else if (color == "white") return ChatColor.WHITE;
		else if (color == "yellow") return ChatColor.YELLOW;
		else Suitcase.scLogger.sendError(ErrorType.COLOR_NOT_FOUND, color);
		return null;
	}
}
