package org.dutch.backpacks.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.dutch.backpacks.BackpacksPlugin;

import java.util.*;

public class CommandManager implements TabExecutor {

    private final Map<String, BackpackCommand> subCommands = new HashMap<>();
    private final BackpacksPlugin plugin;

    public CommandManager(BackpacksPlugin plugin) {
        this.plugin = plugin;
        registerCommands();
    }

    private void registerCommands() {
        subCommands.put("help", new HelpCommand());
        subCommands.put("give", new GiveCommand(plugin));
        //subCommands.put("open", new OpenCommand(plugin));
        subCommands.put("upgrade", new UpgradeCommand(plugin));
        subCommands.put("reset", new ResetCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args.length == 0) {
            plugin.getUtils().openBackpack(player);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        BackpackCommand command = subCommands.get(subCommand);

        if (command != null) {
            command.execute(player, Arrays.copyOfRange(args, 1, args.length));
        } else {
            player.sendMessage("§cUnknown subcommand! Use /bp help");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(subCommands.keySet());
        } else if (args.length > 1) {
            BackpackCommand command = subCommands.get(args[0].toLowerCase());
            if (command != null) {
                return command.onTabComplete((Player) sender, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        return completions;
    }
}
