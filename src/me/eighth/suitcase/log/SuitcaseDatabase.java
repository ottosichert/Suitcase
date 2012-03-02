package me.eighth.suitcase.log;

import java.util.ArrayList;

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

	/**
	 * Returns the rating of a player
	 * @param target Selected player
	 */
	public double getRating(String target) {
		return -1.0;
	}
	
	/**
	 * Returns an ArrayList of the top ten best rated players
	 */
	public ArrayList<String> getTopRatings() {
		return new ArrayList<String>();
	}
	
	/**
	 * Sets the rating of a player
	 * @param sender Rating player
	 * @param target Rated player
	 * @param rating Rating value
	 */
	public boolean setRating(String sender, String target, int rating) {
		return false;
	}
	
	/**
	 * Returns the amount of warnings of a player
	 * @param target Selected player
	 */
	public int getWarnings(String target) {
		return -1;
	}
	
	/**
	 * Increases the warning counter of a player by one or resets it
	 * @param target Warned player
	 * @param warning True if player is warned or false if player is forgiven
	 */
	public boolean setWarnings(String target, boolean warning) {
		return false;
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
