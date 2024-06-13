package com.bigweas.backpacks.commands;

import com.bigweas.backpacks.Backpacks;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetBackpackSizeCommand implements CommandExecutor {

    private final Backpacks plugin;

    public SetBackpackSizeCommand(Backpacks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {

            if (args.length == 1) { // Condition without player argument

                // Get the size from the arguments as an integer
                int newBackpackSize = Integer.parseInt(args[0]);

                if (!plugin.checkCorrectBackpackSize(newBackpackSize)) {
                    player.sendMessage(ChatColor.RED +
                            "Must use a multiple of 9 for size and a number less than 54");
                    return true;
                }

                if (newBackpackSize < plugin.getPlayerBackpackSize(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You cannot decrease the size of your backpack! (for now)");
                    return true;
                }

                plugin.setPlayerBackpackSize(player.getUniqueId(), newBackpackSize);

                player.sendMessage(ChatColor.GREEN + "Set your backpack to size of " + newBackpackSize);

            } else if (args.length == 2) { // Condition for a player argument

                // Grab the target player and the backpack size
                Player targetPlayer = plugin.getServer().getPlayer(args[0]);
                int newBackpackSize = Integer.parseInt(args[1]);

                if (!plugin.checkCorrectBackpackSize(newBackpackSize)) {
                    player.sendMessage(ChatColor.RED +
                            "Must use a multiple of 9 for size and a number less than 54");
                    return true;
                }

                if (newBackpackSize < plugin.getPlayerBackpackSize(targetPlayer.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You cannot decrease the size of their backpack! (for now)");
                    return true;
                }

                plugin.setPlayerBackpackSize(targetPlayer.getUniqueId(), newBackpackSize);

                player.sendMessage(ChatColor.GREEN + "Set " + targetPlayer.getDisplayName() +
                        "'s backpack to size of " + newBackpackSize);

            }

        } else {
            sender.sendMessage("This command can only be used by players!");
        }

        return true;
    }

}
