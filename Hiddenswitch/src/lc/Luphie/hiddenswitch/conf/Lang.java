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
	}
}
