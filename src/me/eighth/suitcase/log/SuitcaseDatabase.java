package me.eighth.suitcase.log;

import me.eighth.suitcase.Suitcase;

public class SuitcaseDatabase {
	
	/** Suitcase instance */
	// private Suitcase plugin;
	
	/**
	 * Database interface for player data
	 * @param plugin Instance of Suitcase
	 */
	public SuitcaseDatabase(Suitcase plugin) {
		// this.plugin = plugin;
	}
	
	/** Initializes database connection */
	protected boolean init() {
		
		return true;
	}
	
	/** Closes database connection and disposes cache */
	protected boolean free() {
		
		return true;
	}
	
	// TODO: Hibernate database interface for MySQL, h2 and some others
}
