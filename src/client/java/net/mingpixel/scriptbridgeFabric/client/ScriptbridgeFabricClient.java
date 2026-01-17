package net.mingpixel.scriptbridgeFabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import java.io.File;

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

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("script")
                .then(ClientCommandManager.literal("openfile")
                    .executes(context -> {
                        File runDir = MinecraftClient.getInstance().runDirectory;
                        File scriptsDir = new File(runDir, "scripts");
                        
                        if (!scriptsDir.exists()) {
                            scriptsDir.mkdirs();
                        }

                        Util.getOperatingSystem().open(scriptsDir);
                        context.getSource().sendFeedback(Text.literal("正在打开脚本文件夹...").formatted(Formatting.GREEN));
                        return 1;
                    })
                )
            );
        });
    }
}
