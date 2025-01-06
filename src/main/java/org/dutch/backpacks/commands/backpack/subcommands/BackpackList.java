package org.dutch.backpacks.commands.backpack.subcommands;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.dutch.backpacks.BackpacksPlugin;
import org.dutch.backpacks.commands.SubCommand;

import java.util.Set;

public class BackpackList extends SubCommand {
    private final BackpacksPlugin plugin;

    public BackpackList(BackpacksPlugin plugin) { this.plugin = plugin; }

    @Override
    public String getName() { return "list"; }

    @Override
    public String getDescription() {
        return "Returns how many backpacks the player has as a list";
    }

    @Override
    public String getSyntax() { return "/backpack list"; }

    @Override
    public void perform(Player player, String[] args) {
        if (args.length != 1) {
            player.sendMessage("This command does not require any arguments.");
            return;
        }

        YamlConfiguration backpackConfig = plugin.getBackpacksConfig();

        ConfigurationSection backpacksSection = backpackConfig.getConfigurationSection(player.getUniqueId().toString() + ".backpacks");

        if (backpacksSection != null) {
            Set<String> itemKeys = backpacksSection.getKeys(false);

            int numOfBackpacks = itemKeys.size();

            player.sendMessage("You have " + numOfBackpacks + " backpacks");
            plugin.getLogger().info("backpacks: " + numOfBackpacks);
        } else {
            player.sendMessage("No backpacks");
        }
    }
}
