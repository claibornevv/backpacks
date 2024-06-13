package com.bigweas.backpacks;

// Importing classes for registering commands and listeners
import com.bigweas.backpacks.commands.BackpackCommand;
import com.bigweas.backpacks.commands.SeebackpackCommand;
import com.bigweas.backpacks.commands.SetBackpackSizeCommand;
import com.bigweas.backpacks.commands.SetDefaultBackpackSizeCommand;
import com.bigweas.backpacks.inventories.BackpackHolder;
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
import java.util.List;
import java.util.UUID;

public final class Backpacks extends JavaPlugin {

    @Override
    public void onEnable() {

        // Saves the config file that is established
        saveDefaultConfig();

        // Registering commands
        getCommand("backpack").setExecutor(new BackpackCommand(this));
        getCommand("seebackpack").setExecutor(new SeebackpackCommand(this));
        getCommand("setdefaultbackpacksize").setExecutor(new SetDefaultBackpackSizeCommand(this));
        getCommand("setbackpacksize").setExecutor(new SetBackpackSizeCommand(this));

        // Registering Event Listeners
        getServer().getPluginManager().registerEvents(new BackpackListener(this), this);

        // Create the backpacks.yml file if it does not exist
        createBackpacksFile();

        // Sending log to console that the plugin is ready
        getLogger().info("Backpack plugin has loaded!");

    }

    @Override
    public void onDisable() {}

    // Getter method for getting the default backpack size from config.yml
    public int getDefaultBackpackSize() { return getConfig().getInt("default-backpack-size"); }

    // Checker method to make sure that the size being used is appropriate for a backpack
    // Must be a multiple of 9 and less than 54
    public boolean checkCorrectBackpackSize(int size) { return (size <= 54 && size % 9 == 0); }

    // Get the backpack size of a specific player from the backpacks.yml file
    public int getPlayerBackpackSize(UUID playerUUID) {
        // Get the backpacks.yml file and create file if it doesn't exist
        File file = new File(getDataFolder(), "backpacks.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Load the config file into a YamlConfiguration object
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Return the value in the PlayerUUID.size section of the config
        return config.getInt(playerUUID + ".size");
    }

    // Helper method to set the size of a player's backpack in the playerUUID.size field in the backpacks.yml file
    public void setPlayerBackpackSize(UUID playerUUID, int size) {
        // Get the file
        File file = new File(getDataFolder(), "backpacks.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Load into YamlConfiguration object
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Get the items
        List<ItemStack> items = (List<ItemStack>) config.getList(playerUUID.toString() + ".contents");

        // If the item lists is null, just set the new size in the playerUUID.contents field
        // TODO: write logic for null ItemStack
        if (items == null) {
            ItemStack[] newItems = new ItemStack[0];
        }

        // Move the items into the newly sized ItemStack
        ItemStack[] adjustedItems = new ItemStack[size];
        for (int i = 0; i < Math.min(size, items.size()); i++) {
            adjustedItems[i] = items.get(i);
        }

        // Make sure the config contains the player
        if (config.contains(playerUUID.toString())) {
            // Set the new size field if the player exists
            config.set(playerUUID.toString() + ".size", size);
        } else {
            config.set(playerUUID.toString() + ".size", size);
            config.set(playerUUID.toString() + ".contents", new ItemStack[0]);
        }

        // Save the config file
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // public method to load backpack inventory of specified player using UUID
    public Inventory loadBackpack (UUID playerUUID) {
        // Get the file (config file) and return null if it doesn't exist
        File file = new File(getDataFolder(), "backpacks.yml");
        if (!file.exists()) return null;

        // Create the config object
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Create a list of ItemStack objects and return null if the list is empty
        List<ItemStack> items = (List<ItemStack>) config.getList(playerUUID.toString() + ".contents");
        int backpackSize = config.getInt(playerUUID.toString() + ".size", getDefaultBackpackSize());
        if (items == null) return null;

        // Create an inventory object for the player to see their backpack and set the contents of the backpack
        Inventory inventory = Bukkit.createInventory(new BackpackHolder(null), backpackSize,
                getServer().getPlayer(playerUUID).getDisplayName() + "'s Backpack");
        inventory.setContents(items.toArray(new ItemStack[0]));

        // Return the inventory
        return inventory;
    }

    // Method to save the backpack
    public void saveBackpack (UUID playerUUID, Inventory inventory) {
        // Get the file
        File file = new File(getDataFolder(), "backpacks.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Create the YamlConfiguration object after getting the file
        YamlConfiguration config = new YamlConfiguration().loadConfiguration(file);

        // Set the relevant information like inventory data and backpack size
        config.set(playerUUID.toString() + ".contents", inventory.getContents());
        config.set(playerUUID.toString() + ".size", inventory.getSize());

        // Save the file
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to create a backpacks plugin config file (really for storing backpack data)
    private void createBackpacksFile() {
        // Get the file
        File file = new File(getDataFolder(), "backpacks.yml");
        // Create file if it doesn't exist
        if (!file.exists()) {
            try {
                // Log that the new file is being generated
                getLogger().info("Creating new backpack.yml file");
                file.getParentFile().mkdirs();
                file.createNewFile();
                // Make the new config after loading the new file
                YamlConfiguration config = new YamlConfiguration().loadConfiguration(file);
                // Save the config
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
