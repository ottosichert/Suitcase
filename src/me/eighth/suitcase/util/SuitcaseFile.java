package me.eighth.suitcase.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.eighth.suitcase.Suitcase;
import me.eighth.suitcase.log.SuitcaseConsole.actionType;

public class SuitcaseFile {

	private Suitcase plugin;
	
	public SuitcaseFile(Suitcase plugin) {
		this.plugin = plugin;
	}
	
	public boolean load(String filename) {
		return load(filename, new HashMap<String, Object>());
	}
	
	public boolean load(String filename, Map<String, Object> defaults) {
		return load(filename, defaults, true);
	}
	
	public boolean load(String filename, Map<String, Object> defaults, boolean optional) {
		
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
				// save and use verified keys
				try {
					saveFile(oldFile, oldConfig);
					return true;
				} catch (IOException e) {
					plugin.con.log(actionType.FILE_SAVE_ERROR, new ArrayList<String>(Arrays.asList(filename, e.toString())));
					return false;
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
						if (!oldConfig.contains(path) && !optional) {
							// set missing property and log to console if file wasn't empty
							plugin.con.log(actionType.PROPERTY_MISSING, new ArrayList<String>(Arrays.asList(path, filename, defaults.get(path).toString())));
							newConfig.set(path, defaults.get(path));
						}
						else {
							// apply old property
							newConfig.set(path, oldConfig.get(path));
						}
					}
					
					for (String path : oldConfig.getKeys(true)) {
						// look for redundant properties and ensure they aren't sections
						if (!defaults.containsKey(path) && !oldConfig.isConfigurationSection(path) && !optional) {
							plugin.con.log(actionType.PROPERTY_REDUNDANT, new ArrayList<String>(Arrays.asList(path, filename, oldConfig.get(path).toString())));
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
						plugin.con.log(actionType.FILE_SAVE_ERROR, new ArrayList<String>(Arrays.asList(filename + "~", e.toString())));
						return false;
					}
					
				}
				else {
					plugin.con.log(actionType.FILE_SAVE_ERROR, new ArrayList<String>(Arrays.asList(filename, "newFileNullError")));
					return false;
				}
			}
		}
		else {
			plugin.con.log(actionType.FILE_SAVE_ERROR, new ArrayList<String>(Arrays.asList(filename, "oldFileNullError")));
			return false;
		}
	}

	private File getFile(String filename, boolean suppress) {
		File file = new File("plugins/Suitcase/" + filename);
		if (!file.exists()) {
			if (!suppress) {
				plugin.con.log(actionType.FILE_NOT_FOUND, new ArrayList<String>(Arrays.asList(filename)));
			}
			try {
				new File(("plugins/Suitcase/" + filename).replaceFirst("/[^/]*$", "")).mkdir();
				file.createNewFile();
			} catch (IOException e) {
				plugin.con.log(actionType.FILE_SAVE_ERROR, new ArrayList<String>(Arrays.asList(filename, e.toString())));
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
