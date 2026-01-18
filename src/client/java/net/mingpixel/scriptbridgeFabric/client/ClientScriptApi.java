package net.mingpixel.scriptbridgeFabric.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mingpixel.scriptbridgeFabric.scripting.IScriptApi;
import net.mingpixel.scriptbridgeFabric.client.wrappers.ScriptPlayer;
import net.mingpixel.scriptbridgeFabric.client.wrappers.ScriptWorld;

public class ClientScriptApi implements IScriptApi {
    private final MinecraftClient client;
    private static final Logger LOGGER = LoggerFactory.getLogger("ScriptBridge-ClientAPI");
    private boolean debugMode = false;

    public ClientScriptApi(MinecraftClient client) {
        this.client = client;
    }

    public void setDebugMode(boolean debug) {
        this.debugMode = debug;
    }

    public void log(String message) {
        LOGGER.info("[JS-Client] " + message);
        if (debugMode && client.player != null) {
            client.player.sendMessage(Text.literal("§7[JS-Client-Debug] " + message), false);
        }
    }

    public void chat(String message) {
        if (client.player != null) {
            client.player.sendMessage(Text.of("[ScriptBridge] " + message), false);
        }
    }
    
    public void sendChatMessage(String message) {
        if (client.player != null) {
             client.player.networkHandler.sendChatMessage(message);
        }
    }

    public String getPlayerName() {
        if (client.player != null) {
            return client.player.getName().getString();
        }
        return "Unknown";
    }

    // --- Wrapper Accessors ---

    public ScriptPlayer getPlayer() {
        return new ScriptPlayer(client.player);
    }

    public ScriptWorld getWorld() {
        return new ScriptWorld(client.world);
    }

    /**
     * Exposes the raw MinecraftClient instance to scripts.
     * Returns Object to avoid class loading issues in GraalJS if the class is remapped.
     * @deprecated Use specific API methods instead to avoid remapping issues.
     */
    @Deprecated
    public Object getMinecraft() {
        return client;
    }

    public void setClipboard(String text) {
        try {
            if (client != null && client.keyboard != null) {
                client.keyboard.setClipboard(text);
            }
        } catch (Throwable t) {
            LOGGER.error("Failed to set clipboard", t);
            if (client.player != null) {
                client.player.sendMessage(Text.literal("§c[ScriptBridge] 无法设置剪贴板: " + t.getMessage()), false);
            }
        }
    }

    public String getClipboard() {
        try {
            if (client != null && client.keyboard != null) {
                return client.keyboard.getClipboard();
            }
        } catch (Throwable t) {
            LOGGER.error("Failed to get clipboard", t);
            if (client.player != null) {
                client.player.sendMessage(Text.literal("§c[ScriptBridge] 无法读取剪贴板: " + t.getMessage()), false);
            }
        }
        return "";
    }
}
