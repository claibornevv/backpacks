package org.dutch.backpacks.commands.backpack.subcommands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.dutch.backpacks.BackpacksPlugin;
import org.dutch.backpacks.commands.SubCommand;

import java.util.UUID;

public class BackpackUpgrade extends SubCommand {
    private final BackpacksPlugin plugin;

    public BackpackUpgrade(BackpacksPlugin plugin) { this.plugin = plugin; }

    @Override
    public String getName() {
        return "upgrade";
    }

    @Override
    public String getDescription() {
        return "Using one diamond, you can upgrade to the next tier of your backpack";
    }

    @Override
    public String getSyntax() {
        return "/backpack upgrade";
    }

    @Override
    public void perform(Player player, String[] args) {
        UUID playerUUID = player.getUniqueId();
        int playerBackpackSize = plugin.getPlayerBackpackSize(playerUUID);

        if (playerBackpackSize == 54) {
            player.sendMessage("Your backpack is already at maximum size");
            return;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack itemInHand = inventory.getItemInMainHand();

        if (itemInHand == null || itemInHand.getType() != Material.DIAMOND || itemInHand.getAmount() < 1) {
            player.sendMessage("You must hold at least one diamond in your hand to upgrade your backpack");
            return;
        }

        // Update the item in hand
        int newAmount = itemInHand.getAmount() - 1;
        if (newAmount > 0) {
            itemInHand.setAmount(newAmount);
        } else {
            inventory.setItemInMainHand(null);
        }


        // Update the backpack
        int newBackpackSize = plugin.getPlayerBackpackSize(player.getUniqueId()) + 9;
        plugin.setPlayerBackpackSize(player, newBackpackSize);

        player.sendMessage("Your backpack has been upgraded to a size of: " + newBackpackSize);
    }
}
