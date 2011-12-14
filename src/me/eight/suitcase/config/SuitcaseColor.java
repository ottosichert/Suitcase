package me.eight.suitcase.config;

import me.eight.suitcase.Suitcase;
import me.eight.suitcase.event.SuitcaseLogger.ErrorType;

import org.bukkit.ChatColor;

public class SuitcaseColor {

	// define variables
	private ChatColor header, frame, text, info, error;
	
	// returns color value of specified type
	public ChatColor getColor (String type) {
		type = type.toLowerCase();
		if (type == "header") return header;
		else if (type == "frame") return frame;
		else if (type == "text") return text;
		else if (type == "info") return info;
		else if (type == "error") return error;
		else Suitcase.scLogger.sendError(ErrorType.COLOR_TYPE_NOT_FOUND, type);
		return null;
	}
	
	// sets specified color
	public void setColor (String type, String color) {
		type = type.toLowerCase();
		if (type == "header") header = colorName(color);
		else if (type == "frame") frame = colorName(color);
		else if (type == "text") text = colorName(color);
		else if (type == "info") info = colorName(color);
		else if (type == "error") error = colorName(color);
		else Suitcase.scLogger.sendError(ErrorType.COLOR_TYPE_NOT_FOUND, type);
	}
	
	// converts color name to ChatColor
	private ChatColor colorName (String color) {
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
