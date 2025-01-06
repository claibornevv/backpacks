package org.dutch.backpacks.commands.backpack.subcommands;

import org.dutch.backpacks.BackpacksPlugin;
import org.dutch.backpacks.commands.SubCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.ChatColor;

public class BackpackOpen extends SubCommand {

    private final BackpacksPlugin plugin;

    public BackpackOpen(BackpacksPlugin plugin) { this.plugin = plugin; }

    @Override
    public String getName() { return "open"; }

    @Override
    public String getDescription() {
        return "Open your backpack or a specified player's backpack";
    }

    @Override
    public String getSyntax() { return "/backpack open <number>"; }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Please enter a number indicating which backpack to open /backpack open <number>");
            return;
        }

        int backpackNumber = Integer.parseInt(args[1]);

        Inventory backpack = plugin.loadBackpack(player.getUniqueId(), backpackNumber);

        if (backpack != null) {
            player.openInventory(backpack);
        } else {
            player.sendMessage(ChatColor.RED + "Backpack does not exist!");
        }
    }

}
