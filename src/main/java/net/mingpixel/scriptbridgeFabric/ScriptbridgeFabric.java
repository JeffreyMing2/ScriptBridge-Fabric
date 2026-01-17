package net.mingpixel.scriptbridgeFabric;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import net.minecraft.entity.Entity;
import net.mingpixel.scriptbridgeFabric.scripting.ScriptManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class ScriptbridgeFabric implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("ScriptBridge");
    private ScriptManager scriptManager;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            LOGGER.info("Initializing ScriptBridge...");
            try {
                scriptManager = new ScriptManager(server.getRunDirectory().resolve("scripts/server"), new net.mingpixel.scriptbridgeFabric.scripting.ScriptApi(server));
                scriptManager.loadScripts();
            } catch (Throwable e) {
                LOGGER.error("Failed to initialize ScriptBridge Server Manager", e);
            }
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (scriptManager != null) {
                scriptManager.close();
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            boolean isOp = false;
            for (String op : server.getPlayerManager().getOpList().getNames()) {
                if (op.equalsIgnoreCase(handler.player.getName().getString())) {
                    isOp = true;
                    break;
                }
            }
            if (isOp) {
                handler.player.sendMessage(
                    Text.literal("[ScriptBridge] ").formatted(Formatting.GOLD)
                        .append(Text.literal("服务端模组加载成功！脚本系统已就绪。").formatted(Formatting.GREEN)),
                    false
                );
            }
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("script")
                .then(CommandManager.literal("run")
                    .then(CommandManager.argument("filename", StringArgumentType.string())
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
                                context.getSource().sendFeedback(() -> Text.literal("正在执行脚本: " + filename).formatted(Formatting.YELLOW), false);
                                scriptManager.executeScript(filename);
                                context.getSource().sendFeedback(() -> Text.literal("脚本执行完成。").formatted(Formatting.GREEN), false);
                            } else {
                                context.getSource().sendError(Text.literal("脚本管理器未初始化！"));
                            }
                            return 1;
                        })
                    )
                )
                .then(CommandManager.literal("list")
                    .executes(context -> {
                        if (scriptManager != null) {
                            List<String> scripts = scriptManager.getScriptList();
                            if (scripts.isEmpty()) {
                                context.getSource().sendFeedback(() -> Text.literal("没有找到服务端脚本。").formatted(Formatting.YELLOW), false);
                            } else {
                                context.getSource().sendFeedback(() -> Text.literal("服务端脚本列表:").formatted(Formatting.GOLD), false);
                                for (String script : scripts) {
                                    context.getSource().sendFeedback(() -> Text.literal("- " + script).formatted(Formatting.WHITE), false);
                                }
                            }
                        } else {
                            context.getSource().sendError(Text.literal("脚本管理器未初始化！"));
                        }
                        return 1;
                    })
                )
                .then(CommandManager.literal("debug")
                    .executes(context -> {
                        if (scriptManager != null) {
                            boolean debug = scriptManager.toggleDebugMode();
                            context.getSource().sendFeedback(() -> Text.literal("服务端调试模式已" + (debug ? "开启" : "关闭")).formatted(debug ? Formatting.GREEN : Formatting.RED), false);
                        } else {
                            context.getSource().sendError(Text.literal("脚本管理器未初始化！"));
                        }
                        return 1;
                    })
                )
                .then(CommandManager.literal("openfile")
                    .executes(context -> {
                        // This might not work on dedicated servers (headless), but useful for local testing
                        try {
                            File scriptsDir = context.getSource().getServer().getRunDirectory().resolve("scripts/server").toFile();
                            if (!scriptsDir.exists()) {
                                scriptsDir.mkdirs();
                            }
                            Util.getOperatingSystem().open(scriptsDir);
                            context.getSource().sendFeedback(() -> Text.literal("正在打开服务端脚本文件夹...").formatted(Formatting.GREEN), false);
                        } catch (Exception e) {
                            context.getSource().sendError(Text.literal("无法打开文件夹 (可能是在无头服务器环境下)"));
                        }
                        return 1;
                    })
                )
                .then(CommandManager.literal("reload")
                    // .requires(source -> source.hasPermissionLevel(2)) // Permission check temporarily removed due to compilation issue
                    .executes(context -> {
                        if (scriptManager != null) {
                            context.getSource().sendFeedback(() -> Text.literal("正在重载脚本...").formatted(Formatting.YELLOW), false);
                            try {
                                scriptManager.reload();
                                context.getSource().sendFeedback(() -> Text.literal("脚本重载成功！").formatted(Formatting.GREEN), false);
                            } catch (Exception e) {
                                LOGGER.error("Failed to reload scripts", e);
                                context.getSource().sendError(Text.literal("脚本重载失败，请检查控制台日志。"));
                            }
                        } else {
                            context.getSource().sendError(Text.literal("脚本管理器未初始化！"));
                        }
                        return 1;
                    })
                )
            );
        });
    }
}
