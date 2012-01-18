package me.eighth.suitcase.event;

import java.util.ArrayList;

import me.eighth.suitcase.Suitcase;

public class SuitcasePlayer {
	
	private Suitcase plugin;
	private ArrayList<String> players = new ArrayList<String>();
	
	public SuitcasePlayer(Suitcase plugin) {
		this.plugin = plugin;
	}
}
