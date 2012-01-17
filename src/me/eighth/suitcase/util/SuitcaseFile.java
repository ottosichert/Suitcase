package me.eighth.suitcase.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseConsole.actionType;

public class SuitcaseFile {

	private Suitcase plugin;
	
	public SuitcaseFile(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	public boolean load(String filename, Map<String, Object> defaults) {
		
		// get given File and FileConfiguration
		File oldFile = getFile(filename, false);
		FileConfiguration oldConfig;
		
		if (oldFile != null) {
			
			// load FileConfigurations
			oldConfig = YamlConfiguration.loadConfiguration(oldFile);
			
			// check if file is empty
			if (oldConfig.getKeys(true).size() == 0) {
				for (String path : defaults.keySet()) {
					// apply defaults
					oldConfig.set(path, defaults.get(path));
				}
			}
			else {
				
				// load new file and FileConfiguration
				File newFile = getFile(filename + "~", true);
				FileConfiguration newConfig;
				if (newFile != null) {
					
					newConfig = YamlConfiguration.loadConfiguration(newFile);
					
					// add missing properties
					for (String path : defaults.keySet()) {
						if (!oldConfig.contains(path)) {
							// set missing property and log to console if file wasn't empty
							newConfig.set(path, defaults.get(path));
							plugin.console.sendAction(actionType.PROPERTY_MISSING, new ArrayList<String>(Arrays.asList(path, filename, defaults.get(path).toString())));
						}
						else {
							// apply old property
							newConfig.set(path, oldConfig.get(path));
						}
					}
					
					// save newFile
					try {
						saveFile(newFile, newConfig);
					} catch (IOException e) {
						plugin.console.sendAction(actionType.FILE_SAVE_ERROR, new ArrayList<String>(Arrays.asList(filename + "~", e.toString())));
						return false;
					}
					
					// delete oldFile and load new one
					oldFile.delete();
					oldFile = getFile(filename, true);
					oldConfig = YamlConfiguration.loadConfiguration(oldFile);
					
					for (String path : newConfig.getKeys(true)) {
						// remove redundant property and ensure it isn't a section
						if (defaults.get(path) != null) {
							oldConfig.set(path, newConfig.get(path));
						}
						else if (!newConfig.isConfigurationSection(path)) {
							plugin.console.sendAction(actionType.PROPERTY_REDUNDANT, new ArrayList<String>(Arrays.asList(path, filename, newConfig.get(path).toString())));
						}
					}
					
					// don't forget to remove the temporary file afterwards
					newFile.delete();
				}
				else {
					plugin.console.sendAction(actionType.FILE_SAVE_ERROR, new ArrayList<String>(Arrays.asList(filename, "newFileNullError")));
					return false;
				}
			}
			
			// save and use verified keys
			try {
				saveFile(oldFile, oldConfig);
				return true;
			} catch (IOException e) {
				plugin.console.sendAction(actionType.FILE_SAVE_ERROR, new ArrayList<String>(Arrays.asList(filename, e.toString())));
				return false;
			}
		}
		else {
			plugin.console.sendAction(actionType.FILE_SAVE_ERROR, new ArrayList<String>(Arrays.asList(filename, "oldFileNullError")));
			return false;
		}
	}

	private File getFile(String filename, boolean suppress) {
		File file = new File("plugins/Suitcase/" + filename);
		if (!file.exists()) {
			try {
				if (!suppress) {
					plugin.console.sendAction(actionType.FILE_NOT_FOUND, new ArrayList<String>(Arrays.asList(filename)));
				}
				new File("plugins/Suitcase").mkdir();
				file.createNewFile();
			} catch (IOException e) {
				plugin.console.sendAction(actionType.FILE_SAVE_ERROR, new ArrayList<String>(Arrays.asList(filename, e.toString())));
				plugin.disable();
				return null;
			}
		}
		return file;
	}
	
	private void saveFile(File file, FileConfiguration fileConfig) throws IOException {
		fileConfig.save(file);
	}
	
}
