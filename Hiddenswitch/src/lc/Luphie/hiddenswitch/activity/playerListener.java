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
import lc.Luphie.hiddenswitch.utilities.KeyBlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.Listener;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class playerListener extends PlayerListener {

	public static HiddenSwitch me;

	public playerListener() {

		me = HiddenSwitch.instance;
		//Bukkit.getServer().getPluginManager().registerEvents(this, me);
		Bukkit.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT, this, Event.Priority.Highest, me);

	}

	//@EventHandler(priority = EventPriority.HIGHEST)
	@Override
	public void onPlayerInteract(PlayerInteractEvent ev) {

		/*
		 *  If event was cancelled, then nothing to do here
		 */
		if (ev.isCancelled()) {
			
			return;
			
		}

		/*
		 *  Make sure the interaction was a click
		 */
		if (!ev.getAction().equals(Action.LEFT_CLICK_BLOCK) && !ev.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			
			return;

		}
		
		/*
		 * Shortcut the config values
		 */
		boolean hsb = HiddenSwitch.instance.getConfig().getBoolean("lchs.dbcontrol.allow-db");
		boolean hss = HiddenSwitch.instance.getConfig().getBoolean("lchs.signcontrol.allow-signs");
		
		/*
		 * If these are both false then there is no need for the plugin to be on at all...
		 */
		if(!hsb && !hss) {
			
			return;
			
		}

		/*
		 *  Check the player's permissions to make sure they can activate switches
		 */
		if (!ev.getPlayer().hasPermission("hiddenswitch.user.use")) {
			
			return;
			
		}

		/*
		 * See if the block is in the list of usable blocks.
		 */
		if (!me.confV.usableBlocks.contains(ev.getClickedBlock().getTypeId())) {
			
			return;

		}

		Block blk = ev.getClickedBlock();
		Player plr = ev.getPlayer();
		Location loc;
		Boolean brb = false;

		// Faces to check
		BlockFace[] faces = {
			BlockFace.UP,
			BlockFace.DOWN,
			BlockFace.NORTH,
			BlockFace.SOUTH,
			BlockFace.EAST,
			BlockFace.WEST };

		// Are we checking as a hiddenswitch block?

		for (BlockFace holder : faces) {

			/*
			 * If signs are enabled check for signs.
			 */
			if(hss) {

				if (blk.getRelative(holder).getTypeId() == 63 || blk.getRelative(holder).getTypeId() == 68) {
	
					if(signSlapper(blk.getRelative(holder), plr)) {
	
						brb = true;

					}
	
				}

			}

			/*
			 * If KeyBlocks are enabled check for KeyBlocks
			 */
			if (hsb) {

				if (blk.getRelative(holder).getTypeId() == 69 || blk.getRelative(holder).getTypeId() == 77) {

					if (canActivateBlock(blk,plr)){
						// Look for levers
						
							if (blk.getRelative(holder).getTypeId() == 69) {

								loc = blk.getRelative(holder).getLocation();
								flipLever(loc, plr);
								brb = true;

							} else if (blk.getRelative(holder).getTypeId() == 77) {

								loc = blk.getRelative(holder).getLocation();
								pushButton(loc, plr);
								brb = true;

							}
						}
				} // END IF RELATIVE IS SWITCH
			}
			
			if(brb) {
				
				break;
				
			}

		} // END FOR
	}

	/**
	 * Check the sign to see if it is a HiddenSwitch Sign and try and use it if
	 * it is
	 * 
	 * @param blk
	 *            The sign found in onPlayerInteract as an instance of
	 *            {@link Block}
	 * @param plr
	 *            The player involved in the event
	 * @return true if successful, otherwise false
	 */
	private boolean signSlapper (Block blk, Player plr) {

		Sign sign = (Sign) blk.getState();
		String slappyFace = sign.getLine(0).toLowerCase();
		Boolean failed = false;

		/*
		 *  Check the first line of the sign to see if we need to run
		 */
		if (!slappyFace.equals(me.getConfig().getString("lchs.signcontrol.sign-text").toLowerCase())) {

			return false;

		}


		/*
		 *  Check to see if line 2 has text, if it does and the player
		 *  doesn't have the ignorekeys.user permission
		 *  then check their name against the sign.
		 */
		if (!sign.getLine(1).isEmpty() && !plr.hasPermission("hiddenswitch.admin.ignorekeys.user")) {

			// Fail if names ARE case sensitive and the name on the sign
			// DOES NOT match the player name
			if (me.getConfig().getBoolean("lchs.config.case-sensitive-names")
				&& !sign.getLine(1).equals(plr.getDisplayName())) {

				failed = true;

				// Fail if names ARE NOT case sensitive and the name on the
				// sign DOES NOT match the player name
			} else if (!me.getConfig().getBoolean(
				"lchs.config.case-sensitive-names")
				&& !sign.getLine(1).equalsIgnoreCase(plr.getDisplayName())) {

				failed = true;

			}
		}


		// if line 3 IS NOT blank and the user DOES NOT have the
		// ignorekeys.key permission
		if (!sign.getLine(2).isEmpty() && !plr.hasPermission("hiddenswitch.admin.ignorekeys.key")) {

			// Is the key item override set?
			if (me.getConfig().getInt("lchs.signcontrol.item-lock-override") != 0) {

				// If so then is the player holding the specified key item?
				if (plr.getItemInHand().getTypeId() != me.getConfig().getInt(
					"lchs.signcontrol.item-lock-override")) {

					failed = true;

				}

				// If it's player set items
			} else if (!sign.getLine(2).equalsIgnoreCase(plr.getItemInHand().getType().toString())) {

				failed = true;

			}
		}
		

		// Flip the switch
		if (failed == false) {

			// Is there a button or switch nearby?
			Location loc;

			// Faces to check
			BlockFace[] faces = {
				BlockFace.UP,
				BlockFace.DOWN,
				BlockFace.NORTH,
				BlockFace.SOUTH,
				BlockFace.EAST,
				BlockFace.WEST };

			// TODO Simplify this to a single statement
			for (BlockFace holder : faces) {

				// Look for levers
				if (blk.getRelative(holder).getTypeId() == 69) {

					loc = blk.getRelative(holder).getLocation();
					flipLever(loc, plr);
					return true;

				}

				// Look for buttons
				if (blk.getRelative(holder).getTypeId() == 77) {

					loc = blk.getRelative(holder).getLocation();
					pushButton(loc, plr);
					return true;

				}
			}
		}
		return false;
	}

	private boolean canActivateBlock (Block blk, Player plr) {
		
		String id = KeyBlock.makeIdString(blk.getWorld(), blk.getX(), blk.getY(), blk.getZ());
		KeyBlock keyblock = null;
		
		/*
		 * Is this a KeyBlock? 
		 */
		if (!me.blkCon.keyblocks.containsKey(id)) {
			
			return false;
			
		} else {
			
			keyblock = me.blkCon.keyblocks.get(id);
			
			if(keyblock == null) {
				
				return false;
				
			}
		
		}
		
		/*
		 * Does this KeyBlock have a user lock?
		 */
		if (!keyblock.users.isEmpty()) {
			
			/*
			 * Does the invoking player match the user lock?
			 */
			if (!keyblock.users.equals(plr.getName())) {
				
				return false;
				
			}
			
		}
		
		/*
		 * Does this KeyBlock have a key item lock?
		 */
		if (!keyblock.key.isEmpty()) {
			
			/*
			 * Does the invoking player's held item match the key item?
			 */
			if (!keyblock.key.equalsIgnoreCase(plr.getItemInHand().getType().toString())) {
				
				return false;
				
			}
			
		}
		
		return true;
		
	}
	/**
	 * Push button.
	 * 
	 * Imitate a player interaction with a button at the given location.
	 * 
	 * @param loc
	 *            The location of the button.
	 * @param player
	 *            The activating player.
	 */
	private void pushButton(Location loc, Player player) {

		net.minecraft.server.Block.STONE_BUTTON.interact(
			((CraftWorld) loc.getWorld()).getHandle(),
			loc.getBlockX(),
			loc.getBlockY(),
			loc.getBlockZ(),
			((CraftPlayer) player.getPlayer()).getHandle());

	}

	/**
	 * Flip lever.
	 * 
	 * Imitate a player interaction with a lever at the given location
	 * 
	 * @param loc
	 *            The location of the button.
	 * @param player
	 *            The activating player.
	 */
	private void flipLever(Location loc, Player player) {

		net.minecraft.server.Block.LEVER.interact(
			((CraftWorld) loc.getWorld()).getHandle(),
			loc.getBlockX(),
			loc.getBlockY(),
			loc.getBlockZ(),
			((CraftPlayer) player.getPlayer()).getHandle());

	}
}