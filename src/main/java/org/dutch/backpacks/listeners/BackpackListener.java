package org.dutch.backpacks.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.dutch.backpacks.BackpacksPlugin;
import org.dutch.backpacks.utils.BackpackUtils;

public class BackpackListener implements Listener {

    private final BackpackUtils utils;

    public BackpackListener(BackpacksPlugin plugin) {
        this.utils = plugin.getUtils();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null && utils.isBackpackItem(item)) {
            utils.openBackpack(event.getPlayer());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals("Backpack")) {
            utils.saveBackpack((Player) event.getPlayer(), event.getInventory());
        }
    }
}
