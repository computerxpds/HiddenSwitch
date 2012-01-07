package lc.Luphie.hiddenswitch;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

public class BrockListener extends BlockListener {
    
    public static HiddenSwitch me;

    public BrockListener(HiddenSwitch instance) {

	me = instance;

    }
    
    public void onSignChange(SignChangeEvent ev) {
	
	// Make sure the event wasn't canceled before this
	if(ev.isCancelled()) {
	    return;
	}

	// Are sign based switches allowed?
	if(!me.getConfig().getBoolean("lchs.signcontrol.allow-signs", true)) {
	    return;
	}
	
	// Get the sign text
	String[] lns = ev.getLines();
	
	// Is this a HiddenSwitch sign?
	if(!lns[0].equals(me.getConfig().getString("lchs.signcontrol.sign-text", "[lchs]"))) {
	    return;
	}
	
	// Get the involved Player
	Player pl = ev.getPlayer();
	
	// Does this player have permission to create a sign?
	if(!pl.hasPermission("hiddenswitch.user.create")) {
	    signBreak(ev.getBlock(),pl);
	    return;
	}
	
	// Check if there is a user line set and if the player has that permission
	if(!lns[1].isEmpty() && !pl.hasPermission("hiddenswitch.user.lockuser")) {
	    signBreak(ev.getBlock(),pl);
	    return;
	}
	
	// Check if there is a key line set and if the player has that permission
	if(!lns[2].isEmpty() && !pl.hasPermission("hiddenswitch.user.lockitem")) {
	    signBreak(ev.getBlock(),pl);
	    return;
	}
	
    }
    
    public void signBreak(Block sign, Player pl) {
	sign.setTypeId(0);
	ItemStack signdrop = new ItemStack(323,1);
	pl.getWorld().dropItem(sign.getLocation(), signdrop);
    }

}
