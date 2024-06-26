package com.bigweas.backpacks.commands.subcommands;

import com.bigweas.backpacks.Backpacks;
import com.bigweas.backpacks.commands.SubCommand;
import com.bigweas.backpacks.inventories.BackpackHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class BackpackReset extends SubCommand {

    private final Backpacks plugin;

    public BackpackReset(Backpacks plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "reset"; }

    @Override
    public String getDescription() { return "resets the backpack to default settings"; }

    @Override
    public String getSyntax() { return "/backpack reset"; }

    @Override
    public void perform(Player player, String[] args) {

        // Get relevant player information
        UUID playerUUID = player.getUniqueId();

        // Create a fresh inventory
        Inventory resetBackpack = Bukkit.createInventory(new BackpackHolder(null),
                plugin.getDefaultBackpackSize(), player.getDisplayName() + "'s Backpack");

        // For resetting the player's own backpack
        if (args.length == 1) {

            // Save the backpack and send a confirmation message to the player
            plugin.saveBackpack(playerUUID, resetBackpack, plugin.getStartingBalance());
            player.sendMessage(ChatColor.GREEN + "Your backpack has been reset!");

        } else if (args.length == 2) { // For resetting another player's backpack

            // Check to make sure the command sender has permission to use this command
            if (!player.hasPermission("backpacks.reset.others")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use that command!");
                return;
            }

            // Get the target player from the argument
            Player targetPlayer = Bukkit.getPlayer(args[1]);

            // Make sure the player exists
            if (targetPlayer == null) {
                // If the player does not exist, let the player executing the command know and return
                player.sendMessage(ChatColor.RED + "Player not found!");
                return;
            }

            // Reset the backpack for the target player
            plugin.saveBackpack(targetPlayer.getUniqueId(), resetBackpack, plugin.getStartingBalance());
            player.sendMessage(ChatColor.GREEN + "The backpack has been reset for: " +
                    targetPlayer.getDisplayName());

        }

    }

}
