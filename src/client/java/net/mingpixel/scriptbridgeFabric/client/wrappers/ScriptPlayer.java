package net.mingpixel.scriptbridgeFabric.client.wrappers;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import java.util.HashMap;
import java.util.Map;

public class ScriptPlayer {
    private final ClientPlayerEntity player;

    public ScriptPlayer(ClientPlayerEntity player) {
        this.player = player;
    }

    public boolean exists() {
        return player != null;
    }

    public ScriptInventory getInventory() {
        if (player == null) return null;
        return new ScriptInventory(player.getInventory());
    }

    public ScriptItem getMainHandItem() {
        return player == null ? new ScriptItem(ItemStack.EMPTY) : new ScriptItem(player.getMainHandStack());
    }

    public ScriptItem getOffHandItem() {
        return player == null ? new ScriptItem(ItemStack.EMPTY) : new ScriptItem(player.getOffHandStack());
    }

    public double getX() { return player == null ? 0 : player.getX(); }
    public double getY() { return player == null ? 0 : player.getY(); }
    public double getZ() { return player == null ? 0 : player.getZ(); }

    public Map<String, Double> getPos() {
        Map<String, Double> pos = new HashMap<>();
        if (player != null) {
            pos.put("x", player.getX());
            pos.put("y", player.getY());
            pos.put("z", player.getZ());
        }
        return pos;
    }

    public float getYaw() { return player == null ? 0 : player.getYaw(); }
    public float getPitch() { return player == null ? 0 : player.getPitch(); }

    public float getHealth() { return player == null ? 0 : player.getHealth(); }
    public float getMaxHealth() { return player == null ? 0 : player.getMaxHealth(); }
    public int getFoodLevel() { return player == null ? 0 : player.getHungerManager().getFoodLevel(); }

    public String getName() { return player == null ? "" : player.getName().getString(); }
    
    public boolean isOnGround() { return player != null && player.isOnGround(); }
    
    public void chat(String msg) {
        if (player != null) player.networkHandler.sendChatMessage(msg);
    }
    
    public void jump() {
        if (player != null && player.isOnGround()) player.jump();
    }
}
