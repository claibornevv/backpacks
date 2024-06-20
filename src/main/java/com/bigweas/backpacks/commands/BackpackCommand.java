package com.bigweas.backpacks.commands;

// Bring in the Backpacks main class
import com.bigweas.backpacks.Backpacks;

// Bukkit classes
import com.bigweas.backpacks.commands.subcommands.BackpackReset;
import com.bigweas.backpacks.commands.subcommands.BackpackUpgrade;
import com.bigweas.backpacks.inventories.BackpackHolder;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.TabExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

// Default java classes
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BackpackCommand implements TabExecutor {

    // Create object of the plugin
    private final Backpacks plugin;

    // Subcommands
    private ArrayList<SubCommand> subcommands = new ArrayList<>();


    // Initialize plugin object in constructor
    public BackpackCommand(Backpacks plugin) {
        // Initialize instance of the plugin
        this.plugin = plugin;

        // Set the subcommands
        subcommands.add(new BackpackUpgrade(plugin));
        subcommands.add(new BackpackReset(plugin));
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

            } else { // Subcommand logic
                // Loop through the possible subcommands
                for (int i = 0; i < getSubcommands().size(); i++) {
                    // If the argument provided equals a subcommand, execute it
                    if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                        getSubcommands().get(i).perform(player, args);
                    }
                }
            }

        } else {
            // Send a response to command block or console about only players being allowed to use the command
            sender.sendMessage("This command can only be used by players");
        }

        return true;
    }


    public ArrayList<SubCommand> getSubcommands() { return subcommands; }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> subcommandsArguments = new ArrayList<>();

            for (int i = 0; i < getSubcommands().size(); i++) {
                subcommandsArguments.add(getSubcommands().get(i).getName());
            }

            return subcommandsArguments;
        }

        return null;
    }

}
