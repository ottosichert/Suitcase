package me.eight.suitcase.config;

import org.bukkit.ChatColor;

import me.eight.suitcase.Suitcase;

public class SuitcaseColor {
	public static Suitcase plugin;

	public SuitcaseColor(Suitcase instance) {
		plugin = instance;
	}

	public enum ColorType { // Provides different display types
		HEADER,
		FRAME,
		TEXT,
		INFO,
		WARNING
	}
	
	public ChatColor getColor (ColorType type) { // Returns color value of specified type
		switch (type) {
		case HEADER:
			return colorName(plugin.getConfig().getString("color.header"));
			
		case FRAME:
			return colorName(plugin.getConfig().getString("color.frame"));
			
		case TEXT:
			return colorName(plugin.getConfig().getString("color.text"));
			
		case INFO:
			return colorName(plugin.getConfig().getString("color.info"));
			
		case WARNING:
			return colorName(plugin.getConfig().getString("color.warning"));
		}
		return ChatColor.AQUA;
	}
	
	public ChatColor colorName (String name) { // Converts color name to ChatColor
		name = name.toLowerCase();
		if (name == "aqua") return ChatColor.AQUA;
		else if (name == "black") return ChatColor.BLACK;
		else if (name == "blue") return ChatColor.BLUE;
		else if (name == "dark_auqa") return ChatColor.DARK_AQUA;
		else if (name == "dark_blue") return ChatColor.DARK_BLUE;
		else if (name == "dark_gray") return ChatColor.DARK_GRAY;
		else if (name == "dark_green") return ChatColor.DARK_GREEN;
		else if (name == "dark_purple") return ChatColor.DARK_PURPLE;
		else if (name == "dark_red") return ChatColor.DARK_RED;
		else if (name == "gold") return ChatColor.GOLD;
		else if (name == "gray") return ChatColor.GRAY;
		else if (name == "green") return ChatColor.GREEN;
		else if (name == "light_purple") return ChatColor.LIGHT_PURPLE;
		else if (name == "red") return ChatColor.RED;
		// else if (name == "white") return ChatColor.WHITE;
		else if (name == "yellow") return ChatColor.YELLOW;
		else return ChatColor.WHITE;
	}

}
