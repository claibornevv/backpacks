package com.bigweas.backpacks;

import com.bigweas.backpacks.commands.BackpackCommand;
import com.bigweas.backpacks.commands.FeedCommand;
import com.bigweas.backpacks.commands.GodCommand;
import com.bigweas.backpacks.listeners.BackpackListener;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class Backpacks extends JavaPlugin {

    private final Map<UUID, Inventory> backpackMap = new HashMap<>();

    @Override
    public void onEnable() {

        // Registering commands
        getCommand("god").setExecutor(new GodCommand());
        getCommand("feed").setExecutor(new FeedCommand());
        getCommand("backpack").setExecutor(new BackpackCommand(this));

        // Registering Event Listeners
        getServer().getPluginManager().registerEvents(new BackpackListener(this), this);

        // Create the backpacks.yml file if it does not exist
        createBackpacksFile();

        // Load the backpacks into the map
        loadBackpacks();

        // Sending log to console that the plugin is ready
        getLogger().info("Backpack plugin has loaded!");

    }

    @Override
    public void onDisable() { saveBackpacks(); } // Save the backpacks when the server is stopped

    public Map<UUID, Inventory> getBackpackMap() {
        return backpackMap;
    }

    public Inventory loadBackpack (UUID playerUUID) {
        File file = new File(getDataFolder(), "backpacks.yml");
        if (!file.exists()) return null;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        List<ItemStack> items = (List<ItemStack>) config.getList(playerUUID.toString());
        if (items == null) return null;

        Inventory inventory = Bukkit.createInventory(null, 27, "Backpack");
        inventory.setContents(items.toArray(new ItemStack[0]));
        backpackMap.put(playerUUID, inventory);

        return inventory;
    }

    private void createBackpacksFile() {
        File file = new File(getDataFolder(), "backpacks.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                YamlConfiguration config = new YamlConfiguration().loadConfiguration(file);
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveBackpacks() {
        createBackpacksFile();
        File file = new File(getDataFolder(), "backpacks.yml");
        YamlConfiguration config = new YamlConfiguration();

        for (Map.Entry<UUID, Inventory> entry : backpackMap.entrySet()) {
            UUID playerUUID = entry.getKey();
            Inventory inventory = entry.getValue();
            config.set(playerUUID.toString(), inventory.getContents());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBackpacks() {
        File file = new File(getDataFolder(), "backpacks.yml");
        if (!file.exists()) return;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String key : config.getKeys(false)) {
            UUID playerUUID = UUID.fromString(key);
            ItemStack[] items = ((List<ItemStack>) config.get(playerUUID.toString())).toArray(new ItemStack[0]);
            Inventory inventory = Bukkit.createInventory(null, 27, "Backpack");
            inventory.setContents(items);
            backpackMap.put(playerUUID, inventory);
        }

    }

}
