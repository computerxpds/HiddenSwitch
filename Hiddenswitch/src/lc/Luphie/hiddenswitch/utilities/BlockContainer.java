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
package lc.Luphie.hiddenswitch.utilities;

import java.util.HashMap;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BlockContainer {

	/** Preloaded key blocks */
	public HashMap<String, KeyBlock> keyblocks = new HashMap<String, KeyBlock>();

	public static Block getBlock(Player player) {

		Block block = player.getTargetBlock(null,32);
		return block;

	}
}

