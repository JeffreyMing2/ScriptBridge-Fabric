# ScriptBridge-Fabric API Documentation

[中文文档](API_zh-CN.md)

ScriptBridge-Fabric provides a powerful JavaScript environment for Minecraft.
It exposes a global `game` object for easy interaction and supports **Native Java Access** with automatic mapping resolution.

---

## 1. Helper API (`game` Object)

The `game` object is your main entry point. Its methods vary slightly between Client and Server.

### Global Methods
*Available in both Client and Server.*

#### `game.setDebugMode(boolean debug)`
Enables or disables debug mode.
- **debug**: `true` to enable.
- **Effect**: Logs are broadcasted to chat/console.

---

### Client Helper API (`scripts/client/`)

#### Wrappers
The client API provides safe wrappers around Minecraft internal objects to make scripting easier and more stable.

#### `game.getPlayer()` -> `ScriptPlayer`
Returns a wrapper for the local player.
*   **`player.getName()`**: Get player name.
*   **`player.getHealth()` / `player.getMaxHealth()`**: Get health info.
*   **`player.getFoodLevel()`**: Get hunger level.
*   **`player.getPos()`**: Returns `{x, y, z}` map.
*   **`player.getX()`, `player.getY()`, `player.getZ()`**: Get coordinates.
*   **`player.getYaw()`, `player.getPitch()`**: Get rotation.
*   **`player.isOnGround()`**: Check if on ground.
*   **`player.chat(message)`**: Send a chat message.
*   **`player.jump()`**: Make the player jump.
*   **`player.getInventory()`**: Returns `ScriptInventory`.
*   **`player.getMainHandItem()`**: Returns `ScriptItem` in main hand.
*   **`player.getOffHandItem()`**: Returns `ScriptItem` in off hand.

#### `game.getWorld()` -> `ScriptWorld`
Returns a wrapper for the client world.
*   **`world.getTime()`**: Get total world time.
*   **`world.getTimeOfDay()`**: Get current day time (0-24000).
*   **`world.isRaining()`**: Check if raining.
*   **`world.isThundering()`**: Check if thundering.
*   **`world.getDimension()`**: Get dimension ID (e.g., `minecraft:overworld`).
*   **`world.getBlock(x, y, z)`**: Returns `ScriptBlock`.

#### `ScriptBlock`
*   **`block.getId()`**: Get block ID (e.g., `minecraft:stone`).
*   **`block.getName()`**: Get localized name.
*   **`block.isSolid()`**: Check if solid.
*   **`block.isAir()`**: Check if air.
*   **`block.getX()`, `block.getY()`, `block.getZ()`**: Get coordinates.

#### `ScriptInventory`
*   **`inv.getSize()`**: Get total slots.
*   **`inv.getStack(slot)`**: Get `ScriptItem` in slot.
*   **`inv.getAll()`**: Get all items as a list.

#### `ScriptItem`
*   **`item.getId()`**: Get item ID.
*   **`item.getName()`**: Get item name.
*   **`item.getCount()`**: Get stack size.
*   **`item.isEmpty()`**: Check if empty.
*   **`item.getDamage()` / `item.getMaxDamage()`**: Get durability info.

#### Other Client Methods
*   **`game.log(message)`**: Log to client output.
*   **`game.chat(message)`**: Show message in local chat (client-only).
*   **`game.sendChatMessage(message)`**: Send chat/command to server.
*   **`game.setClipboard(text)`**: Set system clipboard content.
*   **`game.getClipboard()`**: Get text from system clipboard.

---

### Server Helper API (`scripts/server/`)

*   **`game.log(message)`**: Log to server console.
*   **`game.broadcast(message)`**: Broadcast to all players.
*   **`game.modifyPlayer(name, attribute, value)`**: Modify player stats (`health`, `food`).
*   **`game.spawnBlock(x, y, z, blockId)`**: Set block in Overworld.

---

## 2. Native Java Access (Smart & Safe)

ScriptBridge uses **GraalVM** to allow access to Minecraft internal classes.

### Automatic Remapping
You don't need to worry about obfuscated names! The engine automatically resolves standard class names to their runtime intermediary names.

```javascript
// This works in both Dev and Production environments!
const MinecraftClient = Java.type('net.minecraft.client.MinecraftClient');
```

### Security Restrictions
For security reasons, access to the following is **BLOCKED**:
*   `java.lang.Runtime`
*   `java.lang.Process`
*   `java.lang.System` (partial)
*   `java.io.*` (File I/O is restricted)

### Example: Client Native Access
```javascript
const MinecraftClient = Java.type('net.minecraft.client.MinecraftClient');
const client = MinecraftClient.getInstance();

if (client.player != null) {
    client.player.jump();
}
```
