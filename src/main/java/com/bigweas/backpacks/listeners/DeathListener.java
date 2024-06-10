package com.bigweas.backpacks.listeners;

import com.bigweas.backpacks.Backpacks;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener{

    private final Backpacks backpacks;

    public DeathListener(Backpacks backpacks) {
        this.backpacks = backpacks;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {

        //backpacks.getConfig();
        e.getPlayer().sendMessage(ChatColor.RED + "HAHA U DIED");

    }

}
