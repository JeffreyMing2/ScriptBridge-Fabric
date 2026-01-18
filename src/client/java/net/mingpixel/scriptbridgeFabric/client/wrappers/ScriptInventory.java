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

    public ScriptItem getMainHandStack() {
        return new ScriptItem(inventory.getMainHandStack());
    }

    public ScriptItem getOffHandStack() {
        return new ScriptItem(inventory.getOffHandStack());
    }

    public ScriptItem getStack(int slot) {
        if (slot < 0 || slot >= inventory.size()) return new ScriptItem(ItemStack.EMPTY);
        return new ScriptItem(inventory.getStack(slot));
    }

    public ScriptItem getArmorStack(int slot) {
        if (slot < 0 || slot >= inventory.armor.size()) return new ScriptItem(ItemStack.EMPTY);
        return new ScriptItem(inventory.armor.get(slot));
    }

    public int getSelectedSlot() {
        return inventory.selectedSlot;
    }

    public void setSelectedSlot(int slot) {
        if (slot >= 0 && slot < 9) {
            inventory.selectedSlot = slot;
        }
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
}
