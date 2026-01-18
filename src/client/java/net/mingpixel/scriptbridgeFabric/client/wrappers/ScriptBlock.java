package net.mingpixel.scriptbridgeFabric.client.wrappers;

import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.HashMap;
import java.util.Map;

public class ScriptBlock {
    private final BlockState state;
    private final BlockPos pos;
    private final BlockView world;

    public ScriptBlock(BlockState state, BlockPos pos, BlockView world) {
        this.state = state;
        this.pos = pos;
        this.world = world;
    }

    public String getId() {
        if (state == null) return "minecraft:air";
        return Registries.BLOCK.getId(state.getBlock()).toString();
    }

    public String getName() {
        if (state == null) return "";
        return state.getBlock().getName().getString();
    }

    public boolean isAir() {
        return state != null && state.isAir();
    }

    public boolean isSolid() {
        return state != null && world != null && state.isSolidBlock(world, pos);
    }
    
    public int getX() { return pos.getX(); }
    public int getY() { return pos.getY(); }
    public int getZ() { return pos.getZ(); }
    
    public Map<String, Integer> getPos() {
        Map<String, Integer> map = new HashMap<>();
        map.put("x", pos.getX());
        map.put("y", pos.getY());
        map.put("z", pos.getZ());
        return map;
    }

    @Override
    public String toString() {
        return getId();
    }
}
