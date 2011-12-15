package me.eight.suitcase.config;

import me.eight.suitcase.Suitcase;
import me.eight.suitcase.util.SuitcaseLog.ErrorType;

import org.bukkit.ChatColor;

public class SuitcaseColor {
	
	// available elements
	public enum ElementType {
		HEADER,
		FRAME,
		TEXT,
		INFO,
		COMMAND,
		ERROR
	}
	// returns color value of specified type
	public ChatColor getColor (ElementType type) {
		switch (type) {
		case HEADER:
			return colorName(Suitcase.scConfig.cfColor.header);
		case FRAME:
			return colorName(Suitcase.scConfig.cfColor.frame);
		case TEXT:
			return colorName(Suitcase.scConfig.cfColor.text);
		case INFO:
			return colorName(Suitcase.scConfig.cfColor.info);
		case COMMAND:
			return colorName(Suitcase.scConfig.cfColor.command);
		case ERROR:
			return colorName(Suitcase.scConfig.cfColor.error);
		default:
			Suitcase.scLogger.sendError(ErrorType.TYPE_NOT_FOUND, type.toString());
			return null;
		}
	}
	
	// sets specified color
	public void setColor (ElementType type, String color) {
		switch (type) {
		case HEADER:
			Suitcase.scConfig.cfColor.header = color;
		case FRAME:
			Suitcase.scConfig.cfColor.frame = color;
		case TEXT:
			Suitcase.scConfig.cfColor.text = color;
		case INFO:
			Suitcase.scConfig.cfColor.info = color;
		case COMMAND:
			Suitcase.scConfig.cfColor.command = color;
		default:
			Suitcase.scLogger.sendError(ErrorType.TYPE_NOT_FOUND, type.toString());
		}
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
