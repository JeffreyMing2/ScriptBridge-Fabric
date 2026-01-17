package net.mingpixel.scriptbridgeFabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.mingpixel.scriptbridgeFabric.scripting.ScriptManager;
import java.io.File;
import java.util.List;

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
                String version = FabricLoader.getInstance()
                    .getModContainer("scriptbridge-fabric")
                    .map(c -> c.getMetadata().getVersion().getFriendlyString())
                    .orElse("Unknown");

                client.player.sendMessage(
                    Text.literal("[ScriptBridge] ").formatted(Formatting.AQUA)
                        .append(Text.literal("客户端模组加载成功！\n版本: " + version + "\n输入 /script openfile 查看脚本目录").formatted(Formatting.WHITE)),
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
                .then(ClientCommandManager.literal("list")
                    .executes(context -> {
                        if (scriptManager != null) {
                            List<String> scripts = scriptManager.getScriptList();
                            if (scripts.isEmpty()) {
                                context.getSource().sendFeedback(Text.literal("没有找到客户端脚本。").formatted(Formatting.YELLOW));
                            } else {
                                context.getSource().sendFeedback(Text.literal("客户端脚本列表:").formatted(Formatting.GOLD));
                                for (String script : scripts) {
                                    context.getSource().sendFeedback(Text.literal("- " + script).formatted(Formatting.WHITE));
                                }
                            }
                        }
                        return 1;
                    })
                )
                .then(ClientCommandManager.literal("debug")
                    .executes(context -> {
                        if (scriptManager != null) {
                            boolean debug = scriptManager.toggleDebugMode();
                            context.getSource().sendFeedback(Text.literal("客户端调试模式已" + (debug ? "开启" : "关闭")).formatted(debug ? Formatting.GREEN : Formatting.RED));
                        }
                        return 1;
                    })
                )
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
