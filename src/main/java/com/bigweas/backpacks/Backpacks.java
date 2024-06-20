/**
 * This is the main class for the Backpacks plugin
 * This class includes helper functions for other classes in the plugin
 * Most classes are documented using comments and Javadoc
 *
 * @author BigWeas
 * @version 1.0
 * @since 2024-06-10
 */

package com.bigweas.backpacks;

// Importing classes for registering commands and listeners
import com.bigweas.backpacks.commands.*;
import com.bigweas.backpacks.econ.CustomEconomy;
import com.bigweas.backpacks.inventories.BackpackHolder;
// Backpack holder custom class (this is for telling a backpack inventory apart from other inventories)
import com.bigweas.backpacks.listeners.BackpackListener;

// Bukkit imports
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

// Vault API
import net.milkbowl.vault.economy.Economy;

// Java imports
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public final class Backpacks extends JavaPlugin {

    // Objects for the VaultAPI
    private static Economy economy = null;

    @Override
    public void onEnable() {

        // Saves the config file that is established
        saveDefaultConfig();

        // Registering commands
        getCommand("backpack").setExecutor(new BackpackCommand(this));
        getCommand("seebackpack").setExecutor(new SeebackpackCommand(this));
        getCommand("setdefaultbackpacksize").setExecutor(new SetDefaultBackpackSizeCommand(this));
        getCommand("setbackpacksize").setExecutor(new SetBackpackSizeCommand(this));
        getCommand("balance").setExecutor(new BalanceCommand(this));

        // Registering Event Listeners
        getServer().getPluginManager().registerEvents(new BackpackListener(this), this);

        // Create the backpacks.yml file if it does not exist
        createBackpacksFile();

        // Setting the API connection with Vault economy
        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Sending log to console that the plugin is ready
        getLogger().info("Backpack plugin has loaded!");

    }

    @Override
    public void onDisable() {}

    // Getter method for getting the default backpack size from config.yml
    public int getDefaultBackpackSize() { return getConfig().getInt("default-backpack-size"); }
    public double getStartingBalance() { return getConfig().getDouble("starting-balance"); }
    public double getUpgradeCost() { return getConfig().getDouble("backpack-upgrade-price"); }

    /**
     * Checker method to make sure that the size being used is appropriate for a backpack
     * Must be a multiple of 9 and less than 54
     *
     * @param size size of backpack to be checked
     * @return true or false depending on whether the input size is a multiple of 9 and less than 55
     */
    public boolean checkCorrectBackpackSize(int size) { return (size <= 54 && size % 9 == 0); }



    /**
     * Get the backpack size of a specific player from the backpacks.yml file
     *
     * @param playerUUID the UUID of a player
     * @return size of the input player's backpack
     */
    public int getPlayerBackpackSize(UUID playerUUID) {
        // Load the config file into a YamlConfiguration object
        YamlConfiguration config = getBackpacksConfig();

        // Return the value in the PlayerUUID.size section of the config
        return config.getInt(playerUUID + ".size");
    }

    public double getPlayerBalance(UUID playerUUID) {
        YamlConfiguration config = getBackpacksConfig();
        return config.getDouble(playerUUID + ".balance");
    }

    // Getters for the Vault API
    public static Economy getEconomy() { return economy; }


    /**
     * Helper method to set the size of a player's backpack in the playerUUID.size field in the backpacks.yml file
     *
     * @param playerUUID Unique ID of the player
     * @param size Size of the backpack to be set
     */
    public void setPlayerBackpackSize(UUID playerUUID, int size) {
        // Load into YamlConfiguration object
        YamlConfiguration config = getBackpacksConfig();

        // Get the items
        List<ItemStack> items = (List<ItemStack>) config.getList(playerUUID.toString() + ".contents");

        // If the item lists is null, just set the new size in the playerUUID.contents field
        // TODO: write logic for null ItemStack
        if (items == null) {
            ItemStack[] newItems = new ItemStack[0];
        }

        // Move the items into the newly sized ItemStack
        // TODO: need to rewrite how to check for non-null item and place in a different slot of the item stack
        ItemStack[] adjustedItems = new ItemStack[size];
        for (int i = 0; i < Math.min(size, items.size()); i++) {
            if (items.get(i) != null) {
                adjustedItems[i] = items.get(i);
            }
        }

        // Make sure the config contains the player
        if (config.contains(playerUUID.toString())) {
            // Set the new size field if the player exists
            config.set(playerUUID.toString() + ".size", size);
            config.set(playerUUID.toString() + ".contents", adjustedItems);
        } else {
            config.set(playerUUID.toString() + ".size", size);
            config.set(playerUUID.toString() + ".contents", new ItemStack[0]);
        }

        // Save the config file
        saveBackpacksConfig(config);

    }


    /**
     * public method to load backpack inventory of specified player using UUID
     *
     * @param playerUUID Unique ID of a player
     * @return Returns the inventory of the specified player and creates an empty one if there's none tied to player
     */
    public Inventory loadBackpack (UUID playerUUID) {
        // Create the config object
        YamlConfiguration config = getBackpacksConfig();

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


    /**
     * Method to save the backpack
     *
     * @param playerUUID Unique ID of player
     * @param inventory Inventory of a specified player (should be of the same player in the playerUUID parameter
     */
    public void saveBackpack (UUID playerUUID, Inventory inventory, double balance) {
        // Create the YamlConfiguration object after getting the file
        YamlConfiguration config = getBackpacksConfig();

        // Set the relevant information like inventory data and backpack size
        config.set(playerUUID.toString() + ".contents", inventory.getContents());
        config.set(playerUUID.toString() + ".size", inventory.getSize());
        config.set(playerUUID.toString() + ".balance", balance);

        // Save the file
        saveBackpacksConfig(config);
    }


    // Method to create a backpacks.yml file to store backpack data for all players
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


    /**
     * Setup method for the VaultAPI
     * Checks to see if the Vault plugin is added to the server
     * @return Returns true if the Vault plugin is added to the server and the Economy exists
     */
    private boolean setupEconomy() {
        // Try to find the Vault plugin jar file in the plugins folder
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Vault plugin not found!");
            return false;
        }

        // Set up the custom economy service for this plugin
        getServer().getServicesManager().register(Economy.class, new CustomEconomy(this), this,
                ServicePriority.Highest);

        // Get the RegisteredServiceProvider
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        // Make sure rsp is not null
        if (rsp == null) {
            getLogger().severe("No economy provider found!");
            return false;
        }
        // Get the provider and return true
        economy = rsp.getProvider();
        getLogger().info("Economy provider found: " + economy.getName());
        return economy != null;
    }

    public YamlConfiguration getBackpacksConfig() {
        File file = new File(getDataFolder(), "backpacks.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    public void saveBackpacksConfig(YamlConfiguration config) {
        // Get the backpack.yml file
        File file = new File(getDataFolder(), "backpacks.yml");

        // Save the config object to the file
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
