package com.bigweas.backpacks.commands;

import com.bigweas.backpacks.Backpacks;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SetDefaultBackpackSizeCommand implements CommandExecutor{

    private final Backpacks plugin;

    public SetDefaultBackpackSizeCommand(Backpacks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0) {
            int backpackSize = Integer.parseInt(args[0]);

            this.plugin.getConfig().set("default-backpack-size", backpackSize);
            this.plugin.saveConfig();
        } else {
            sender.sendMessage("Usage: /setdefaultbackpacksize <backpack size>");
        }

        return true;
    }

}
