package org.dutch.backpacks.commands;

import org.bukkit.entity.Player;
import org.dutch.backpacks.BackpacksPlugin;

import java.util.ArrayList;
import java.util.List;

public class ResetCommand implements BackpackCommand {

    private final BackpacksPlugin plugin;

    public ResetCommand(BackpacksPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length > 0 && !player.hasPermission("backpacks.reset.others")) {
            player.sendMessage("You don't have permission to reset other players' backpacks!");
            return;
        }

        Player target = args.length > 0 ? plugin.getServer().getPlayer(args[0]) : player;

        if (target == null) {
            player.sendMessage("Player not found!");
            return;
        }

        plugin.getUtils().setBackpackSize(target, 9);
        player.sendMessage("Backpack reset to default size for " + target.getName() + "!");

        if (!target.equals(player)) {
            target.sendMessage("Your backpack has been reset to default size by " + player.getName() + "!");
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1 && player.hasPermission("backpacks.reset.others")) {
            plugin.getServer().getOnlinePlayers().forEach(p -> completions.add(p.getName()));
        }

        return completions;
    }

}
