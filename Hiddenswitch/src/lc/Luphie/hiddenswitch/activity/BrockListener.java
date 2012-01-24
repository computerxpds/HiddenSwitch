/*
 *
 * HiddenSwitch - Hidden switches and buttons for Bukkit
 * Copyright (C) 2011-2012  Luphie (devLuphie) luphie@lumpcraft.com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */



package lc.Luphie.hiddenswitch.activity;

//~--- non-JDK imports --------------------------------------------------------

import lc.Luphie.hiddenswitch.HiddenSwitch;
import lc.Luphie.hiddenswitch.utilities.KeyBlock;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockListener;

//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

public class BrockListener extends BlockListener {
    private HiddenSwitch me;

    public BrockListener() {

        me = HiddenSwitch.instance;

        // Bukkit.getServer().getPluginManager().registerEvents(this, me);
        Bukkit.getServer().getPluginManager().registerEvent(Event.Type.SIGN_CHANGE,
                this,
                Event.Priority.Highest,
                me);
        Bukkit.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BREAK,
                this,
                Event.Priority.Highest,
                me);
        Bukkit.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BURN,
                this,
                Event.Priority.Highest,
                me);

    }

    // @EventHandler(priority = EventPriority.HIGHEST)
    @Override
    public void onBlockBreak(BlockBreakEvent ev) {

        if (ev.isCancelled()) {
            return;
        }

        if (!removeHiddenSwitch(ev.getBlock())) {
            return;
        }

        ev.getPlayer().sendMessage(me.lang.getLang().getString("language.messages.breakswitch"));

    }

    // @EventHandler(priority = EventPriority.HIGHEST)
    @Override
    public void onBlockBurn(BlockBurnEvent ev) {

        if (ev.isCancelled()) {
            return;
        }

        if (!removeHiddenSwitch(ev.getBlock())) {
            return;
        }

    }

    // @EventHandler(priority = EventPriority.HIGHEST)
    @Override
    public void onSignChange(SignChangeEvent ev) {

        // Make sure the event wasn't canceled before this
        if (ev.isCancelled()) {
            return;
        }

        // Are sign based switches allowed?
        if (!me.getConfig().getBoolean("lchs.signcontrol.allow-signs", true)) {
            return;
        }

        // Get the sign text
        String[] lns = ev.getLines();

        // Is this a HiddenSwitch sign?
        if (!lns[0].equals(me.getConfig().getString("lchs.signcontrol.sign-text", "[lchs]"))) {
            return;
        }

        // Get the involved Player
        Player pl = ev.getPlayer();

        // Does this player have permission to create a sign?
        if (!pl.hasPermission("hiddenswitch.user.create")) {
            signBreak(ev.getBlock(),
                      pl);

            return;
        }

        // Check if there is a user line set and if the player has that
        // permission
        if (!lns[1].isEmpty() &&!pl.hasPermission("hiddenswitch.user.lockuser")) {
            signBreak(ev.getBlock(),
                      pl);

            return;
        }

        // Check if there is a key line set and if the player has that
        // permission
        if (!lns[2].isEmpty() &&!pl.hasPermission("hiddenswitch.user.lockitem")) {
            signBreak(ev.getBlock(),
                      pl);

            return;
        }

        // Check if username shortcuts are enabled, and if so and they are using
        // one, replace the shortcut with their name
        if (me.getConfig().getBoolean("lchs.signcontrol.allow-username-shortcut") &&
                lns[1].equalsIgnoreCase(me.getConfig().getString("lchs.signcontrol.username-shortcut"))) {
            lns[1] = pl.getDisplayName();
        }
    }

    // TODO Secure this
    public boolean removeHiddenSwitch(Block block) {

        // See if it is a useable block
        if (!me.confV.usableBlocks.contains(block.getTypeId())) {
            return false;
        }

        // See if it is in the hashmap

        String id = KeyBlock.makeIdString(block.getWorld(),
                                          block.getX(),
                                          block.getY(),
                                          block.getZ());

        // If it's found remove it from the hashmap
        if (!me.blkCon.keyblocks.containsKey(id)) {

            return false;

        } else {

            KeyBlock key = me.blkCon.keyblocks.get(id);

            me.blkCon.keyblocks.remove(id);

            // If it is in the database then we need to remove it
            if (key.isInDatabase) {

                HiddenSwitch.DBH.dropRecord(id);

            }
        }

        return true;
    }

    public void signBreak(Block sign, Player pl) {

        sign.setTypeId(0);

        ItemStack signdrop = new ItemStack(323,
                                           1);

        pl.getWorld().dropItem(sign.getLocation(),
                               signdrop);
    }
}