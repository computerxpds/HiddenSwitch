package lc.Luphie.hiddenswitch.utilities;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class BlockLocker {
	public void lockBlock(Block block) {
		// Check to see if block locking is allowed
		// Check to see if the user has permissions
		// Check to see if the block lockable
		// Check to see if the block is locked
		// Lock the block
			// Enter lock into hashmap
			// save database
	}
	public static Block getBlock(Player player) {
		Block block = player.getTargetBlock(null,32);
		return block;
	}
}
