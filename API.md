# ScriptBridge-Fabric API Documentation

[中文文档](API_zh-CN.md)

ScriptBridge-Fabric provides two layers of API:
1. The **Helper API** (`game` object) for simplified interaction.
2. The **Native Java API** (GraalVM) for full access to Minecraft internals.

---

## 1. Helper API (`game` Object)

The `game` object provides a simplified interface for common tasks.

### Global Methods
*Available in both Client and Server.*

#### `game.setDebugMode(boolean debug)`
Enables or disables debug mode.
- **debug**: `true` to enable.
- **Effect**: Logs are broadcasted to chat/console.

---

### Server Helper API
*Available in `scripts/server/`.*

#### `game.log(String message)`
Logs to server console (and chat if debug is on).

#### `game.broadcast(String message)`
Broadcasts to all players.

#### `game.modifyPlayer(String playerName, String attribute, double value)`
Modifies player stats.
- **attribute**: `"health"`, `"food"`.

#### `game.spawnBlock(int x, int y, int z, String blockId)`
Sets a block in the Overworld.

---

### Client Helper API
*Available in `scripts/client/`.*

#### `game.log(String message)`
Logs to client output.

#### `game.chat(String message)`
Shows a message in local chat (client-only).

#### `game.sendChatMessage(String message)`
Sends a message or command to the server.

#### `game.getPlayerName()`
Returns the current player's name.

---

## 2. Advanced: Native Java Access

ScriptBridge uses **GraalVM** with `allowAllAccess(true)`, meaning you can access **any Java class** in the game or mod environment using `Java.type()`.

### Usage
```javascript
const JavaString = Java.type('java.lang.String');
const System = Java.type('java.lang.System');
```

### Client-Side Native Examples
You can access `MinecraftClient` directly.

```javascript
// scripts/client/native_test.js
const MinecraftClient = Java.type('net.minecraft.client.MinecraftClient');
const Text = Java.type('net.minecraft.text.Text');

// Get the client instance
const client = MinecraftClient.getInstance();

// Check if player exists
if (client.player != null) {
    // Send a raw packet or modify client state
    client.player.sendMessage(Text.of("§aHello from Native Java API!"), false);
    
    // Example: Set FOV (if accessible) or other options
    // client.options.fov.setValue(110.0); 
}
```

### Server-Side Native Examples
You can access `MinecraftServer` and internal logic.

```javascript
// scripts/server/native_test.js
const Items = Java.type('net.minecraft.item.Items');
const ItemStack = Java.type('net.minecraft.item.ItemStack');

// Note: Accessing server instance usually requires getting it from a context or static helper
// Since 'game' object implementation holds the server, you might need reflection or static getters if available.
// However, many Fabric classes have static registries.

// Example: Print a registry entry name
const stone = Items.STONE;
game.log("Native Item Name: " + stone.toString());
```

### Important Notes
- **Mappings**: You must use the correct class names (Intermediary or Yarn, depending on the runtime environment). In a production Fabric environment (Remapped), you might need to use Intermediary names if not running in a dev environment with Yarn mappings, OR the mod loader handles remapping. *For most end-users, this feature is experimental and depends on the runtime mappings.*
- **Reflection**: You can use Java reflection if needed.
- **Power**: This allows you to do ANYTHING a mod can do. Use with caution.
