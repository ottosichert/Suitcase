package me.eighth.suitcase.config;

import java.io.File;

import me.eighth.suitcase.Suitcase;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SuitcaseConfig {
	
	// define variables
	private FileConfiguration cfConfig;
	private File cfFile = new File(Suitcase.plugin.getDataFolder(), "config.yml");
	
	/*
	// set up config
	public static class config {
		
		public static class mechanics {
			public boolean op_permissions = true;
			
			public static class rating {
				public boolean enable = true;
				public boolean allow_revoke = false;
				public boolean multiple_rating = false;
				public String interval = "1d";
				public int min = 0;
				public int max = 100;
				public int start = 0;
			}
			
			public static class warning {
				public boolean enable = true;
				public int max = 3;
			}
		}
		
		public static class log {	
			public boolean command = true; // enable logging of player commands
			public boolean vote = true; // show player votes in log
			public boolean warn = true; // show admin warnings in log
			public boolean system = true; // log reload etc. as well
			
			public static class database { // TODO: Use Hibernate
				public boolean enable = false;
				public String type = "MySQL";
				public String database_name = "minecraft";
				public String table = "suitcase";
				public String username = "root";
				public String password = "root";
			}
			
			public static class text {
				public boolean enable = true;
				public String file_name = "log_{date}_{count}.txt"; // {date} = file creation date {count} = current index
				public int max_lines = 10000;
			}
		}
		
		public static class appearance {
			public boolean full_help = false; // display commands without permission
			
			public static class color {
				public String header = "dark_aqua"; // DaRK_aqUa works as well
				public String frame = "gray";
				public String text = "aqua";
				public String info = "light_green";
				public String command = "gold";
				public String error = "dark_red";
			}
		}
		
		public static class stats {
			public boolean enable = false;
			// TODO: Getting an idea how to provide stats
		}
	}
	*/
	
	// get config file
	public void initConfig() {
		// read file
		cfFile = new File(Suitcase.plugin.getDataFolder(), "config.yml");
		FileConfiguration conf = YamlConfiguration.loadConfiguration(cfFile);
		
		
		Suitcase.configKeys = conf.getValues(true);
	}
}
