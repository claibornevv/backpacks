package com.bigweas.backpacks.commands;

import com.bigweas.backpacks.Backpacks;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SetBackpackSizeCommand implements CommandExecutor {

    // Create instance of the backpack plugin
    private final Backpacks plugin;

    // Set the Backpacks object through the constructor
    public SetBackpackSizeCommand(Backpacks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Make sure that the person using the command is a player
        if (sender instanceof Player player) {

            if (args.length == 1) { // Condition without player argument

                // Get the size from the arguments as an integer
                int newBackpackSize;
                // Try the extract the number from the argument and throw and error to the player if it is not a number
                try {
                    newBackpackSize = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Size must be a number");
                    return true;
                }

                // Make sure that the input size is the correct backpack (inventory) size
                if (!plugin.checkCorrectBackpackSize(newBackpackSize)) {
                    player.sendMessage(ChatColor.RED +
                            "Must use a multiple of 9 for size and a number less than 54");
                    return true;
                }

                // Set the new backpack size using a helper method from the plugin main class
                plugin.setPlayerBackpackSize(player.getUniqueId(), newBackpackSize);

                // Send confirmation to player that the size has been adjusted
                player.sendMessage(ChatColor.GREEN + "Set your backpack to size of " + newBackpackSize);

            } else if (args.length == 2) { // Condition for a player argument

                // Grab the target player and the backpack size
                Player targetPlayer = plugin.getServer().getPlayer(args[0]);

                // Make sure the player given exists
                // If they don't exist, throw an error to the player giving the command
                if (targetPlayer == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found");
                    return true;
                }

                int newBackpackSize;
                // Try the extract the number from the argument and throw and error to the player if it is not a number
                try {
                    newBackpackSize = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Size must be a number");
                    return true;
                }

                // Make sure the player entered a proper backpack size and return if not
                if (!plugin.checkCorrectBackpackSize(newBackpackSize)) {
                    player.sendMessage(ChatColor.RED +
                            "Must use a multiple of 9 for size and a number less than 54");
                    return true;
                }

                // Set the new size of the backpack using helper method from the plugin main class
                plugin.setPlayerBackpackSize(targetPlayer.getUniqueId(), newBackpackSize);

                // Send a response to the player confirming that the size of the backpack has been changed
                player.sendMessage(ChatColor.GREEN + "Set " + targetPlayer.getDisplayName() +
                        "'s backpack to size of " + newBackpackSize);

            } else {
                sender.sendMessage(ChatColor.RED + "Usage: /setbackpacksize (optional):[player] <size>");
            }

        } else {
            sender.sendMessage("This command can only be used by players!");
        }

        return true;
    }

}
