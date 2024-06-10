package com.bigweas.backpacks.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player p) {

            if (args.length == 0) {
                p.setFoodLevel(20);
                p.sendMessage(ChatColor.YELLOW + "You have been fed");
            } else {
                String playerName = args [0];
                Player targetPlayer = Bukkit.getServer().getPlayerExact(playerName);

                if (targetPlayer == null) {
                    p.sendMessage(ChatColor.RED + "Player not found, they may be offline");
                } else {
                    targetPlayer.setFoodLevel(20);
                    targetPlayer.sendMessage(ChatColor.YELLOW + "You have been fed by " + p.getDisplayName());
                }
            }
        }

        return true;
    }

}
