package lc.Luphie.hiddenswitch;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import lc.Luphie.hiddenswitch.conf.confValues;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HiddenSwitch extends JavaPlugin {
	
	public static HiddenSwitch lcHS;
	public final Logger logger = Logger.getLogger("Minecraft");
	public final playerListener blLs = new playerListener(this);
	public confValues confV = new confValues();
	protected static FileConfiguration conf;
	
	public String logName = "[HiddenSwitch]";
	
	
	public void onDisable() {

		
		this.logger.info(logName + " is offline.");
		
	}

	public void onEnable() {

		// Get Plugin description
		PluginDescriptionFile pdfFile = this.getDescription();
		
		// Try and find the config.yml
		if(!getDataFolder().exists()) {
			this.logger.info(logName+" Couldn't find config directory; creating a new one.");
			getDataFolder().mkdir();
		}

		// Announce Ourselves
		this.logger.info(logName + " v:" + pdfFile.getVersion() + " is online.");
		
		// Shortcut the PluginManager
		PluginManager pm = getServer().getPluginManager();
		
		// Try and load the config
		FileConfiguration conf = getConfig();
		
		// If the config is empty try and create a new one
		if(!conf.contains("lchs.config")) {

			this.logger.info(logName+" Attempting to create config.yml.");
			
			// Read the included copy of config.yml
			InputStream configEmb = this.getResource("config.yml");

			if(configEmb != null) {

				conf = YamlConfiguration.loadConfiguration(configEmb);
							
			} else {
				
				this.logger.info(logName +"[ERROR] No config found or created! Please redownload the latest version of HiddenSwitch.");
				pm.disablePlugin(this);
				return;
				
			}
			try {
				conf.save(new File(getDataFolder(), "config.yml"));
				
			} catch (IOException e1) {
				this.logger.info(logName+"[ERROR] No config found or created! Please redownload the latest version of HiddenSwitch.");
				e1.printStackTrace();
			}
		}
		
		// Load Allowed Blocks to confV
		confV.blockString(conf.getString("lchs.config.useable-blocks"));
		pm.registerEvent(Event.Type.PLAYER_INTERACT, this.blLs, Event.Priority.Normal, this);
	}

}
