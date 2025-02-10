package org.dutch.backpacks.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.dutch.backpacks.BackpacksPlugin;

import java.util.ArrayList;
import java.util.List;

public class GiveCommand implements BackpackCommand{

    private final BackpacksPlugin plugin;

    public GiveCommand(BackpacksPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        Player target = args.length > 0 ? Bukkit.getPlayer(args[0]) : player;
        if (target != null) {
            target.getInventory().addItem(plugin.getUtils().createBackpackItem());
            player.sendMessage("§aGiven backpack to " + target.getName());
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            Bukkit.getOnlinePlayers().forEach(p -> completions.add(p.getName()));
        }

        return completions;
    }

}
