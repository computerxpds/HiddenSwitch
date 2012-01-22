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
package lc.Luphie.hiddenswitch.activity;

import lc.Luphie.hiddenswitch.HiddenSwitch;
import lc.Luphie.hiddenswitch.utilities.BlockContainer;
import lc.Luphie.hiddenswitch.utilities.KeyBlock;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


/**
 * A collection of command handlers.
 * 
 * @author Luphie
 */
public class OhTheCommandity {

	/**
	 * Create a new block based hidden switch.
	 * 
	 * @param sender
	 *            An instance of what used the command either console or player.
	 */
	public static void lchs(CommandSender sender) {

		HiddenSwitch me = HiddenSwitch.instance;
		
		// Did this come from a player?
		if(!(sender instanceof Player)) {
			me.log.info(HiddenSwitch.logName + "But why would the server need a hidden switch?");
			return;
		}
		
		Player player = (Player) sender;

		// Is this enabled?
		if(!me.getConfig().getBoolean("lchs.dbcontrol.allow-db")) {
			return;
		}
		
		// Does the user have permission for this command?
		if(!player.hasPermission("hiddenswitch.user.command")) {
			return;
		}
		
		// Get the block to check if it is even usable
		Block block = BlockContainer.getBlock(player);
		
		// Is the block usable?
		if(!me.confV.usableBlocks.contains(block.getTypeId())) {
			player.sendMessage(me.lang.getLang().getString("language.messages.cannotuseblock"));
			return;
		}
		
		// Is the block already locked?
		String searchID = block.getWorld().getName() + block.getX() + block.getY() + block.getZ();
		if(me.confV.keyblocks.containsKey(searchID)) {
			player.sendMessage(me.lang.getLang().getString("language.messages.cannotuseblock"));
			return;
		}
		
		KeyBlock key = KeyBlock.blockToKey(block);
		me.confV.keyblocks.put(key.id, key);
		HiddenSwitch.DBH.newRecord(key);
		
		player.sendMessage(me.lang.getLang().getString("language.messages.hiddenswitchset"));
		
	}

	/**
	 * Reloads the HiddenSwitch config file.
	 * 
	 * @param sender
	 *            An instance of what used the command, whether console or
	 *            player.
	 */
	public static void lchsreload(CommandSender sender) {
		
		HiddenSwitch me = HiddenSwitch.instance;

		if (sender instanceof Player) {

			Player player = (Player) sender;
			
			if (player.hasPermission("hiddenswitch.admin.reload")) {

				player.sendMessage(me.lang.getLang().getString("language.messages.reload-config"));
				me.log.info(HiddenSwitch.logName + me.lang.getLang().getString("language.messages.reload-config"));
			
			} else {
			
				return;
			
			}
		}
		
		HiddenSwitch.instance.confV.reloadConfig();

	}
	
	public static void lchsreloadlang(CommandSender sender) {
		
		HiddenSwitch me = HiddenSwitch.instance;

		if (sender instanceof Player) {

			Player player = (Player) sender;
			
			if (player.hasPermission("hiddenswitch.admin.reload")) {

				player.sendMessage(me.lang.getLang().getString("language.messages.lang"));
				me.log.info(HiddenSwitch.logName + me.lang.getLang().getString("language.messages.reload-lang"));
			
			} else {
			
				return;
			
			}
		}
		
		me.lang.reloadLang();
	}
	
	public static void lchsreloaddb(CommandSender sender) {
		
	}
}

