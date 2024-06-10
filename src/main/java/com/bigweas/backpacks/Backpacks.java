package com.bigweas.backpacks;

// Importing classes for registering commands and listeners
import com.bigweas.backpacks.commands.BackpackCommand;
import com.bigweas.backpacks.commands.SeebackpackCommand;
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

        // Registering Event Listeners
        getServer().getPluginManager().registerEvents(new BackpackListener(this), this);

        // Create the backpacks.yml file if it does not exist
        createBackpacksFile();

        // Sending log to console that the plugin is ready
        getLogger().info("Backpack plugin has loaded!");

    }

    @Override
    public void onDisable() {}

    public int getDefaultBackpackSize() { return getConfig().getInt("default-backpack-size"); }

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
        Inventory inventory = Bukkit.createInventory(new BackpackHolder(null), backpackSize, getServer().getPlayer(playerUUID).getDisplayName() + "'s Backpack");
        inventory.setContents(items.toArray(new ItemStack[0]));

        // Return the inventory
        return inventory;
    }

    // Method to save the backpack
    public void saveBackpack (UUID playerUUID, Inventory inventory) {
        File file = new File(getDataFolder(), "backpacks.yml");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        YamlConfiguration config = new YamlConfiguration().loadConfiguration(file);
        config.set(playerUUID.toString() + ".contents", inventory.getContents());
        config.set(playerUUID.toString() + ".size", inventory.getSize());

        try {
            config.save(file);
            getLogger().info("Saving backpack " + playerUUID.toString() + " to " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to create a backpacks plugin config file (really for storing backpack data)
    private void createBackpacksFile() {
        File file = new File(getDataFolder(), "backpacks.yml");
        if (!file.exists()) {
            try {
                getLogger().info("Creating new backpack.yml file");
                file.getParentFile().mkdirs();
                file.createNewFile();
                YamlConfiguration config = new YamlConfiguration().loadConfiguration(file);
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
