package me.eight.suitcase;

import java.util.logging.Logger;

import me.eight.suitcase.event.SuitcaseCommand;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Suitcase extends JavaPlugin {

	public static Suitcase plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	protected FileConfiguration config;
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info("[Suitcase] " + pdfFile.getName() + " disabled."); // Disabling plugin
	}
	
	@Override
	public void onEnable() {
		getCommand("suitcase").setExecutor(new SuitcaseCommand(this));
		config = getConfig();
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info("[Suitcase] " + pdfFile.getName() + " " + pdfFile.getVersion() + " enabled."); // Enabling plugin
	}
	
	public void reload() {
		PluginManager pm = getServer().getPluginManager();
		pm.disablePlugin(this);
		pm.enablePlugin(this);
		reloadConfig();
	}
	
	public void initConfig() {
		config.addDefault("vote.karma.max", "100");
		config.addDefault("vote.karma.min", "100");
		config.addDefault("vote.karma.d", "");
		config.addDefault("", "");
		config.addDefault("", "");
		config.addDefault("", "");
		config.addDefault("", "");
		config.addDefault("", "");
		config.addDefault("", "");
		config.addDefault("", "");
		config.addDefault("", "");
	}
}
