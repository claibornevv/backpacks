package org.dutch.backpacks.commands;

import org.bukkit.entity.Player;
import org.dutch.backpacks.BackpacksPlugin;

import java.util.Collections;
import java.util.List;

public class UpgradeCommand implements BackpackCommand {

    private final BackpacksPlugin plugin;

    public UpgradeCommand(BackpacksPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        if (plugin.getUtils().upgradeBackpack((player))) {
            int newSize = plugin.getUtils().getBackpackSize(player);
            player.sendMessage("§aBackpack upgraded! New size: " + (newSize/9) + " rows");
            player.sendMessage("§eRemaining levels: " + player.getLevel());
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return Collections.emptyList();
    }

}
