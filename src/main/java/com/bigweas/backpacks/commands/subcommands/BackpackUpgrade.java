package com.bigweas.backpacks.commands.subcommands;

import com.bigweas.backpacks.Backpacks;
import com.bigweas.backpacks.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BackpackUpgrade extends SubCommand {

    private final Backpacks plugin;

    public BackpackUpgrade(Backpacks plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() { return "upgrade"; }

    @Override
    public String getDescription() { return "Upgrade the size of your backpack"; }

    @Override
    public String getSyntax() { return "/backpack upgrade"; }

    @Override
    public void perform(Player player, String[] args) {
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

}
