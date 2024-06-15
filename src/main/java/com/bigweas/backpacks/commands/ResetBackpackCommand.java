/**
 * This is the command file for resetting backpacks
 * Uses an instance of the Backpacks.java file class to run public methods
 *
 * @author BigWeas
 * @version 1.0
 * @since 2024-06-12
 */

package com.bigweas.backpacks.commands;

// Project imports
import com.bigweas.backpacks.Backpacks;
import com.bigweas.backpacks.inventories.BackpackHolder;

// Bukkit imports
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

// Java imports
import java.util.UUID;

public class ResetBackpackCommand implements CommandExecutor {

    // Creating object for the instance of the plugin
    private final Backpacks plugin;

    // Initialize plugin object through constructor
    public ResetBackpackCommand(Backpacks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Make sure a player is executing the command
        if (sender instanceof Player player) {
            // Get relevant player information
            UUID playerUUID = player.getUniqueId();

            // Create a fresh inventory
            Inventory resetBackpack = Bukkit.createInventory(new BackpackHolder(null),
                    plugin.getDefaultBackpackSize(), player.getDisplayName() + "'s Backpack");

            // If a player argument is given, reset backpack for target player
            if (args.length > 0 && args.length < 2) {

                // Get the target player from the argument
                Player targetPlayer = Bukkit.getPlayer(args[0]);

                // Make sure the player exists
                if (targetPlayer == null) {
                    // If the player does not exist, let the player executing the command know and return
                    player.sendMessage(ChatColor.RED + "Player not found!");
                    return true;
                }

                // Reset the backpack for the target player
                plugin.saveBackpack(targetPlayer.getUniqueId(), resetBackpack);

            } else if (args.length == 0) { // If no arguments given, reset backpack for player executing command

                // Save the backpack and send a confirmation message to the player
                plugin.saveBackpack(playerUUID, resetBackpack);
                player.sendMessage(ChatColor.GREEN + "Your backpack has been reset!");

            }

        } else {
            // Let the command block or console know that you need to be a player to run this command
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
        }

        // Return true :)
        return true;
    }

}
