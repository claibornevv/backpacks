package com.bigweas.backpacks.listeners;

import com.bigweas.backpacks.Backpacks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class BackpackListener implements Listener {

    private final Backpacks plugin;

    public BackpackListener(Backpacks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        Inventory inventory = event.getInventory();

        if (plugin.getBackpackMap().containsKey(playerUUID) && plugin.getBackpackMap().get(playerUUID).equals(inventory)) {
            saveBackpack(playerUUID, inventory);
        }
    }

    private void saveBackpack(UUID playerUUID, Inventory inventory) {
        File file = new File(plugin.getDataFolder(), "backpacks.yml");

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration config = new YamlConfiguration().loadConfiguration(file);
        config.set(playerUUID.toString(), inventory.getContents());

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
