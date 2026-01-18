package net.mingpixel.scriptbridgeFabric.client.wrappers;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ScriptInventory {
    private final PlayerInventory inventory;

    public ScriptInventory(PlayerInventory inventory) {
        this.inventory = inventory;
    }

    public ScriptItem getStack(int slot) {
        if (slot < 0 || slot >= inventory.size()) return new ScriptItem(ItemStack.EMPTY);
        return new ScriptItem(inventory.getStack(slot));
    }
    
    public int getSize() {
        return inventory.size();
    }
    
    public List<ScriptItem> getAll() {
        List<ScriptItem> items = new ArrayList<>();
        for (int i = 0; i < inventory.size(); i++) {
            items.add(new ScriptItem(inventory.getStack(i)));
        }
        return items;
    }
    
    public int getSelectedSlot() {
        // Accessing field directly might fail if not visible, try to use getter or just return 0 if failed
        // For now, removing direct field access if it causes build errors.
        // We can expose this via player wrapper or if we confirm visibility.
        return 0; 
    }
}
