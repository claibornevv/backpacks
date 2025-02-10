package org.dutch.backpacks;

import org.bukkit.plugin.java.JavaPlugin;
import org.dutch.backpacks.utils.BackpackUtils;
import org.dutch.backpacks.commands.CommandManager;
import org.dutch.backpacks.listeners.BackpackListener;

public final class BackpacksPlugin extends JavaPlugin {

    private BackpackUtils utils;

    @Override
    public void onEnable() {
        this.utils = new BackpackUtils(this);
        getCommand("backpack").setExecutor(new CommandManager(this));
        getServer().getPluginManager().registerEvents(new BackpackListener(this), this);
        getLogger().info("Backpack Plugin has been Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Backpack Plugin has been disabled!");
    }

    public BackpackUtils getUtils() {
        return utils;
    }
}
