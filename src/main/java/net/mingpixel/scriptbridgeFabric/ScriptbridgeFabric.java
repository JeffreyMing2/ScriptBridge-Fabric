package net.mingpixel.scriptbridgeFabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.mingpixel.scriptbridgeFabric.scripting.ScriptManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptbridgeFabric implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ScriptBridge");
    private ScriptManager scriptManager;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            LOGGER.info("Initializing ScriptBridge...");
            scriptManager = new ScriptManager(server, server.getRunDirectory());
            scriptManager.loadScripts();
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (scriptManager != null) {
                scriptManager.close();
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            handler.player.sendMessage(
                Text.literal("[ScriptBridge] ").formatted(Formatting.GOLD)
                    .append(Text.literal("服务端模组加载成功！脚本系统已就绪。").formatted(Formatting.GREEN)),
                false
            );
        });
    }
}
