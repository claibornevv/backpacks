package com.bigweas.backpacks.commands;

// Bring in the Backpacks class
import com.bigweas.backpacks.Backpacks;

// Bukkit classes
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

// Default java classes
import java.util.UUID;

public class SeebackpackCommand implements CommandExecutor{

    // Create object of plugin
    private final Backpacks plugin;

    // Set the object of plugin in the constructor
    public SeebackpackCommand(Backpacks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Make sure command is only coming from player and not console or command block
        if(!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players");
            return true;
        }

        // Make sure there is an argument for a player name
        if (args.length == 0) {
            sender.sendMessage("Usage: /seebackpack <player>");
        }

        // Collect the player name from the argument
        String playerName = args[0];

        // Get the command sender as Player object
        Player player = (Player) sender;
        // Get target player in command as Player object
        Player targetPlayer = Bukkit.getServer().getPlayerExact(playerName);
        // Get the UUID of the target player
        UUID targetPlayerUUID = targetPlayer.getUniqueId();
        // Find the backpack of the target player
        Inventory targetPlayerBackpack = plugin.loadBackpack(targetPlayerUUID);

        // Open the backpack of the target player (if it exists)
        if (targetPlayerBackpack == null) {
            player.sendMessage(ChatColor.RED + "target player does not have a backpack or does not exist");
        } else {
            player.openInventory(targetPlayerBackpack);
        }

        return true;
    }

}
