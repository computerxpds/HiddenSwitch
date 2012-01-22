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
import lc.Luphie.hiddenswitch.utilities.BlockContainer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Primary Plugin Class
 */
public class HiddenSwitch extends JavaPlugin {

	public Lang lang;
	public static DatabaseHandler DBH;
	public static String logName;
	public playerListener playList;
	public BrockListener brokList;
	public BlockContainer blkCon;
	public HSConfig confV;
	public static PluginManager pm;
	public static HiddenSwitch instance;

	public static boolean debug = false;

	public Logger log = Logger.getLogger("Minecraft");

	public HiddenSwitch() {
		instance = this;
	}

	/**
	 * @see org.bukkit.plugin.Plugin#onDisable()
	 */
	public void onDisable() {

		if(confV instanceof HSConfig) {

			confV.saveToFile();

		}

		if(lang instanceof Lang) {

			lang.saveToFile();
			log.info(logName + lang.getLang().getString("language.messages.offline"));
			
		} else {

			log.info(logName + "is offline.");

		}
		
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

		// Yay new objects to play with
		lang = new Lang();
		playList = new playerListener();
		brokList = new BrockListener();
		DBH = new DatabaseHandler();
		confV = new HSConfig();
		blkCon = new BlockContainer();
		
		debug = getConfig().getBoolean("lchs.config.debug");
		pm = getServer().getPluginManager();

		
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

			OhTheCommandity.lchs(sender, mods);

		}

		if(cmd.getName().toLowerCase().equals("lchsreloadlang")) {

			OhTheCommandity.lchsreloadlang(sender);

		}
		
		return true;

	}

}
