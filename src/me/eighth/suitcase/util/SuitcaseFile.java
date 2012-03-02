package me.eighth.suitcase.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.util.SuitcaseConsole.Action;

public class SuitcaseFile {
	
	/** Suitcase instance */
	private Suitcase plugin;
	
	/**
	 * Provides an interface for YAML configuration files
	 * @param plugin Instance of Suitcase
	 */
	public SuitcaseFile(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	/** Loads a file and merges its value with its defaults */
	public boolean load(String filename, Map<String, Object> defaults, boolean optional) {
		
		/** Gets the old file */
		File oldFile = getFile(filename, optional);
		
		/** Gets the configuration of the old file */
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
				// save and use verified keys
				try {
					saveFile(oldFile, oldConfig);
					return true;
				} catch (IOException e) {
					plugin.log(Action.FILE_SAVE_ERROR, new ArrayList<String>(Arrays.asList(filename, e.toString())));
					return false;
				}
			}
			else {
				
				/** Gets the temporary file */
				File newFile = getFile(filename + "~", true);
				
				/** Gets an empty configuration */
				FileConfiguration newConfig;
				
				if (newFile != null) {
					
					newConfig = YamlConfiguration.loadConfiguration(newFile);
					
					// add missing properties
					for (String path : defaults.keySet()) {
						if (!oldConfig.contains(path) && !optional) {
							// set missing property and log to console if file wasn't empty
							plugin.log(Action.PROPERTY_MISSING, new ArrayList<String>(Arrays.asList(path, filename, defaults.get(path).toString())));
							newConfig.set(path, defaults.get(path));
						}
						else {
							// apply old property
							newConfig.set(path, oldConfig.get(path));
						}
					}
					if (!optional) {
						for (String path : Suitcase.removeStringsFromList(oldConfig.getKeys(false), defaults.keySet().toArray(new String[0]))) {
							// look for redundant properties and ensure they aren't sections
							if (!oldConfig.isConfigurationSection(path)) {
								plugin.log(Action.PROPERTY_REDUNDANT, new ArrayList<String>(Arrays.asList(path, filename, oldConfig.get(path).toString())));
							}
						}
					}
					
					try {
						// save newFile
						saveFile(newFile, newConfig);
						// delete oldFile and rename newFile
						oldFile.delete();
						newFile.renameTo(oldFile);
						return true;
					} catch (IOException e) {
						plugin.log(Action.FILE_SAVE_ERROR, new ArrayList<String>(Arrays.asList(filename + "~", e.toString())));
						return false;
					}
				}
				else {
					return false;
				}
			}
		}
		else {
			return false;
		}
	}
	
	/** Returns a File for a file name */
	private File getFile(String filename, boolean suppress) {
		File file = new File(filename);
		if (!file.exists()) {
			if (!suppress) {
				plugin.log(Action.FILE_NOT_FOUND, new ArrayList<String>(Arrays.asList(filename)));
			}
			try {
				// create parent directory
				new File(filename.replaceFirst("/[^/]*$", "")).mkdir();
				file.createNewFile();
			} catch (IOException e) {
				plugin.log(Action.FILE_SAVE_ERROR, new ArrayList<String>(Arrays.asList(filename, e.toString())));
				return null;
			}
		}
		return file;
	}
	
	/** Saves a FileConfiguration to a File */
	private void saveFile(File file, FileConfiguration fileConfig) throws IOException {
		fileConfig.save(file);
	}
	
}
