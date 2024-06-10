package com.bigweas.backpacks.listeners;

// Getting the backpack plugin
import com.bigweas.backpacks.Backpacks;

// Bukkit classes
import com.bigweas.backpacks.inventories.BackpackHolder;
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
    private final Backpacks plugin;

    // Set the object of plugin in the constructor
    public BackpackListener(Backpacks plugin) {
        this.plugin = plugin;
    }

    // Event handler for saving backpack when the backpack inventory is closed
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.getHolder() instanceof BackpackHolder) {
            Player player = (Player) event.getPlayer();
            UUID playerUUID = player.getUniqueId();
            plugin.getLogger().info("Inventory closed, saving player inventory of" + playerUUID);
            plugin.saveBackpack(playerUUID, inventory);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();

        if (plugin.loadBackpack(playerUUID) == null) {
            plugin.getLogger().info("Creating new backpack for " + playerUUID);
            Inventory backpack = Bukkit.createInventory(new BackpackHolder(null), 27, player.getDisplayName() + "'s Backpack");
            plugin.saveBackpack(playerUUID, backpack);
        }
    }

}
