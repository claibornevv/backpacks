package org.dutch.backpacks;

import org.bukkit.entity.Player;
import org.dutch.backpacks.commands.backpack.BackpackCommand;
import org.dutch.backpacks.inventories.BackpackHolder;
import org.dutch.backpacks.listeners.BackpackListener;

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

public final class BackpacksPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Registering commands
        getCommand("backpack").setExecutor(new BackpackCommand(this));

        // Registering Event Listeners
        getServer().getPluginManager().registerEvents(new BackpackListener(this), this);

        // Create the backpacks.yml file if it does not exist
        createBackpacksFile();

        getLogger().info("Backpack plugin has loaded!");
    }

    @Override
    public void onDisable() {}

    // Getter method for getting the default backpack size from config.yml
    public int getDefaultBackpackSize() { return getConfig().getInt("default-backpack-size"); }

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
        YamlConfiguration config = getBackpacksConfig();
        return config.getInt(playerUUID + ".backpack.size");
    }


    /**
     * Helper method to set the size of a player's backpack in the playerUUID.size field in the backpacks.yml file
     *
     * @param player Player object used to reference UUID and name
     * @param size Size of the backpack to be set
     */
    public void setPlayerBackpackSize(Player player, int size) {
        Inventory backpack = loadBackpack(player.getUniqueId());
        Inventory newBackpack = Bukkit.createInventory(new BackpackHolder(null), size,
                player.getName() + "'s Backpack");

        // Copy items from old backpack to new backpack
        for (int slot = 0; slot < backpack.getSize(); slot++) {
            ItemStack item = backpack.getItem(slot);
            if (item != null) {
                newBackpack.setItem(slot, item);
            }
        }

        saveBackpack(player.getUniqueId(), newBackpack);
    }


    /**
     * public method to load backpack inventory of specified player using UUID
     *
     * @param playerUUID Unique ID of a player
     * @return Returns the inventory of the specified player and creates an empty one if there's none tied to player
     */
    public Inventory loadBackpack (UUID playerUUID) {
        YamlConfiguration config = getBackpacksConfig();

        String backpackName = getServer().getPlayer(playerUUID).getName() + "'s Backpack";
        if (backpackName == null) {
            backpackName = "Your backpack";
        }

        int size = config.getInt(playerUUID + ".backpack.size");
        Inventory backpack =  Bukkit.createInventory(new BackpackHolder(null), size, backpackName);

        // Transfer ItemStack items from the config to the Inventory object
        List<?> itemList = config.getList(playerUUID + ".backpack.contents");
        if (itemList != null) {
            for (int i = 0; i < itemList.size(); i++) {
                Object obj = itemList.get(i);
                if (obj instanceof ItemStack) {
                    backpack.setItem(i, (ItemStack) obj);
                }
            }
        }

        return backpack;
    }

    public Inventory loadBackpack(Player player) {
        UUID playerUUID = player.getUniqueId();
        String playerName = player.getName();
        YamlConfiguration config = getBackpacksConfig();

        String backpackName = playerName + "'s Backpack";

        int size = getPlayerBackpackSize(playerUUID);
        Inventory backpack =  Bukkit.createInventory(new BackpackHolder(null), size, backpackName);

        // Transfer ItemStack items from the config to the Inventory object
        List<?> itemList = config.getList(playerUUID + ".backpack.contents");
        if (itemList != null) {
            for (int i = 0; i < itemList.size(); i++) {
                Object obj = itemList.get(i);
                if (obj instanceof ItemStack) {
                    backpack.setItem(i, (ItemStack) obj);
                }
            }
        }

        return backpack;
    }


    /**
     * Method to save the backpack
     *
     * @param playerUUID Unique ID of player
     * @param inventory Inventory of a specified player (should be of the same player in the playerUUID parameter
     */
    public void saveBackpack (UUID playerUUID, Inventory inventory) {
        // Create the YamlConfiguration object after getting the file
        YamlConfiguration config = getBackpacksConfig();

        // Set the relevant information like inventory data and backpack size
        config.set(playerUUID + ".backpack.contents", inventory.getContents());
        config.set(playerUUID + ".backpack.size", inventory.getSize());

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
