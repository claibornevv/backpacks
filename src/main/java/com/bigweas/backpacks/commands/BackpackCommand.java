package com.bigweas.backpacks.commands;

import com.bigweas.backpacks.Backpacks;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class BackpackCommand implements CommandExecutor {

    private final Backpacks plugin;

    public BackpackCommand(Backpacks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players");
            return true;
        }

        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        Inventory backpack = plugin.loadBackpack(playerUUID);

        if (backpack == null) {
            backpack = plugin.getServer().createInventory(null, 27, "Backpack");
            plugin.getBackpackMap().put(playerUUID, backpack);
        }

        player.openInventory(backpack);
        return true;
    }

}
