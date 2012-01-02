package lc.Luphie.hiddenswitch;


import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.material.Button;
import org.bukkit.material.Lever;


public class playerListener extends PlayerListener {

	public static HiddenSwitch plugin;
	
	public playerListener(HiddenSwitch instance) {

		plugin = instance;
					
	}
	
	public void onPlayerInteract (PlayerInteractEvent event) {
				
		Block iblock = event.getClickedBlock();
		Player playa = event.getPlayer();
		
		if(playa.hasPermission("hiddenswitch.use") == false) {
			return;
		}
		
		Boolean gogogo = false;

		// Are left clicks allowed?
		Boolean cclicks = plugin.getConfig().getBoolean("lchs.config.left-clicks");

		// Compare action to allowed clicks:
		if(cclicks) {
			if(event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				gogogo = true;
			}
		} else {
			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
				gogogo = true;
			}
		}
		
	
		// See if the block that was clicked is a useable block
		try {
			if(!plugin.confV.useableBlocks.contains(iblock.getTypeId())) {
				gogogo = false;
			}
		} catch(NullPointerException e) {
			gogogo=false;
		}

		// if we are still good to go then continue
		if(gogogo == true) {

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
	}
	
	public void signSlapper(Block signToSlap, Player playa) {
		
		Sign hola = (Sign)signToSlap.getState();
		String slappyFace = hola.getLine(0).toLowerCase();
		Boolean failed = false;
		
		// Is this a lchs sign?
		if(slappyFace.equals(plugin.getConfig().getString("lchs.signcontrol.sign-text").toLowerCase())) {
			
			// So it is, now do we allow per player restrictions?
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


			// Flippa da switch
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
				
				for(BlockFace holder : faces) {
					
					// find all the levers next to the sign
					if (signToSlap.getRelative(holder).getTypeId() == 69) {
						
						levers = signToSlap.getRelative(holder);
						BlockState statte = levers.getState();
						Lever lerer = (Lever)statte.getData();
						
						// Toggle Lever
						if(lerer.isPowered()) lerer.setPowered(false); else lerer.setPowered(true);
						
						// Update Lever and block it is attached to
						statte.update();
						
						
						//Try and power the block the switch is on.
							
					}
					if (signToSlap.getRelative(holder).getTypeId() == 77) {
						
						levers = signToSlap.getRelative(holder);
						BlockState statte = levers.getState();
						Button lerer = (Button)statte.getData();
						

						// Toggle Lever
						if(lerer.isPowered()) {
							lerer.setPowered(false);
						} else {
							lerer.setPowered(true);
						}
						
						// Update Lever and block it is attached to
						statte.update();

						
						// Cheat to pass vars into new runnable
						class RunShot implements Runnable {
							Button lerer;
							BlockState statte;
							RunShot(Button df, BlockState re) {
								this.lerer = df;
								this.statte = re;
							}
							public void run() {
								lerer.setPowered(false);
								statte.update();
							}
						}
						
						plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new RunShot(lerer,statte), 18L);
					}
				}
			}
		}
	}
}
