package me.eight.suitcase.config;

import org.bukkit.configuration.file.FileConfiguration;

public class SuitcaseConfig {
	
	// define variables
	protected FileConfiguration cfFile;
	protected mechanics cfMechanics;
	protected log cfLog;
	protected stats cfStats;
	protected color cfColor;
	
	// set up config
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
		public class file {
			public boolean enable = true;
			public String file_name = "log_$d_$n"; // $d = date  $n = file number
			public int max_lines = 10000;
		}
	}
	public class stats {
		public boolean enable = false;
		// TODO: Getting an idea how to provide stats
	}
	public class color {
		public String header = "dark_aqua"; // DaRK_aqUa works here too
		public String frame = "gray";
		public String text = "aqua";
		public String info = "light_green";
		public String command = "gold";
		public String error = "dark_red";
	}

	// define basic classes
	public boolean setConfig(FileConfiguration config) {
		cfFile = parseConfig(config);
		return true;
	}
	
	private FileConfiguration parseConfig(FileConfiguration config) {
		return config;
	}
	
	public boolean getBool() {
		return false;
	}
}
