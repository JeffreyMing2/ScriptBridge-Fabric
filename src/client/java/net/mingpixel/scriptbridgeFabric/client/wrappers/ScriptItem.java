package net.mingpixel.scriptbridgeFabric.client.wrappers;

import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

public class ScriptItem {
    private final ItemStack stack;

    public ScriptItem(ItemStack stack) {
        this.stack = stack;
    }

    public boolean isEmpty() {
        return stack == null || stack.isEmpty();
    }

    public String getId() {
        if (isEmpty()) return "minecraft:air";
        return Registries.ITEM.getId(stack.getItem()).toString();
    }

    public String getName() {
        if (isEmpty()) return "";
        return stack.getName().getString();
    }

    public int getCount() {
        return isEmpty() ? 0 : stack.getCount();
    }

    public int getMaxCount() {
        return isEmpty() ? 0 : stack.getMaxCount();
    }

    public boolean isDamageable() {
        return !isEmpty() && stack.isDamageable();
    }

    public int getDamage() {
        return isEmpty() ? 0 : stack.getDamage();
    }

    public int getMaxDamage() {
        return isEmpty() ? 0 : stack.getMaxDamage();
    }
    
    // Helper to allow simple checks like if (item.id == "minecraft:diamond")
    public String toString() {
        return getId();
    }
}
