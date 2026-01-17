package net.mingpixel.scriptbridgeFabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.mingpixel.scriptbridgeFabric.scripting.ScriptManager;
import java.io.File;

public class ScriptbridgeFabricClient implements ClientModInitializer {
    private ScriptManager scriptManager;

    @Override
    public void onInitializeClient() {
        File runDir = MinecraftClient.getInstance().runDirectory;

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            // Initialize and load scripts when joining a world
            if (scriptManager == null) {
                scriptManager = new ScriptManager(runDir.toPath().resolve("scripts/client"), new ClientScriptApi(client));
                scriptManager.loadScripts();
            } else {
                scriptManager.reload(); // Ensure fresh state if it wasn't null for some reason
            }

            if (client.player != null) {
                client.player.sendMessage(
                    Text.literal("[ScriptBridge] ").formatted(Formatting.AQUA)
                        .append(Text.literal("客户端模组加载成功！脚本已启用。").formatted(Formatting.WHITE)),
                    false
                );
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            // Unload scripts when leaving the world
            if (scriptManager != null) {
                scriptManager.close();
                scriptManager = null;
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("script")
                .then(ClientCommandManager.literal("openfile")
                    .executes(context -> {
                        File scriptsDir = new File(runDir, "scripts");
                        
                        if (!scriptsDir.exists()) {
                            scriptsDir.mkdirs();
                        }

                        Util.getOperatingSystem().open(scriptsDir);
                        context.getSource().sendFeedback(Text.literal("正在打开脚本文件夹...").formatted(Formatting.GREEN));
                        return 1;
                    })
                )
                .then(ClientCommandManager.literal("reload")
                    .executes(context -> {
                        if (scriptManager != null) {
                            context.getSource().sendFeedback(Text.literal("正在重载客户端脚本...").formatted(Formatting.YELLOW));
                            try {
                                scriptManager.reload();
                                context.getSource().sendFeedback(Text.literal("客户端脚本重载成功！").formatted(Formatting.GREEN));
                            } catch (Exception e) {
                                context.getSource().sendFeedback(Text.literal("客户端脚本重载失败！").formatted(Formatting.RED));
                                e.printStackTrace();
                            }
                        }
                        return 1;
                    })
                )
            );
        });
    }
}
