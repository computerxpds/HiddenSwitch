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
	public static void lchs(CommandSender sender, String[] args) {

		HiddenSwitch me = HiddenSwitch.instance;
		
		/*
		 *  Did this come from a player?
		 */
		if(!(sender instanceof Player)) {
			me.log.info(HiddenSwitch.logName + "But why would the server need a hidden switch?");
			return;
		}
		
		Player player = (Player) sender;

		/*
		 *  Is this enabled?
		 */
		if(!me.getConfig().getBoolean("lchs.dbcontrol.allow-db")) {
			return;
		}
		
		/*
		 *  Does the user have permission for this command?
		 */
		if(!player.hasPermission("hiddenswitch.user.command")) {
			return;
		}
		
		/*
		 *  Get the block to check if it is even usable
		 */
		Block block = BlockContainer.getBlock(player);
		
		/*
		 *  Is the block usable?
		 */
		if(!me.confV.usableBlocks.contains(block.getTypeId())) {
			player.sendMessage(me.lang.getLang().getString("language.messages.cannotuseblock"));
			return;
		}
		
		/*
		 *  Is the block already locked?
		 */
		String searchID = block.getWorld().getName() + block.getX() + block.getY() + block.getZ();
		if(me.blkCon.keyblocks.containsKey(searchID)) {
			player.sendMessage(me.lang.getLang().getString("language.messages.cannotuseblock"));
			return;
		}
		
		KeyBlock key = KeyBlock.blockToKey(block);
		
		/*
		 * Set the keyBlock's owner
		 */
		key.owner = player.getName();

		/*
		 * Get any arguments
		 */
		for (String hold : args) {

			/*
			 * For now to separate the arguments I went with a key:value setup
			 * so here we split the arguments into 2 strings and hunt for the
			 * key in the first so we can make use of the second
			 */
			String[] strings = hold.split(":", 2);

			/*
			 * If the first part is user then safety check the second part to
			 * make sure it is safe to go into the database. (Minecraft.net says
			 * usernames can only be letters numbers and _ so anything other
			 * than that is disallowed.
			 */
			if (strings[0].equals("user")) {

				// Check username
				if(strings[1].matches("\\w{3,}")) {
					
					// If it is safe set it to a key
					key.users = strings[1];
					
				// If it is unsafe yell at the sender	
				} else {
					
					player.sendMessage(me.lang.getLang().getString("language.warnings.invalidusername"));
					
				}
			
			/*
			 * Because long commands are a pain to use there are 2 shortcuts.
			 * the key "me" sets the user to the command sender and if the key
			 * "key" is provided without a value then it is set to the currently
			 * held item.
			 */
			} else if (strings[0].equals("me")) {
			
				key.users = player.getName();
			
			// Now for the "key" key
			} else if (strings[0].equals("key")) {
			
				/*
				 * If there is no second part then set the key item to what the
				 * user is currently holding
				 */
				if (strings.length == 1) {
				
					key.key = player.getItemInHand().getType().toString();
				
				/*
				 * If there is a key item specified make sure it is database
				 * safe and if it is set it as the KeyBlock's key
				 */
				} else if(strings[1].matches("\\w{3,}")) {
				
					key.key = strings[1];
				
				// If it is not database safe then yell at the user
					//TODO support extra data
				} else {
					
					player.sendMessage(me.lang.getLang().getString("language.warnings.invalidkeyname"));

				}
			}
			
		}
		
		HiddenSwitch.DBH.newRecord(key);
		key.isInDatabase = true;
		key.owner = player.getName();
		me.blkCon.keyblocks.put(key.id, key);
		
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
		
		genAdminCom(sender, "hiddenswitch.admin.reload", "language.messages.reload-config");
		HiddenSwitch.instance.confV.reloadConfig();

	}
	
	/**
	 * Reload the language configuration file.
	 * 
	 * @param sender
	 *            An instance of what used the command, console or player.
	 */
	public static void lchsreloadlang(CommandSender sender) {
		
		genAdminCom(sender, "hiddenswitch.admin.reloadlang", "language.messages.lang");
		HiddenSwitch.instance.lang.reloadLang();

	}
	
	/**
	 * Reload the KeyBlock database
	 * 
	 * @param sender
	 *            The command sender
	 */
	public static void lchsreloaddb(CommandSender sender) {

		genAdminCom(sender, "hiddenswitch.admin.reloaddb", "language.messages.reload-db");
		HiddenSwitch.instance.blkCon.reloadKeyBlocks();
		
	}
	
	/**
	 * Save the 'floating' KeyBlocks to the database, uses the saveAll() method;
	 * effectively a manual skip over updates().
	 * 
	 * @param sender
	 *            the sender
	 */
	public static void lchsSaveDB(CommandSender sender) {

		genAdminCom(sender, "hiddenswitch.admin.save", "language.messages.save-db");
		HiddenSwitch.DBH.saveAll();

	}

	private static void genAdminCom(CommandSender sender, String permPath, String mesPath) {

		HiddenSwitch me = HiddenSwitch.instance;

		if (sender instanceof Player) {

			Player player = (Player) sender;
			
			if (player.hasPermission(permPath)) {

				player.sendMessage(me.lang.getLang().getString(mesPath));
			
			} else {
			
				return;
			
			}
		}
	
		me.log.info(HiddenSwitch.logName + me.lang.getLang().getString(mesPath));
	}
}

