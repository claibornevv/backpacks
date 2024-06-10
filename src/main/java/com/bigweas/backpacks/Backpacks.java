package com.bigweas.backpacks;

// Importing classes for registering commands and listeners
import com.bigweas.backpacks.commands.BackpackCommand;
import com.bigweas.backpacks.commands.SeebackpackCommand;
import com.bigweas.backpacks.listeners.BackpackListener;

// Bukkit imports
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

// Java imports
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class Backpacks extends JavaPlugin {

    // Map for setting player backpacks (kind of like active storage compared to yml file)
    private final Map<UUID, Inventory> backpackMap = new HashMap<>();
    private final int defaultBackpackSize = getConfig().getInt("default-backpack-size");

    @Override
    public void onEnable() {

        // Registering commands
        getCommand("backpack").setExecutor(new BackpackCommand(this));
        getCommand("seebackpack").setExecutor(new SeebackpackCommand(this));

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

    // Getter for the backpackMap private variable
    public Map<UUID, Inventory> getBackpackMap() {
        return backpackMap;
    }

    public int getDefaultBackpackSize() { return defaultBackpackSize; }

    // public method to load backpack inventory of specified player using UUID
    public Inventory loadBackpack (UUID playerUUID) {
        // Get the file (config file) and return null if it doesn't exist
        File file = new File(getDataFolder(), "backpacks.yml");
        if (!file.exists()) return null;

        // Create the config object
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Create a list of ItemStack objects and return null if the list is empty
        List<ItemStack> items = (List<ItemStack>) config.getList(playerUUID.toString());
        if (items == null) return null;

        // Create an inventory object for the player to see their backpack and set the contents of the backpack
        // Then add that to the backpackMap
        Inventory inventory = Bukkit.createInventory(null, 27, getServer().getPlayer(playerUUID).getDisplayName() + "'s Backpack");
        inventory.setContents(items.toArray(new ItemStack[0]));
        backpackMap.put(playerUUID, inventory);

        // Return the inventory
        return inventory;
    }

    // Method to create a backpacks plugin config file (really for storing backpack data)
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

    // Method to save the states of backpacks into the yml file
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

    // Load backpacks from the .yml file (this is done at server startup)
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
