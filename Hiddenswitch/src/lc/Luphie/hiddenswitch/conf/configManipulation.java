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
package lc.Luphie.hiddenswitch.conf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lc.Luphie.hiddenswitch.HiddenSwitch;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class configManipulation {
	
	private static HiddenSwitch me;

	public List<Integer> useableBlocks = new ArrayList<Integer>();
	public boolean confLeftClicks;
	public boolean signAllowSigns;
	public String signSignText;
	public boolean signAllowULock;
	public boolean signAllowILock;
	public int signILockOverride;
	public boolean blockAllowBlocks;

	public configManipulation(HiddenSwitch instance) {
		me = instance;
	}
	
	/**
	 * setBlockList()
	 * 
	 * 	Sets configManipulation.useableBlocks to a list of integers retrieved
	 * from the config field lchs.config.useable-blocks.
	 * 
	 * @param string
	 */
	public void setBlockList(String string) {
		
		// Empty the list (in case of reloads)
		useableBlocks.clear();
		
		String[] strings = string.split(",");
		for(String toInt : strings) {
			useableBlocks.add(Integer.parseInt(toInt));
		}
	}
	
	/**
	 * reloadConfig
	 * 
	 * Reloads the configuration from file.
	 * 
	 * @param sender Source of the command
	 * @return Integer
	 */
	public int reloadConfig(CommandSender sender) {
		
	
		// If the command was issued by a player, check to make sure they have the permission
		if((sender instanceof Player)) {
			Player player = (Player) sender;
			if(player.hasPermission("hiddenswitch.admin.reload")) {
				player.sendMessage("Reloading HiddenSwitch Config...");
			} else {
				return 2;
			}
		}

		me.logger.info(me.logName+" Attempting to reload configuration.");

		me.reloadConfig();
		
		// Update the allowed blocks
		setBlockList(me.getConfig().getString("lchs.config.useable-blocks"));
		
		
		return 0;
	}
	
	public boolean createConfigFile() {

		// Check to see if there is a data folder
		if(!me.getDataFolder().exists()) {
			
			// If not, inform the console and try and make one
			me.logger.info(me.logName+" Couldn't find config directory; creating a new one.");
			try {

				me.getDataFolder().mkdir();
				
			} catch(Exception e) {
				
				me.logger.info(me.logName+"[ERROR] Could not create data directory \""+me.getDataFolder().toString()+"\"");
				return false;
				
			}
			
			// If there was no directory then there is no config file so create that too
			me.logger.info(me.logName+" Data directory created, attempting to write config.yml");
			if(recreateConfigFile()) {
				me.logger.info(me.logName+" Config file and directory created.");
				return true;
			}
			
			return false;
			
		} // end 'no directory'

		// Verify the existence of the config file.
		if(!(new File(me.getDataFolder(), "config.yml")).exists()) {

			// If it doesn't exist try and create it
			me.logger.info(me.logName+" No config found, attempting to create");

			if(recreateConfigFile()) {
				return true;
			}
			
			return false;
			
		} // end 'no config file'
		
		return true;
	}
	
	public boolean recreateConfigFile() {

			// Read the included copy of config.yml
			InputStream configEmb = me.getResource("config.yml");

			// If it exsists then write it to the external file
			if(configEmb != null) {

				FileConfiguration conf = YamlConfiguration.loadConfiguration(configEmb);
				try {

					conf.save(new File(me.getDataFolder(), "config.yml"));
					
				} catch (IOException e1) {

					me.logger.info(me.logName+"[ERROR] No config found or created! Please redownload the latest version of HiddenSwitch.");
					return false;

				}

			} else {
				
				me.logger.info(me.logName +"[ERROR] No config found or created! Please redownload the latest version of HiddenSwitch.");
				return false;
				
			}
			
			return true;
	}

}

