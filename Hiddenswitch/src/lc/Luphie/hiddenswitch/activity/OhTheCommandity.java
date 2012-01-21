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
import lc.Luphie.hiddenswitch.utilities.BlockLocker;
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
	 * @param player
	 */
	public static void lchs(CommandSender sender) {

		// Did this come from a player?
		if(!(sender instanceof Player)) {
			HiddenSwitch.instance.log.info(HiddenSwitch.logName + "But why would the server need a hidden switch?");
			return;
		}
		
		Player player = (Player) sender;

		// Is this enabled?
		if(!HiddenSwitch.instance.getConfig().getBoolean("lchs.dbcontrol.allow-db")) {
			return;
		}
		
		// Does the user have permission for this command?
		if(!player.hasPermission("hiddenswitch.user.command")) {
			return;
		}
		
		// Get the block to check if it is even usable
		Block block = BlockLocker.getBlock(player);
		
		// Is the block usable?
		if(!HiddenSwitch.instance.confV.usableBlocks.contains(block.getTypeId())) {
			player.sendMessage("Block cannot be used as a hidden switch.");
			return;
		}
		
		// Is the block already locked?
		String searchID = block.getWorld().getName() + block.getX() + block.getY() + block.getZ();
		if(HiddenSwitch.instance.confV.keyblocks.containsKey(searchID)) {
			player.sendMessage("This block cannot be used as a hidden switch.");
			return;
		}
		
		KeyBlock key = KeyBlock.blockToKey(block);
		HiddenSwitch.instance.confV.keyblocks.put(key.id, key);
		HiddenSwitch.DBH.newRecord(key);
		
		player.sendMessage(HiddenSwitch.instance.lang.getLang().getString("language.messages.hiddenswitchset"));
		
	}

	public static void lchsreload(CommandSender sender) {

		if (sender instanceof Player) {

			Player player = (Player) sender;
			
			if (player.hasPermission("hiddenswitch.admin.reload")) {

				player.sendMessage("Reloading HiddenSwitch Config...");
			
			} else {
			
				return;
			
			}
		}
		
		HiddenSwitch.instance.confV.reloadConfig();

	}
}

