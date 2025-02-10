package org.dutch.backpacks.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.dutch.backpacks.BackpacksPlugin;
import org.bukkit.plugin.Plugin;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class BackpackUtils {

    private final Plugin plugin;
    private final NamespacedKey backpackKey;
    private final NamespacedKey sizeKey;

    public BackpackUtils(BackpacksPlugin plugin) {
        this.plugin = plugin;
        this.backpackKey = new NamespacedKey(plugin, "backpack");
        this.sizeKey = new NamespacedKey(plugin, "backpack-size");
    }

    public ItemStack createBackpackItem() {
        ItemStack backpack = new ItemStack(Material.CHEST);
        ItemMeta meta = backpack.getItemMeta();
        meta.setDisplayName("Backpack");
        meta.getPersistentDataContainer().set(backpackKey, PersistentDataType.BYTE, (byte) 1);
        backpack.setItemMeta(meta);
        return backpack;
    }

    public void openBackpack(Player player) {
        Inventory backpack = getBackpackInventory(player);
        player.openInventory(backpack);
    }

    public boolean isBackpackItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer().has(backpackKey, PersistentDataType.BYTE);
    }

    // TODO: add serialization/deserialization methods here
    private Inventory getBackpackInventory(Player player) {
        int size = getBackpackSize(player);
        String inventoryData = player.getPersistentDataContainer().get(backpackKey, PersistentDataType.STRING);

        Inventory backpack = Bukkit.createInventory(player, size, "Backpack");

        if (inventoryData != null) {
            try {
                byte[] decodedBytes = Base64.getDecoder().decode(inventoryData);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

                int itemCount = dataInput.readInt();

                for (int i = 0; i < itemCount; i++) {
                    int slot = dataInput.readInt();
                    ItemStack item = (ItemStack) dataInput.readObject();
                    backpack.setItem(slot, item);
                }

                dataInput.close();
            } catch (IOException | ClassNotFoundException e) {
                plugin.getLogger().warning("Failed to deserialize backpack data for player: " + player.getName());
                e.printStackTrace();
            }
        }

        return backpack;
    }

    public void saveBackpack(Player player, Inventory backpack) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            ItemStack[] contents = backpack.getContents();
            int itemCount = 0;
            for (int i = 0; i < contents.length; i++) {
                if (contents[i] != null && contents[i].getType() != Material.AIR) {
                    itemCount++;
                }
            }

            dataOutput.writeInt(itemCount);

            for (int i = 0; i < contents.length; i++) {
                ItemStack item = contents[i];
                if (item != null && item.getType() != Material.AIR) {
                    dataOutput.writeInt(i);
                    dataOutput.writeObject(item);
                }
            }

            dataOutput.close();

            String encodedData = Base64.getEncoder().encodeToString(outputStream.toByteArray());

            player.getPersistentDataContainer().set(backpackKey, PersistentDataType.STRING, encodedData);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to serialize backpack data for player: " + player.getName());
            e.printStackTrace();
        }
    }

    public int getBackpackSize(Player player) {
        return player.getPersistentDataContainer().getOrDefault(sizeKey, PersistentDataType.INTEGER, 9);
    }

    public void setBackpackSize(Player player, int size) {
        player.getPersistentDataContainer().set(sizeKey, PersistentDataType.INTEGER, size);
    }

    public boolean upgradeBackpack(Player player) {
        int currentSize = getBackpackSize(player);
        int newSize = currentSize + 9;

        if (newSize > 54) {
            player.sendMessage("§cYour backpack is already at maximum size!");
            return false;
        }

        if (player.getLevel() < 5) {
            player.sendMessage("§cYou need at least 5 experience levels to upgrade!");
            return false;
        }

        player.giveExpLevels(-5);
        setBackpackSize(player, newSize);
        return true;
    }

}
