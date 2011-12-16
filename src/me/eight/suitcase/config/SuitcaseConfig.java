package me.eight.suitcase.config;

import org.bukkit.configuration.file.FileConfiguration;

public class SuitcaseConfig {
	
	// define variables
	protected FileConfiguration cfFile;
	
	// set up config
	public class config {
		
		public class mechanics {
			public boolean op_permissions = true;
			
			public class rating {
				public boolean enable = true;
				public boolean allow_revert = false;
				public boolean multiple_rating = false;
				public String interval = "1d";
				public int min = 0;
				public int max = 100;
				public int start = 0;
			}
			
			public class warning {
				public boolean enable = true;
				public int max = 3;
			}
		}
		
		public class log {	
			public boolean command = false; // enable logging of player commands
			public boolean rate = true; // show player ratings in log
			public boolean warn = true; // show admin warnings in log
			public boolean system = true; // log reload etc. as well
			
			public class database {
				public boolean enable = false;
				public String type = "MySQL";
				public String database_name = "minecraft";
				public String table = "suitcase";
				public String username = "root";
				public String password = "root";
			}
			
			public class text {
				public boolean enable = true;
				public String file_name = "log_{date}_{count}.txt"; // {date} = file creation date {count} = current index
				public int max_lines = 10000;
			}
		}
		
		public class appearance {
			public boolean full_help = false; // display commands without permission
			
			public class color {
				public String header = "dark_aqua"; // DaRK_aqUa works as well
				public String frame = "gray";
				public String text = "aqua";
				public String info = "light_green";
				public String command = "gold";
				public String error = "dark_red";
			}
		}
		
		public class stats {
			public boolean enable = false;
			// TODO: Getting an idea how to provide stats
		}
	}
	
	// define basic classes
	public boolean setConfig(FileConfiguration config) {
		cfFile = parseConfig(config);
		return true;
	}
	
	private FileConfiguration parseConfig(FileConfiguration config) {
		return config;
	}
}
