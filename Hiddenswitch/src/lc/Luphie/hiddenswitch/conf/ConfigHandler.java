/* *

 * HiddenSwitch - Hidden switches and buttons for Bukkit.
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
import org.bukkit.util.config.ConfigurationException;


/**
 * configManipulation
 * 
 * A set of tools for working with the config.
 * 
 */
public class ConfigHandler {

	/** The main class. */
	private static HiddenSwitch me;

	/** A list of allowed hiddenswitch blocks. */
	public List<Integer> usableBlocks = new ArrayList<Integer>();

	/**
	 * Instantiates a new config manipulation.
	 *
	 * @param instance The calling instance of HiddenSwitch
	 */
	public ConfigHandler(HiddenSwitch instance) {

		me = instance;

	}

	/**
	 * Load config.
	 * 
	 * Attempts to load the configuration from file.
	 *
	 * @return true if successful, otherwise false
	 */
	public boolean loadConfig() {

		// Check to see if the data folder exists
		if (!me.getDataFolder().exists()) {

			// Then try and create it
			return createConfigFile(true);
		}

		// Check to see if the config file exists
		if (!(new File(me.getDataFolder(), "config.yml")).exists()) {

			// If not then try and create it
			return createConfigFile(false);
		}

		// Set the defaults 
		me.reloadConfig();

		if(!checkConfig(me.getConfig())) {
			me.log.warning(HiddenSwitch.logName+"Config may be corrupted.");
		}
		
		// Update the allowed blocks
		setBlockList(me.getConfig().getString("lchs.config.usable-blocks"));
		
		return true;
	}
	
	/**
	 * setBlockList()
	 * 
	 * Sets configManipulation.usableBlocks to a list of integers retrieved from
	 * the config field lchs.config.usable-blocks.
	 *
	 * @param string the new block list
	 */
	public void setBlockList(String string) {

		// Empty the list (in case of reloads)
		usableBlocks.clear();

		String[] strings = string.split(",");
		for (String toInt : strings) {
			usableBlocks.add(Integer.parseInt(toInt));
		}
	}

	/**
	 * reloadConfig
	 * 
	 * Reloads the configuration from file.
	 * 
	 * TODO:
	 *		Separate this into a command handler and the config reloader.
	 * 
	 * @param sender
	 *            Source of the command
	 * @return Integer
	 */
	public boolean reloadConfig(CommandSender sender) {

		// If the command was issued by a player, check to make sure they have
		// the permission
		if ((sender instanceof Player)) {
			Player player = (Player) sender;
			if (player.hasPermission("hiddenswitch.admin.reload")) {
				player.sendMessage("Reloading HiddenSwitch Config...");
			} else {
				return false;
			}
		}

		me.log.info(HiddenSwitch.logName+"Attempting to reload configuration.");

		if(!checkConfig(YamlConfiguration.loadConfiguration(new File(me.getDataFolder(), "config.yml")))) {
			me.log.warning(HiddenSwitch.logName+"Error while attempting to reload config.");
			return false;
		}
		
		me.reloadConfig();

		// Update the allowed blocks
		setBlockList(me.getConfig().getString("lchs.config.usable-blocks"));

		return true;
	}

	/**
	 * Creates the config file.
	 *
	 * @param makeDir Whether or not a default directory needs to be made
	 * @return true if successful
	 */
	public boolean createConfigFile(boolean makeDir) {

		if (makeDir) {

			// Directory not found, say some words
			me.log.info(HiddenSwitch.logName+"Couldn't find config directory; creating a new one.");
			try {

				me.getDataFolder().mkdir();

			} catch (Exception e) {

				me.log.warning(HiddenSwitch.logName+"Could not create data directory \""
					+ me.getDataFolder().toString() + "\"");
				return false;

			}
		}

		// Time to make the config, say words to the console
		me.log.info(HiddenSwitch.logName+"Data directory created, attempting to write config.yml");
		if (recreateConfigFile()) {
			me.log.info(HiddenSwitch.logName+"Config file and directory created.");
			return true;
		}

		return false;

	}

	/**
	 * Recreate config file.
	 *
	 * @return true if successful, otherwise false
	 */
	public boolean recreateConfigFile() {

		FileConfiguration defConf;
		
		try {
			defConf = loadDefaults();
		} catch (ConfigurationException e1) {
			me.log.severe(HiddenSwitch.logName+e1.getMessage());
			HiddenSwitch.pm.disablePlugin(me);
			return false;
		}
		
		return saveConfigToFile(defConf);
		
	}

	/**
	 * Load defaults
	 * 
	 * Load the default configuration settings from the included config.yml
	 * file.
	 *
	 * @return FileConfiguration the default config.yml loaded into a FileConfiguration
	 * @throws ConfigurationException with message
	 */
	public YamlConfiguration loadDefaults() throws ConfigurationException {

		// Read the included copy of config.yml
		InputStream configEmb = me.getResource("config.yml");

		// If it exists then return it
		if (configEmb != null) {

			return YamlConfiguration.loadConfiguration(configEmb);

		}

		throw new ConfigurationException(
			"Could not find a required resource, please redownload the newest version or report this error.");
	}

	/**
	 * Check config.
	 * 
	 * Checks the configuration to make sure everything is set, and fills in the
	 * blanks where needed.
	 *
	 * @param config FileConfiguration to check
	 * @return true if successful, otherwise false
	 */
	public boolean checkConfig(FileConfiguration config) {

		FileConfiguration defConf;

		try {
			defConf = loadDefaults();
		} catch (ConfigurationException e1) {
			me.log.severe(HiddenSwitch.logName+e1.getMessage());
			HiddenSwitch.pm.disablePlugin(me);
			return false;
		}

		boolean saveConf = false;

		/*
		 * Config
		 */

		// Check Usable Blocks
		if (!config.isSet("lchs.config.usable-blocks")) {

			config.set(
				"lchs.config.usable-blocks",
				defConf.getString("lchs.config.usable-blocks", "1,2,3,4,5,7,17,22,24,35,41,42,43,45,47,48,49,53,57,67,80,82,87,88,98,108,109,110,114,121"));
			saveConf = true;
		}

		// Allow Left Clicks
		if (!config.isSet("lchs.config.left-clicks")) {

			config.set(
				"lchs.config.left-clicks",
				defConf.getBoolean("lchs.config.left-clicks", true));
			saveConf = true;
		}

		// Case Sensitive name checking
		if (!config.isSet("lchs.config.case-sensitive-names")) {

			config.set(
				"lchs.config.case-sensitive-names",
				defConf.getBoolean("lchs.config.case-sensitive-names", false));
			saveConf = true;
		}

		/*
		 * SignControl
		 */

		// Allow Signs
		if (!config.isSet("lchs.signcontrol.allow-signs")) {

			config.set(
				"lchs.signcontrol.allow-signs",
				defConf.getBoolean("lchs.signcontrol.allow-signs", true));
			saveConf = true;
		}

		// Sign Header Text
		if (!config.isSet("lchs.signcontrol.sign-text")) {

			config.set(
				"lchs.signcontrol.sign-text",
				defConf.getString("lchs.signcontrol.sign-text", "[lchs]"));
			saveConf = true;
		}

		// Allow User Locks TODO: Phase Out
		if (!config.isSet("lchs.signcontrol.allow-user-lock")) {

			config.set(
				"lchs.signcontrol.allow-user-lock",
				defConf.getBoolean("lchs.signcontrol.allow-user-lock", true));
			saveConf = true;
		}

		// Allow Item Locks TODO: Phase Out
		if (!config.isSet("lchs.signcontrol.allow-item-lock")) {

			config.set(
				"lchs.signcontrol.allow-item-lock",
				defConf.getBoolean("lchs.signcontrol.allow-item-lock", true));
			saveConf = true;
		}

		// Item Lock Override Item TODO: Reconsider usefulness
		if (!config.isSet("lchs.signcontrol.item-lock-override")) {

			config.set(
				"lchs.signcontrol.item-lock-override",
				defConf.getInt("lchs.signcontrol.item-lock-override", 0));
			saveConf = true;
		}

		// Allow Username Shortcuts
		if (!config.isSet("lchs.signcontrol.allow-username-shortcut")) {

			config.set(
				"lchs.signcontrol.allow-username-shortcut",
				defConf.getBoolean("lchs.signcontrol.allow-username-shortcut", false));
			saveConf = true;
		}

		// Username Shortcut text
		if (!config.isSet("lchs.signcontrol.username-shortcut")) {

			config.set(
				"lchs.signcontrol.username-shortcut",
				defConf.getString("lchs.signcontrol.username-shortcut", "me"));
			saveConf = true;
		}

		/*
		 * Save Config
		 * 
		 * If anything was updated save the config to file.
		 */
		if (saveConf) {

			me.log.info(HiddenSwitch.logName+"Missing conf entries found, filling in the blanks...");
			if(!saveConfigToFile(config)) {
				me.log.warning(HiddenSwitch.logName+"Config could not be written!");
				return false;
			}
			me.log.info(HiddenSwitch.logName+"Config successfully written");
		}
		
		return true;
	}

	/**
	 * Save config.
	 * 
	 * Saves the configuration to file.
	 *
	 * @param conf FileConfiguration to save
	 * @return true if successful, otherwise false
	 */
	public boolean saveConfigToFile(FileConfiguration conf) {

		try {
			conf.save(new File(me.getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
