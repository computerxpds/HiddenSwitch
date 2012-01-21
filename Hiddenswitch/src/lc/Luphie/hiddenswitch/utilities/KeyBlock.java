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
	public String id;
	public String users;
	public String key;
	public int x;
	public int y;
	public int z;
	public String world;
	
	public KeyBlock(String id, String world, int x, int y, int z, String users, String key){
		this.id = id;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.users = users;
		this.key = key;
	}
	
	public static KeyBlock blockToKey(Block block) {
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		String world = block.getWorld().getName();
		String id = world + Integer.toString(x) + Integer.toString(y) + Integer.toString(z);
		KeyBlock key = new KeyBlock(id, world, x, y, z, "", "");
		return key;
	}
}