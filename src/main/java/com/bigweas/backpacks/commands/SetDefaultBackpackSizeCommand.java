package com.bigweas.backpacks.commands;

import com.bigweas.backpacks.Backpacks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetDefaultBackpackSizeCommand implements CommandExecutor{

    // Create plugin object
    private final Backpacks plugin;

    // Set the plugin object in the constructor
    public SetDefaultBackpackSizeCommand(Backpacks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Make sure that the arguments given are the correct length
        // TODO: Make sure the argument is a number
        if (args.length > 0) {
            // Get the size argument as an integer from the args variable
            int backpackSize = Integer.parseInt(args[0]);

            // Set the new default backpack size in the config.yml file
            this.plugin.getConfig().set("default-backpack-size", backpackSize);
            this.plugin.saveConfig();
        } else {
            // Send an error message to the player in game if they did not enter the correct input
            sender.sendMessage("Usage: /setdefaultbackpacksize <backpack size>");
        }

        return true;
    }

}
