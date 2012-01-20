package lc.Luphie.hiddenswitch.activity;

import lc.Luphie.hiddenswitch.HiddenSwitch;
import lc.Luphie.hiddenswitch.utilities.BlockLocker;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class OhTheCommandity {

	public void lchs(Player player) {
		
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
			return;
		}
		
		// Is the block already locked?
		String searchID = block.getWorld().getName() + block.getX() + block.getY() + block.getZ();
		if(!HiddenSwitch.instance.confV.keyblocks.containsKey(searchID)) {
			player.sendMessage("Block cannot be used as a hidden switch.");
			return;
		}
	}
}
