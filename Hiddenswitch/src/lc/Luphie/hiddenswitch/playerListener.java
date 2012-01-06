/* *

 * HiddenSwitch - Hidden switches and buttons for Bukkit 
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
package lc.Luphie.hiddenswitch;


import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;


public class playerListener extends PlayerListener {

	public static HiddenSwitch plugin;
	
	public playerListener(HiddenSwitch instance) {

		plugin = instance;
					
	}
	
	public void onPlayerInteract (PlayerInteractEvent event) {
		
		//If event was cancelled, then nothing to do here
		if(event.isCancelled()) {
			return;
		}
		
		Player playa = event.getPlayer();

		// If the player doesn't have permissions to do this then nope out of it
		if(playa.hasPermission("hiddenswitch.use") == false) {
			return;
		}
		
		Block iblock = event.getClickedBlock();
		
		// Are left clicks allowed?
		Boolean cclicks = plugin.getConfig().getBoolean("lchs.config.left-clicks");

		// Compare action to allowed clicks:
		if(cclicks) {
			if(!event.getAction().equals(Action.LEFT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				return;
			}
		} else {
			if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				return;
			}
		}
		
	
		// See if the block that was clicked is a usable block
		try {
			if(!plugin.confV.usableBlocks.contains(iblock.getTypeId())) {
				return;
			}
		} catch(NullPointerException e) {
			plugin.logger.info(plugin.logName +" iblock NullPointerExeption");
			return;
		}

		// Faces to check
		BlockFace[] faces = {
			BlockFace.UP,
			BlockFace.DOWN,
			BlockFace.NORTH,
			BlockFace.SOUTH,
			BlockFace.EAST,
			BlockFace.WEST
		};
		
		for(BlockFace holder : faces) {

			// Are we looking for signs?
			if(plugin.getConfig().getBoolean("lchs.signcontrol.allow-signs")) {
				

				// Try and find a sign post next to the clicked block
				if (iblock.getRelative(holder).getTypeId() == 63 ||
					iblock.getRelative(holder).getTypeId() == 68 ){

					signSlapper(iblock.getRelative(holder),playa);
					
				}
			}
		} //END FOR
	}

	public void signSlapper(Block signToSlap, Player playa) {
		
		Sign hola = (Sign)signToSlap.getState();
		String slappyFace = hola.getLine(0).toLowerCase();
		Boolean failed = false;
		
		// Check the first line of the sign to see if we need to run
		if(!slappyFace.equals(plugin.getConfig().getString("lchs.signcontrol.sign-text").toLowerCase())) {
			
			return;
			
		}
			
		// So it is, now do we allow per player restrictions?
		// TODO: Set this to a permission
		if(plugin.getConfig().getBoolean("lchs.signcontrol.allow-user-lock")) {

			String slappyTorso = hola.getLine(1);
			
			// Check to see if line 2 has text, if it does and the player doesn't have the ignorekeys.user permission
			// then check their name against the sign.
			if(!slappyTorso.isEmpty() && !playa.hasPermission("hiddenswitch.admin.ignorekeys.user")) {
				
				// Fail if names ARE case sensitive and the name on the sign DOES NOT match the player name
				if(plugin.getConfig().getBoolean("lchs.config.case-sensitive-names") && !slappyTorso.equals(playa.getDisplayName())) {
				
					failed = true;
					
				// Fail if names ARE NOT case sensitive and the name on the sign DOES NOT match the player name
				} else if(!plugin.getConfig().getBoolean("lchs.config.case-sensitive-names") && !slappyTorso.equalsIgnoreCase(playa.getDisplayName())) {

					failed = true;
					
				}
			}
		}
		
		// What about held item restrictions?
		// TODO: Set this to a permission
		if(plugin.getConfig().getBoolean("lchs.signcontrol.allow-item-lock")) {
		
			String slappyLegs = hola.getLine(2).toUpperCase();
			
			// if line 3 IS NOT blank and the user DOES NOT have the ignorekeys.key permission
			if(!slappyLegs.isEmpty() && !playa.hasPermission("hiddenswitch.admin.ignorekeys.key")) {

				// Is the key item override set?
				if(plugin.getConfig().getInt("lchs.signcontrol.item-lock-override") != 0) {

					// If so then is the player holding the specified key item?
					if(playa.getItemInHand().getTypeId() != plugin.getConfig().getInt("lchs.signcontrol.item-lock-override")) {
					
						failed = true;
					
					}
				
				// If it's player set items
				} else if(!slappyLegs.equalsIgnoreCase(playa.getItemInHand().getType().toString())) {
				
					failed = true;
					
				}
			}
		}


		// Flip the switch
		if(failed == false) {

			// Is there a button or switch nearby?
			Block levers;
			
			// Faces to check
			BlockFace[] faces = {
				BlockFace.UP,
				BlockFace.DOWN,
				BlockFace.NORTH,
				BlockFace.SOUTH,
				BlockFace.EAST,
				BlockFace.WEST
			};
			
			// TODO Simplify this to a single statement
			for(BlockFace holder : faces) {
				
				// find all the levers next to the sign
				if (signToSlap.getRelative(holder).getTypeId() == 69) {
					
					levers = signToSlap.getRelative(holder);

					// Imitate a player interaction with the lever to properly get block state changes
					net.minecraft.server.Block.LEVER.interact(((CraftWorld)levers.getWorld()).getHandle(),levers.getX(), levers.getY(), levers.getZ(),((CraftPlayer)playa.getPlayer()).getHandle());
					
											
				}
				if (signToSlap.getRelative(holder).getTypeId() == 77) {
					
					levers = signToSlap.getRelative(holder);

					// Imitate a player interaction with the button to properly get block state changes
					net.minecraft.server.Block.STONE_BUTTON.interact(((CraftWorld)levers.getWorld()).getHandle(),levers.getX(), levers.getY(), levers.getZ(),((CraftPlayer)playa.getPlayer()).getHandle());


				}
			}
		}

	}
}
