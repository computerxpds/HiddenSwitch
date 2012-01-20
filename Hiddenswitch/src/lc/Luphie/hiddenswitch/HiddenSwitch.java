/* *

 * HiddenSwitch - Hidden switches and buttons for Bukkit 
 * Copyright (C) 2011-2012  Luphie (devLuphie) luphie@lumpcraft.com

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

import lc.Luphie.hiddenswitch.activity.BrockListener;
import lc.Luphie.hiddenswitch.activity.PlayerListener;
import lc.Luphie.hiddenswitch.conf.ConfigHandler;
import lc.Luphie.hiddenswitch.conf.DatabaseHandler;
import lc.Luphie.hiddenswitch.conf.Lang;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Primary Plugin Class
 */
public class HiddenSwitch extends JavaPlugin {

	public Logger log = Logger.getLogger("Minecraft");
	public static String logName;
	/** Player Listener */
	public final PlayerListener blLs = new PlayerListener();

	/** Block Listener */
	public final BrockListener brLs = new BrockListener();

	/** Config Handler */
	public final ConfigHandler confV = new ConfigHandler(this);

	/** PluginManager */
	public static PluginManager pm;
	
	/** This Instance */
	public static HiddenSwitch instance;
	
	/** Database Handler */
	public DatabaseHandler DBH = new DatabaseHandler();
	
	/** Lang Class */
	public Lang lang;

	public HiddenSwitch() {
		instance = this;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.bukkit.plugin.Plugin#onDisable()
	 */
	public void onDisable() {

		confV.saveConfigToFile(getConfig());
		log.info(logName + " is offline.");

	}

	public void onEnable() {

		pm = getServer().getPluginManager();
		logName = "[" + getDescription().getName() + "] ";

		// Try and load the config file
		if (!confV.loadConfig()) {

			pm.disablePlugin(this);
			return;

		}

		lang = new Lang();
		
		// Announce Ourselves
		log.info(logName + "v:" + getDescription().getVersion() + " " + lang.getLang().getString("language.messages.online"));

		// Set Language
				
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.bukkit.plugin.java.JavaPlugin#onCommand(org.bukkit.command.CommandSender
	 * , org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] mods) {

		if (cmd.getName().toLowerCase().equals("lchsreload")) {

			if (!confV.reloadConfig(sender)) {
				log.warning(logName + "Could not reload config.");
			}
		}
		
		if(cmd.getName().toLowerCase().equals("lchs")) {
			if(sender instanceof Player) {
				
			} else {
				log.info(logName+"But the server is not allowed to lock blocks...");
			}
		}

		return true;

	}

}
