package org.dutch.backpacks;

import org.bukkit.plugin.java.JavaPlugin;
import org.dutch.backpacks.utils.BackpackUtils;
import org.dutch.backpacks.commands.CommandManager;
import org.dutch.backpacks.listeners.BackpackListener;

public final class BackpacksPlugin extends JavaPlugin {

    private BackpackUtils utils;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        int defaultSize = getConfig().getInt("default-size", 9);
        int upgradeCost = getConfig().getInt("upgrade-cost", 5);
        int maxSize = getConfig().getInt("max-size", 54);

        this.utils = new BackpackUtils(this, defaultSize, upgradeCost, maxSize);
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
