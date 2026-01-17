package net.mingpixel.scriptbridgeFabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ScriptbridgeFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.player != null) {
                client.player.sendMessage(
                    Text.literal("[ScriptBridge] ").formatted(Formatting.AQUA)
                        .append(Text.literal("客户端模组加载成功！").formatted(Formatting.WHITE)),
                    false
                );
            }
        });
    }
}
