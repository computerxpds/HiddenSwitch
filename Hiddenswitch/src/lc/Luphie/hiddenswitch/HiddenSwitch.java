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

import java.util.logging.Logger;

import lc.Luphie.hiddenswitch.conf.ConfigHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Primary Plugin Class
 */
public class HiddenSwitch extends JavaPlugin {

	public static final Logger log = Logger.getLogger("Minecraft");
	public static String logName; 
	/** Player Listener */
	public final playerListener blLs = new playerListener(this);
	
	/** Block Listener */
	public final BrockListener brLs = new BrockListener(this);
	
	/** Config Handler */
	public final ConfigHandler confV = new ConfigHandler(this);
	
	/** PluginManager */
	public static PluginManager pm;

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.Plugin#onDisable()
	 */
	public void onDisable() {

		confV.saveConfigToFile(getConfig());
		log.info(logName+" is offline.");

	}

	public void onEnable() {

		pm = getServer().getPluginManager();
		logName = "["+getDescription().getName()+"]";

		// Announce Ourselves
		log.info(logName+"v:" + getDescription().getVersion() + " is online.");

		// Try and load the config file
		if (!confV.loadConfig()) {

			pm.disablePlugin(this);
			
		}

		// Load Allowed Blocks to confV
		confV.setBlockList(getConfig().getString("lchs.config.usable-blocks"));

		// Set on highest priority since we are not changing the event we just
		// need to know that it actually happened
		pm.registerEvent(
			Event.Type.PLAYER_INTERACT,
			this.blLs,
			Event.Priority.Highest,
			this);

		// Set on low priority, since we may cancel the event. (low is behind
		// protection plugins hopefully)
		pm.registerEvent(Event.Type.SIGN_CHANGE, this.brLs, Event.Priority.Normal, this);
	}

	/* (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	public boolean onCommand(
		CommandSender sender,
		Command cmd,
		String cmdLabel,
		String[] mods) {

		if (cmd.getName().toLowerCase().equals("lchsreload")) {

			if(!confV.reloadConfig(sender)) {
				log.warning(logName+"Could not reload config.");
			}
		}

		return true;

	}

}
