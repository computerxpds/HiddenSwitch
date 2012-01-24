/*
 *
 * HiddenSwitch - Hidden switches and buttons for Bukkit
 * Copyright (C) 2011-2012  Luphie (devLuphie) luphie@lumpcraft.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */



package lc.Luphie.hiddenswitch.conf;

//~--- non-JDK imports --------------------------------------------------------

import lc.Luphie.hiddenswitch.HiddenSwitch;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/**
 * A set of tools to handle the HiddenSwitch Configuration.
 *
 * @author Luphie
 * @version 2.0.1
 */
public class HSConfig {
    private static HiddenSwitch me;
    public List<Integer>        usableBlocks = new ArrayList<Integer>();
    public File                 confFile     = new File(HiddenSwitch.instance.getDataFolder(), "config.yml");

    public HSConfig() {

        // Shortcut for the main plugin instance
        me = HiddenSwitch.instance;

        try {

            me.getConfig().loadFromString(loadDefaults().saveToString());

        } catch (InvalidConfigurationException e) {

            HiddenSwitch.log.severe(HiddenSwitch.logName + "Could not load default config!");
            HiddenSwitch.pm.disablePlugin(me);

        }

        if (!confFile.exists()) {

            HiddenSwitch.log.info(HiddenSwitch.logName + "No configuration file found, attempting to create..."
            );

            if (!saveToFile()) {

                HiddenSwitch.log.severe(HiddenSwitch.logName + "Could not save configuration file!");

            } else {

                HiddenSwitch.log.info(HiddenSwitch.logName +
                                               "Configuration file \"config.yml\" saved successfully."
                );

            }

        } else {

            reloadConfig();

        }

    }

    /**
     * Loads the default config.yml included in the HiddenSwitch jar file.
     *
     * @return YamlConfig - The default HiddenSwitch config.yml file.
     */
    public YamlConfiguration loadDefaults() {

        return YamlConfiguration.loadConfiguration(me.getResource("config.yml"));

    }

    /**
     * Reloads the HiddenSwitch configuration from the config.yml file on the
     * disc.
     */
    public void reloadConfig() {

        me.getConfig().setDefaults(loadDefaults());

        if (!confFile.exists()) {

            HiddenSwitch.log.warning(HiddenSwitch.logName + "Could not find configuration file, using defaults");

        } else {

            me.reloadConfig();
            me.getConfig().setDefaults(loadDefaults());
            me.getConfig().options().copyDefaults(true);
            HiddenSwitch.log.info(HiddenSwitch.logName + "Configuration loaded...");

        }

        // Save configuration to update any missing entries
        saveToFile();

        // Update the allowed blocks
        setBlockList(me.getConfig().getString("lchs.config.usable-blocks"));

    }

    /**
     * Saves the default configuration to a file on the disc.
     *
     * @return true if successful, otherwise false
     */
    public boolean saveToFile() {

        return saveToFile(me.getConfig());

    }

    /**
     * Saves the provided configuration to a file on the disc.
     *
     * @param conf
     *            FileConfiguration to save
     * @return true if successful, otherwise false
     */
    public boolean saveToFile(FileConfiguration conf) {

        try {

            conf.save(confFile);

        } catch (IOException e) {

            e.printStackTrace();

            return false;

        }

        return true;

    }

    /**
     * Breaks the comma separated list of usable block ids from the
     * configuration into a Java list of the individual usable blocks to check
     * against later with the listeners.
     *
     * @param string
     *            The comma seperated list of usable blocks as pulled from
     *            config.yml.
     */
    public void setBlockList(String string) {

        // Empty the list (in case of reloads)
        usableBlocks.clear();

        String[] strings = string.split(",");

        for (String toInt : strings) {

            usableBlocks.add(Integer.parseInt(toInt));

        }
    }
}