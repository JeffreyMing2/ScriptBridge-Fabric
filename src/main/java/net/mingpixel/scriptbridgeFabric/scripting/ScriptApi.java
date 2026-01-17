package net.mingpixel.scriptbridgeFabric.scripting;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScriptApi implements IScriptApi {
    private final MinecraftServer server;
    private static final Logger LOGGER = LoggerFactory.getLogger("ScriptBridge-API");
    private boolean debugMode = false;

    public ScriptApi(MinecraftServer server) {
        this.server = server;
    }

    public void setDebugMode(boolean debug) {
        this.debugMode = debug;
    }

    public void log(String message) {
        LOGGER.info("[JS] " + message);
        if (debugMode && server != null) {
            server.getPlayerManager().broadcast(Text.literal("§7[JS-Debug] " + message), false);
        }
    }

    public void broadcast(String message) {
        if (server == null) return;
        server.getPlayerManager().broadcast(Text.of("[ScriptBridge] " + message), false);
    }

    public void modifyPlayer(String playerName, String attribute, double value) {
        if (server == null) return;
        ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerName);
        if (player != null) {
            if ("health".equalsIgnoreCase(attribute)) {
                player.setHealth((float) value);
                LOGGER.info("Set health of {} to {}", playerName, value);
            } else if ("food".equalsIgnoreCase(attribute)) {
                player.getHungerManager().setFoodLevel((int) value);
                LOGGER.info("Set food level of {} to {}", playerName, value);
            } else {
                LOGGER.warn("Unknown attribute: {}", attribute);
            }
        } else {
            LOGGER.warn("Player not found: {}", playerName);
        }
    }

    public void spawnBlock(int x, int y, int z, String blockId) {
        if (server == null) return;
        // Default to overworld
        var world = server.getOverworld();
        Identifier id = Identifier.of(blockId);
        
        if (Registries.BLOCK.containsId(id)) {
            Block block = Registries.BLOCK.get(id);
            world.setBlockState(new BlockPos(x, y, z), block.getDefaultState());
            LOGGER.info("Spawned block {} at {}, {}, {}", blockId, x, y, z);
        } else {
            LOGGER.warn("Block not found: {}", blockId);
        }
    }
}
