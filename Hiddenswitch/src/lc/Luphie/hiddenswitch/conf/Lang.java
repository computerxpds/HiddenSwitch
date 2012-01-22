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
package lc.Luphie.hiddenswitch.conf;

import java.io.File;
import java.io.IOException;

import lc.Luphie.hiddenswitch.HiddenSwitch;

import org.bukkit.configuration.file.YamlConfiguration;

public class Lang {

	private YamlConfiguration text = null;
	private File langFile = new File(HiddenSwitch.instance.getDataFolder(), "lang/eng.yml");
	
	public Lang() {
		
		text = loadDefaults();
		
		File file = new File(HiddenSwitch.instance.getDataFolder(), "lang/"+HiddenSwitch.instance.getConfig().getString("lchs.config.language-file"));

		if(!file.exists()){
			
			HiddenSwitch.instance.log.info(HiddenSwitch.logName + "No language file found, attempting to create...");
			
			if(!saveToFile()) {

				HiddenSwitch.instance.log.severe(HiddenSwitch.logName + "Could not save default language file!");
				
			} else {
				
				HiddenSwitch.instance.log.info(HiddenSwitch.logName + "Language file \"eng.yml\" saved successfully.");

			}
			
		} else {

			langFile = file;
			reloadLang();

		}
		
	}
	
	/**
	 * Return the loaded language configuration.
	 * 
	 * @return YamlConfiguration - returns the loaded language config.
	 */
	public YamlConfiguration getLang() {

		return text;

	}
	
	/**
	 * Attempts to load the language file from the disc, if not found it will
	 * load from the default eng.yml file included in the HiddenSwitch jar file.
	 */
	public void reloadLang() {

		text.setDefaults(loadDefaults());

		if(!langFile.exists()) {
		
			HiddenSwitch.instance.log.warning(HiddenSwitch.logName + "Could not find language file specified in config.yml defaulting to english.");
		
		} else {

			text = YamlConfiguration.loadConfiguration(langFile);
			
		}
		
		saveToFile();
		
	}
	
	
	/**
	 * Load the default English messages from the eng.yml file included in the
	 * HiddenSwitch jar file.
	 * 
	 * @return YamlConfiguration - the default language configuration.
	 */
	private YamlConfiguration loadDefaults() {

		return YamlConfiguration.loadConfiguration(HiddenSwitch.instance.getResource("eng.yml"));

	}
	
	/**
	 * Save the language configuration stored in the current instance to file.
	 * 
	 * @return boolean - Returns True if successful, otherwise false
	 */
	public boolean saveToFile() {
		
		return saveToFile(text);
		
	}
	
	/**
	 * Save the provided YamlConfiguration to file.
	 * 
	 * @param YamlConfiguration
	 *            The config to save.
	 * @return boolean - True if successful, otherwise false;
	 */
	public boolean saveToFile(YamlConfiguration conf) {
		
		try {
		
			conf.save(langFile);
		
		} catch (IOException e) {
		
			e.printStackTrace();
			return false;
		
		}
		
		return true;
		
		
	}

}
