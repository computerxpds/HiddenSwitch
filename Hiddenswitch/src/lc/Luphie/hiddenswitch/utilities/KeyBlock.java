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

 * */package lc.Luphie.hiddenswitch.utilities;

import org.bukkit.block.Block;

public class KeyBlock {

	/**
	 * The string id of the block. The string id consists of the worldname
	 * followed by the x, y, and z coordinates in a single spaceless string ie
	 * "world1212553".
	 */
	public String id;

	/**
	 * Comma separated list of user(s) who are allowed to activate this KeyBlock
	 */
	public String users;

	/**
	 * Key item that must be held to activate this KeyBlock.
	 */
	public String key;
	
	/**
	 * The name of the player who created this KeyBlock
	 */
	public String owner;
	
	/**
	 * x coordinate of the KeyBlock
	 */
	public int x;
	
	/**
	 * y coordinate of the KeyBlock
	 */
	public int y;
	
	/**
	 * z coordinate of the KeyBlock
	 */
	public int z;
	
	/**
	 * World that contains the KeyBlock
	 */
	public String world;
	
	/**
	 * Whether or not this KeyBlock is save in the database, used for updates,
	 * saving, and reloading the database.
	 */
	public boolean isInDatabase;
	
	public KeyBlock(String id, String world, int x, int y, int z, String users, String key, String owner, boolean isInDB){

		this.id = id;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.users = users;
		this.key = key;
		this.owner = owner;
		this.isInDatabase = isInDB;

	}
	
	public static KeyBlock blockToKey(Block block) {

		int bx = block.getX();
		int by = block.getY();
		int bz = block.getZ();

		String bworld = block.getWorld().getName();
		String bid = bworld + Integer.toString(bx) + Integer.toString(by) + Integer.toString(bz);
		KeyBlock key = new KeyBlock(bid, bworld, bx, by, bz, "", "", "", false);

		return key;
	}
}