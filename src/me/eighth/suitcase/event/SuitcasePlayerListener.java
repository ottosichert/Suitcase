package me.eighth.suitcase.event;

import java.util.ArrayList;

import me.eighth.suitcase.Suitcase;

public class SuitcasePlayerListener {
	
	private Suitcase plugin;
	private ArrayList<String> players = new ArrayList<String>();
	
	public SuitcasePlayerListener(Suitcase plugin) {
		this.plugin = plugin;
	}
}
