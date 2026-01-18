package net.mingpixel.scriptbridgeFabric.client.wrappers;

import net.minecraft.client.world.ClientWorld;

public class ScriptWorld {
    private final ClientWorld world;

    public ScriptWorld(ClientWorld world) {
        this.world = world;
    }

    public boolean exists() {
        return world != null;
    }

    public long getTime() { return world == null ? 0 : world.getTime(); }
    public long getTimeOfDay() { return world == null ? 0 : world.getTimeOfDay(); }
    public boolean isRaining() { return world != null && world.isRaining(); }
    public boolean isThundering() { return world != null && world.isThundering(); }
}
