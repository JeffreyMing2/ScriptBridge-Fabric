package net.mingpixel.scriptbridgeFabric.client.wrappers;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;

public class ScriptWorld {
    private final ClientWorld world;

    public ScriptWorld(ClientWorld world) {
        this.world = world;
    }

    public boolean exists() {
        return world != null;
    }

    public ScriptBlock getBlock(int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (world == null) return new ScriptBlock(null, pos, null);
        return new ScriptBlock(world.getBlockState(pos), pos, world);
    }

    public String getDimension() {
        if (world == null) return "unknown";
        return world.getRegistryKey().getValue().toString();
    }

    public long getTime() { return world == null ? 0 : world.getTime(); }
    public long getTimeOfDay() { return world == null ? 0 : world.getTimeOfDay(); }
    public boolean isRaining() { return world != null && world.isRaining(); }
    public boolean isThundering() { return world != null && world.isThundering(); }
}
