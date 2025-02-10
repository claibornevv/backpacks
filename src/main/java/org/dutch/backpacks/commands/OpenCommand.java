package org.dutch.backpacks.commands;

import org.bukkit.entity.Player;
import org.dutch.backpacks.BackpacksPlugin;

import java.util.ArrayList;
import java.util.List;

public class OpenCommand implements BackpackCommand{

    private final BackpacksPlugin plugin;

    public OpenCommand(BackpacksPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("backpacks.open")) {
            player.sendMessage("§cYou don't have permission to open backpacks!");
            return;
        }

        if (args.length > 0 && player.hasPermission("backpacks.open.others")) {
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target != null) {
                plugin.getUtils().openBackpack(target);
                player.sendMessage("§aOpened backpack for " + target.getName());
            } else {
                player.sendMessage("§cPlayer not found!");
            }
        } else {
            plugin.getUtils().openBackpack(player);
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1 && player.hasPermission("backpacks.open.others")) {
            plugin.getServer().getOnlinePlayers().forEach(p -> completions.add(p.getName()));
        }

        return completions;
    }

}
