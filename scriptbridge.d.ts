/**
 * ScriptBridge-Fabric API Definitions
 * 
 * This file provides type definitions for the global `game` object exposed
 * in the ScriptBridge environment, as well as helpers for the native Java access.
 */

// ==========================================
// Helper API (The 'game' object)
// ==========================================

interface ScriptApi {
    /**
     * Enables or disables debug mode.
     * When enabled, log messages are broadcasted to chat/console.
     * @param debug true to enable, false to disable
     */
    setDebugMode(debug: boolean): void;
}

interface ClientScriptApi extends ScriptApi {
    /**
     * Logs a message to the client log (game output).
     * @param message The message to log
     */
    log(message: string): void;

    /**
     * Displays a message in the player's local chat (client-only).
     * @param message The message to display
     */
    chat(message: string): void;

    /**
     * Sends a chat message or command to the server.
     * @param message The text to send (e.g. "Hello" or "/gamemode creative")
     */
    sendChatMessage(message: string): void;

    /**
     * Gets the name of the current player.
     * @returns The player's name
     */
    getPlayerName(): string;
}

interface ServerScriptApi extends ScriptApi {
    /**
     * Logs a message to the server console.
     * @param message The message to log
     */
    log(message: string): void;

    /**
     * Broadcasts a message to all players on the server.
     * @param message The message to broadcast
     */
    broadcast(message: string): void;

    /**
     * Modifies a player's attribute.
     * @param playerName Name of the target player
     * @param attribute Attribute to modify ("health" or "food")
     * @param value The new value
     */
    modifyPlayer(playerName: string, attribute: "health" | "food", value: number): void;

    /**
     * Spawns a block in the Overworld.
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param blockId Block ID (e.g. "minecraft:stone")
     */
    spawnBlock(x: number, y: number, z: number, blockId: string): void;
}

/**
 * The global 'game' object.
 * Its type depends on whether the script is running on the Client or Server.
 * 
 * - In `scripts/client/`: It is `ClientScriptApi`.
 * - In `scripts/server/`: It is `ServerScriptApi`.
 */
declare const game: ClientScriptApi | ServerScriptApi;


// ==========================================
// Native Java API (GraalVM)
// ==========================================

/**
 * GraalVM Java interop object.
 * Allows accessing any Java class in the classpath.
 */
declare const Java: {
    /**
     * Loads a Java class by name.
     * @param className The fully qualified class name (e.g. "java.lang.System", "net.minecraft.client.MinecraftClient")
     * @returns The Java class object
     */
    type(className: string): any;

    /**
     * Creates a Java array from a JavaScript array.
     * @param type The Java type (e.g. "int[]", "java.lang.String[]")
     * @param values The values to populate the array
     */
    to(values: any[], type: string): any;
    
    // Add other GraalVM Java.* methods if needed
};
