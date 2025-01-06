package org.dutch.backpacks.inventories;

import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.Inventory;

public class BackpackHolder implements InventoryHolder{

    private final Inventory inventory;

    public BackpackHolder(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

}
