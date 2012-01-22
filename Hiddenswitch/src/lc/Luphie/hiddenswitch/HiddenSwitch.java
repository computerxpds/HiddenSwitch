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
import lc.Luphie.hiddenswitch.activity.OhTheCommandity;
import lc.Luphie.hiddenswitch.activity.playerListener;
import lc.Luphie.hiddenswitch.conf.HSConfig;
import lc.Luphie.hiddenswitch.conf.DatabaseHandler;
import lc.Luphie.hiddenswitch.conf.Lang;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Primary Plugin Class
 */
public class HiddenSwitch extends JavaPlugin {

	public Lang lang;
	public Logger log = Logger.getLogger("Minecraft");
	public static DatabaseHandler DBH;
	public static String logName;
	public playerListener blLs;
	public BrockListener brLs;
	public HSConfig confV;
	public static PluginManager pm;
	public static HiddenSwitch instance;

	public HiddenSwitch() {
		instance = this;
	}

	/**
	 * @see org.bukkit.plugin.Plugin#onDisable()
	 */
	public void onDisable() {

		confV.saveToFile(getConfig());
		log.info(logName + " is offline.");
		
	}

	/**
	 * @see org.bukkit.plugin.Plugin#onEnable()
	 */
	public void onEnable() {
		
		// Verify the data folder and create it if needed
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		
		logName = "[" + getDescription().getName() + "] ";

		blLs = new playerListener();
		brLs = new BrockListener();
		DBH = new DatabaseHandler();
		confV = new HSConfig();
		
		pm = getServer().getPluginManager();

		lang = new Lang();
		
		// Announce Ourselves
		log.info(logName + "v:" + getDescription().getVersion() + " " + lang.getLang().getString("language.messages.online"));

		// Set Language
	}

	/**
	 * @see
	 * org.bukkit.plugin.java.JavaPlugin#onCommand(org.bukkit.command.CommandSender
	 * , org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] mods) {

		if (cmd.getName().toLowerCase().equals("lchsreload")) {
			OhTheCommandity.lchsreload(sender);
		}
		
		if(cmd.getName().toLowerCase().equals("lchs")) {
			OhTheCommandity.lchs(sender);
		}

		return true;

	}

}
