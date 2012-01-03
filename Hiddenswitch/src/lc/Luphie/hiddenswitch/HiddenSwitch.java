/* *

 * HiddenSwitch - Hidden switches and buttons for Bukkit 
 * Copyright (C) 2011  Luphie (devLuphie) luphie@lumpcraft.com

 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* */
package lc.Luphie.hiddenswitch;

import java.io.File;
import java.util.logging.Logger;

import lc.Luphie.hiddenswitch.conf.configManipulation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HiddenSwitch extends JavaPlugin {
	
	public static HiddenSwitch lcHS;
	public final Logger logger = Logger.getLogger("Minecraft");
	public final playerListener blLs = new playerListener(this);
	public final configManipulation confV = new configManipulation(this);
	protected static FileConfiguration conf;
	protected PluginManager pm;
	
	public String logName = "[HiddenSwitch]";
	
	
	public void onDisable() {

		
		this.logger.info(logName + " is offline.");
		
	}

	public void onEnable() {

		pm = getServer().getPluginManager();
		
		// Announce Ourselves
		this.logger.info(logName + " v:" + getDescription().getVersion() + " is online.");
		
		// Try and find the config.yml
		if(confV.createConfigFile()) {

			FileConfiguration conf = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
			
			//TODO config: Verify all settings and fill in the blanks
			if(!conf.contains("lchs.config")) {

				logger.info(logName+" Config invalid Attempting to recreate");

				if(!confV.recreateConfigFile()) {
					pm.disablePlugin(this);
				}
			}
			
			getConfig().setDefaults(conf);
		} else {
			pm.disablePlugin(this);
		}

		// Load Allowed Blocks to confV
		confV.setBlockList(getConfig().getString("lchs.config.usable-blocks"));
		pm.registerEvent(Event.Type.PLAYER_INTERACT, this.blLs, Event.Priority.Normal, this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] mods) {

		if(cmd.getName().toLowerCase().equals("lchsreload")) {
			
			switch(confV.reloadConfig(sender)) {
			// Worked Fine
			case 0:
				break;
			// Exception thrown while trying to reload
			case 1:
				pm.disablePlugin(this);
				break;
			// Does not have permission
			case 2:
				break;
			}
		}
		
		return true;
		
	}

}
