package org.dutch.backpacks.listeners;

import org.dutch.backpacks.BackpacksPlugin;

// Bukkit classes
import org.dutch.backpacks.inventories.BackpackHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

// Java default classes
import java.util.UUID;

public class BackpackListener implements Listener {

    // Create object of plugin
    private final BackpacksPlugin plugin;

    // Set the object of plugin in the constructor
    public BackpackListener(BackpacksPlugin plugin) {
        this.plugin = plugin;
    }

    // Event handler for saving backpack when the backpack inventory is closed
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Get the inventory for the listener
        Inventory inventory = event.getInventory();

        // Make sure that the inventory being listened to is only a backpack and nothing else
        if (inventory.getHolder() instanceof BackpackHolder) {
            // If it is a backpack, get relevant player information
            Player player = (Player) event.getPlayer();
            UUID playerUUID = player.getUniqueId();

            // Save the backpack to the backpacks.yml file using helper function in Backpacks.java
            plugin.saveBackpack(playerUUID, inventory);
        }
    }

    // Listener for when players join the server and creates a backpack for them if it is their first time joining
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Get relevant player information
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        // Try loading the backpack and if it returns null, create a new backpack
        if (plugin.loadBackpack(playerUUID) == null) {
            // Create the backpack
            Inventory backpack = Bukkit.createInventory(new BackpackHolder(null),
                    plugin.getDefaultBackpackSize(), player.getName() + "'s Backpack");
            // Save the backpack
            plugin.saveBackpack(playerUUID, backpack);
        }
    }

}
