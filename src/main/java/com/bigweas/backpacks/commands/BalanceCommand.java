package com.bigweas.backpacks.commands;

import com.bigweas.backpacks.Backpacks;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {

    // Create object of the plugin
    private final Backpacks plugin;

    // Initialize plugin object in constructor
    public BalanceCommand(Backpacks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {
            player.sendMessage(ChatColor.GREEN + "Your balance is: $" +
                    plugin.getPlayerBalance(player.getUniqueId()));
        }

        return true;
    }

}
