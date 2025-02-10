package org.dutch.backpacks.commands;

import org.bukkit.entity.Player;

import java.util.List;

public interface BackpackCommand {

    void execute(Player player, String[] args);
    List<String> onTabComplete(Player player, String[] args);
}
