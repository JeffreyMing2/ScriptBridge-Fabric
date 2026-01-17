package net.mingpixel.scriptbridgeFabric.client;

import com.mojang.brigadier.arguments.StringArgumentType;
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
                try {
                    scriptManager = new ScriptManager(runDir.toPath().resolve("scripts/client"), new ClientScriptApi(client));
                    scriptManager.loadScripts();
                } catch (Throwable e) {
                    // Log error but don't crash client
                    // Use a logger or print stack trace
                    e.printStackTrace();
                    client.player.sendMessage(Text.literal("§c[ScriptBridge] 初始化失败，请检查日志").formatted(Formatting.RED), false);
                }
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
                // Client-side commands namespace
                .then(ClientCommandManager.literal("client")
                    .then(ClientCommandManager.literal("run")
                        .then(ClientCommandManager.argument("filename", StringArgumentType.string())
                            .suggests((context, builder) -> {
                                if (scriptManager != null) {
                                    for (String script : scriptManager.getScriptList()) {
                                        builder.suggest(script);
                                    }
                                }
                                return builder.buildFuture();
                            })
                            .executes(context -> {
                                String filename = StringArgumentType.getString(context, "filename");
                                if (scriptManager != null) {
                                    context.getSource().sendFeedback(Text.literal("正在执行客户端脚本: " + filename).formatted(Formatting.YELLOW));
                                    scriptManager.executeScript(filename);
                                    context.getSource().sendFeedback(Text.literal("客户端脚本执行完成。").formatted(Formatting.GREEN));
                                } else {
                                    context.getSource().sendError(Text.literal("脚本管理器未初始化！"));
                                }
                                return 1;
                            })
                        )
                    )
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
                )
                // Server-side passthrough namespace
                .then(ClientCommandManager.literal("server")
                    .then(ClientCommandManager.argument("args", StringArgumentType.greedyString())
                        .executes(context -> {
                            String args = StringArgumentType.getString(context, "args");
                            // Send command to server (without slash)
                            // "script <args>"
                            if (context.getSource().getClient().getNetworkHandler() != null) {
                                context.getSource().getClient().getNetworkHandler().sendChatCommand("script " + args);
                            } else {
                                context.getSource().sendError(Text.literal("未连接到服务器！"));
                            }
                            return 1;
                        })
                    )
                )
                // Shortcut aliases for convenience (Default to Client)
                .then(ClientCommandManager.literal("run")
                    .then(ClientCommandManager.argument("filename", StringArgumentType.string())
                        .suggests((context, builder) -> {
                            if (scriptManager != null) {
                                for (String script : scriptManager.getScriptList()) {
                                    builder.suggest(script);
                                }
                            }
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            // Alias to client run
                            String filename = StringArgumentType.getString(context, "filename");
                            if (scriptManager != null) {
                                context.getSource().sendFeedback(Text.literal("正在执行客户端脚本: " + filename).formatted(Formatting.YELLOW));
                                scriptManager.executeScript(filename);
                                context.getSource().sendFeedback(Text.literal("客户端脚本执行完成。").formatted(Formatting.GREEN));
                            } else {
                                context.getSource().sendError(Text.literal("脚本管理器未初始化！"));
                            }
                            return 1;
                        })
                    )
                )
                .then(ClientCommandManager.literal("list")
                    .executes(context -> {
                        // Alias to client list
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
                .then(ClientCommandManager.literal("openfile")
                     .executes(context -> {
                        File scriptsDir = new File(runDir, "scripts");
                        if (!scriptsDir.exists()) scriptsDir.mkdirs();
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
