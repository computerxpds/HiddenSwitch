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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerListener implements Listener {

	public static HiddenSwitch me;

	public PlayerListener() {

		me = HiddenSwitch.instance;
		Bukkit.getServer().getPluginManager().registerEvents(this, me);

	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerBlockSlap(PlayerInteractEvent event) {

		// If event was cancelled, then nothing to do here
		if (event.isCancelled()) {

			return;
		}

		// Are sign base hidden switches enabled?
		if (!me.getConfig().getBoolean("lchs.signcontrol.allow-signs")) {

			return;
		}

		Player playa = event.getPlayer();

		// v0.0.6 PERMS check
		// Check for the players use sign permission
		if (playa.hasPermission("hiddenswitch.user.use") == false) {

			return;
		}

		Block iblock = event.getClickedBlock();

		// Are left clicks allowed?
		Boolean cclicks = me.getConfig().getBoolean("lchs.config.left-clicks");

		// Compare action to allowed clicks:
		if (cclicks) {

			if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			
				return;
			}
		} else {

			if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			
				return;
			}
		}

		// See if the block that was clicked is a usable block
		try {

			if (!me.confV.usableBlocks.contains(iblock.getTypeId())) {
			
				return;
			}
		} catch (NullPointerException e) {

			me.log.warning(HiddenSwitch.logName+"iblock NullPointerExeption");
			return;
		}

		Location loc;

		// Faces to check
		BlockFace[] faces = {
			BlockFace.UP,
			BlockFace.DOWN,
			BlockFace.NORTH,
			BlockFace.SOUTH,
			BlockFace.EAST,
			BlockFace.WEST };

		// Are we checking as a hiddenswitch block?
		boolean hsb = HiddenSwitch.instance.getConfig().getBoolean("lchs.dbcontrol.allow-db");

		for (BlockFace holder : faces) {

			// Try and find a sign post next to the clicked block
			if (iblock.getRelative(holder).getTypeId() == 63 || iblock.getRelative(holder).getTypeId() == 68) {

				if(signSlapper(iblock.getRelative(holder), playa)) {

					break;
				}

			}
			if (hsb) {

				if (iblock.getRelative(holder).getTypeId() == 69 || iblock.getRelative(holder).getTypeId() == 77) {

					String id = iblock.getWorld().getName() + Integer.toString(iblock.getX()) + Integer.toString(iblock.getY()) + Integer.toString(iblock.getZ());
					
					if(me.confV.keyblocks.containsKey(id)) {
					// Look for levers
					
						if (iblock.getRelative(holder).getTypeId() == 69) {
	
							loc = iblock.getRelative(holder).getLocation();
							flipLever(loc, playa);
							break;
	
						}
	
						// Look for buttons
						if (iblock.getRelative(holder).getTypeId() == 77) {
	
							loc = iblock.getRelative(holder).getLocation();
							pushButton(loc, playa);
							break;
	
						}
					}
				} // END IF RELATIVE IS SWITCH
			}

		} // END FOR
	}


	public boolean signSlapper(Block signToSlap, Player playa) {

		Sign hola = (Sign) signToSlap.getState();
		String slappyFace = hola.getLine(0).toLowerCase();
		Boolean failed = false;

		// Check the first line of the sign to see if we need to run
		if (!slappyFace.equals(me
			.getConfig()
			.getString("lchs.signcontrol.sign-text")
			.toLowerCase())) {

			return false;

		}

		// So it is, now do we allow per player restrictions?
		// PERMS Set this to a permission
		if (me.getConfig().getBoolean("lchs.signcontrol.allow-user-lock")) {

			// Check to see if line 2 has text, if it does and the player
			// doesn't have the ignorekeys.user permission
			// then check their name against the sign.
			if (!hola.getLine(1).isEmpty()
				&& !playa.hasPermission("hiddenswitch.admin.ignorekeys.user")) {

				// Fail if names ARE case sensitive and the name on the sign
				// DOES NOT match the player name
				if (me.getConfig().getBoolean("lchs.config.case-sensitive-names")
					&& !hola.getLine(1).equals(playa.getDisplayName())) {

					failed = true;

					// Fail if names ARE NOT case sensitive and the name on the
					// sign DOES NOT match the player name
				} else if (!me.getConfig().getBoolean(
					"lchs.config.case-sensitive-names")
					&& !hola.getLine(1).equalsIgnoreCase(playa.getDisplayName())) {

					failed = true;

				}
			}
		}

		// What about held item restrictions?
		// TODO: Set this to a permission
		// DEPRECIATED
		if (me.getConfig().getBoolean("lchs.signcontrol.allow-item-lock")) {


			// if line 3 IS NOT blank and the user DOES NOT have the
			// ignorekeys.key permission
			if (!hola.getLine(2).isEmpty()
				&& !playa.hasPermission("hiddenswitch.admin.ignorekeys.key")) {

				// Is the key item override set?
				if (me.getConfig().getInt("lchs.signcontrol.item-lock-override") != 0) {

					// If so then is the player holding the specified key item?
					if (playa.getItemInHand().getTypeId() != me.getConfig().getInt(
						"lchs.signcontrol.item-lock-override")) {

						failed = true;

					}

					// If it's player set items
				} else if (!hola.getLine(2).equalsIgnoreCase(playa
					.getItemInHand()
					.getType()
					.toString())) {

					failed = true;

				}
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
				if (signToSlap.getRelative(holder).getTypeId() == 69) {

					loc = signToSlap.getRelative(holder).getLocation();
					flipLever(loc, playa);
					return true;

				}

				// Look for buttons
				if (signToSlap.getRelative(holder).getTypeId() == 77) {

					loc = signToSlap.getRelative(holder).getLocation();
					pushButton(loc, playa);
					return true;

				}
			}
		}
		return false;
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