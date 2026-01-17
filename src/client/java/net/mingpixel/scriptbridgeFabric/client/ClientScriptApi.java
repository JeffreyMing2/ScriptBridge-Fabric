package net.mingpixel.scriptbridgeFabric.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.mingpixel.scriptbridgeFabric.scripting.IScriptApi;

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

    /**
     * Exposes the raw MinecraftClient instance to scripts.
     * This allows scripts to access client-side features without needing manual reflection/mappings lookup.
     */
    public MinecraftClient getMinecraft() {
        return client;
    }

    public void setClipboard(String text) {
        if (client != null && client.keyboard != null) {
            client.keyboard.setClipboard(text);
        }
    }

    public String getClipboard() {
        if (client != null && client.keyboard != null) {
            return client.keyboard.getClipboard();
        }
        return "";
    }
}
