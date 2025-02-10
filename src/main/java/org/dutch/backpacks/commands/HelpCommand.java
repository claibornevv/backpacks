package org.dutch.backpacks.commands;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HelpCommand implements BackpackCommand{
    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage("§6Backpack Commands:\n" +
                "§e/bp open §7- Open your backpack\n" +
                "§e/bp give [player] §7- Give a backpack item");
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return new ArrayList<>(); // No tab completions for help command
    }
}
