package com.bigweas.backpacks.commands;

// Bring in the Backpacks main class
import com.bigweas.backpacks.Backpacks;

// Bukkit classes
import com.bigweas.backpacks.inventories.BackpackHolder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

// Default java classes
import java.util.UUID;

public class BackpackCommand implements CommandExecutor {

    // Create object of the plugin
    private final Backpacks plugin;

    // Initialize plugin object in constructor
    public BackpackCommand(Backpacks plugin) {
        this.plugin = plugin;
    }

    // Backpack command itself with all it's functionality
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Check to make sure the person running the command is not a player
        // If command block or console, do not run rest of code
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players");
            return true;
        }

        // Get important player information and their backpack if it exists
        UUID playerUUID = player.getUniqueId();
        Inventory backpack = plugin.loadBackpack(playerUUID);

        // If their backpack does not exist, create a new backpack and store it
        if (backpack == null) {
            backpack = plugin.getServer().createInventory(new BackpackHolder(null),
                    plugin.getDefaultBackpackSize(), "Backpack");
            plugin.saveBackpack(playerUUID, backpack);
        }

        // Open the backpack up to the player
        player.openInventory(backpack);
        return true;
    }

}
