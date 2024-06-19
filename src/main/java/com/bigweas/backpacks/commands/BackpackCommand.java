package com.bigweas.backpacks.commands;

// Bring in the Backpacks main class
import com.bigweas.backpacks.Backpacks;

// Bukkit classes
import com.bigweas.backpacks.inventories.BackpackHolder;
import org.bukkit.ChatColor;
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Make sure the command sender is a player and not a command block or console
        if (sender instanceof Player player) {

            if (args.length == 0) {

                // Get relevant player information
                UUID playerUUID = player.getUniqueId();
                Inventory backpack = plugin.loadBackpack(playerUUID);

                // If the backpack does not exist, create a new one and save it
                if (backpack == null) {
                    backpack = plugin.getServer().createInventory(new BackpackHolder(null),
                            plugin.getDefaultBackpackSize(), player.getDisplayName() + "'s Backpack");
                    plugin.saveBackpack(playerUUID, backpack, plugin.getStartingBalance());
                }

                // Open the backpack to the player
                player.sendMessage(ChatColor.GREEN + "Opening your backpack");
                player.openInventory(backpack);

            } else if (args.length == 1 && args[0].equals("upgrade")) {
                // TODO: implement the backpack upgrade feature
                // This will allow players to upgrade their backpacks using Vault currency
                // Each time they upgrade their backpack, it adds a row to the backpack
                // Eventually, there could be a way to upgrade to a second backpack
                player.sendMessage(ChatColor.YELLOW + "this feature has not been fully implemented yet");
                // Get the players current balance
                double playerBalance = plugin.getEconomy().getBalance(player);

                // Make sure their balance is enough to upgrade depending on what's in the config
                if (playerBalance >= plugin.getUpgradeCost()) {
                    // Get the size of the player's backpack
                    int playerBackpackSize = plugin.getPlayerBackpackSize(player.getUniqueId());
                    // Check to make sure their backpack is upgradable
                    if (playerBackpackSize > 0 && playerBackpackSize < 54) {
                        // Load their backpack. This is for saving it once the size changes
                        Inventory playerBackpack = plugin.loadBackpack(player.getUniqueId());
                        // Set the new size of the backpack (basically just add 9 to it)
                        plugin.setPlayerBackpackSize(player.getUniqueId(), playerBackpackSize+9);

                        // Subtract the cost of upgrading the backpack
                        plugin.getEconomy().withdrawPlayer(player, plugin.getUpgradeCost());

                        // Save the backpack to apply the change
                        plugin.saveBackpack(player.getUniqueId(),
                                plugin.loadBackpack(player.getUniqueId()), plugin.getEconomy().getBalance(player));

                        // Send confirmation to the player
                        player.sendMessage(ChatColor.GREEN + "Your backpack has been upgraded!");
                    } else {
                        player.sendMessage(ChatColor.RED + "Your backpack is already maxed out!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You do not have the funds to upgrade your backpack");
                }
            }

        } else {
            // Send a response to command block or console about only players being allowed to use the command
            sender.sendMessage("This command can only be used by players");
        }

        return true;
    }

}
