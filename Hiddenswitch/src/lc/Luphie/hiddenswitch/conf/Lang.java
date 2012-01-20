<<<<<<< HEAD
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

 * */package lc.Luphie.hiddenswitch.conf;

import java.io.File;
import java.io.IOException;

import lc.Luphie.hiddenswitch.HiddenSwitch;

import org.bukkit.configuration.file.YamlConfiguration;

public class Lang {
	private YamlConfiguration text = null;
	
	public Lang() {
		File file = new File(HiddenSwitch.instance.getDataFolder(), "lang/"+HiddenSwitch.instance.getConfig().getString("lchs.config.language-file"));
		if(!file.exists()){
			loadDefaults();
		}
	}
	// Based off the getConfig // reloadConfig methods in bukkit's JavaPlugin.java
	public YamlConfiguration getLang() {
		if(text == null) {
			reloadLang();
		}
		return text;
	}
	
	public void reloadLang() {
		String file = HiddenSwitch.instance.getConfig().getString("lchs.config.language-file");
		File langFile = new File(HiddenSwitch.instance.getDataFolder(), "lang/"+file);
		if(!langFile.exists()) {
			HiddenSwitch.instance.log.warning(HiddenSwitch.logName + "Could not find language file specified in config.yml defaulting to english.");
			loadDefaults();
		}
		text = YamlConfiguration.loadConfiguration(langFile);
	}
	public void loadDefaults() {

		File file = new File(HiddenSwitch.instance.getDataFolder(), "lang/eng.yml");
		
		if(!file.exists()){
			try {
				YamlConfiguration lang = YamlConfiguration.loadConfiguration(HiddenSwitch.instance.getResource("eng.yml"));
				lang.save(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
=======
package lc.Luphie.hiddenswitch.conf;

import java.io.File;
import java.io.IOException;

import lc.Luphie.hiddenswitch.HiddenSwitch;

import org.bukkit.configuration.file.YamlConfiguration;

public class Lang {
	private YamlConfiguration text = null;
	
	public Lang() {
		File file = new File(HiddenSwitch.instance.getDataFolder(), "lang/eng.yml");
		if(!file.exists()){
			try {
				YamlConfiguration lang = YamlConfiguration.loadConfiguration(HiddenSwitch.instance.getResource("eng.yml"));
				lang.save(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	// Based off the getConfig // reloadConfig methods in bukkit's JavaPlugin.java
	public YamlConfiguration getLang() {
		if(text == null) {
			reloadLang();
		}
		return text;
	}
	
	public void reloadLang() {
		String file = HiddenSwitch.instance.getConfig().getString("lchs.config.language-file");
		File langFile = new File(HiddenSwitch.instance.getDataFolder(), "lang/"+file);
		if(!langFile.exists()) {
			HiddenSwitch.instance.log.warning(HiddenSwitch.logName + "Could not find language file specified in config.yml defaulting to english.");
			langFile = new File(HiddenSwitch.instance.getDataFolder(), "lang/eng.yml");
		}
		text = YamlConfiguration.loadConfiguration(langFile);
>>>>>>> refs/remotes/origin/development
	}
}
