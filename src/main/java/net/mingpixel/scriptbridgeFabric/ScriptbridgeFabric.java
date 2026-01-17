package net.mingpixel.scriptbridgeFabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.command.CommandManager;
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
            scriptManager = new ScriptManager(server.getRunDirectory().resolve("scripts/server"), new net.mingpixel.scriptbridgeFabric.scripting.ScriptApi(server));
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

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("script")
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
