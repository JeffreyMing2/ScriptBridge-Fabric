# ScriptBridge-Fabric API Documentation

[中文文档](API_zh-CN.md)

ScriptBridge-Fabric exposes a global object named `game` to all JavaScript scripts. This object provides different methods depending on whether the script is running on the **Client** or the **Server**.

## Global Methods (Available in both Client and Server)

### `game.setDebugMode(boolean debug)`
Enables or disables debug mode for the current script engine.
- **debug**: `true` to enable, `false` to disable.
- **Effect**: When enabled, `game.log()` messages are also shown in the in-game chat (client-side) or broadcast to ops (server-side).

---

## Server API (`scripts/server/`)

These methods are available only when the script is running in the server environment.

### `game.log(String message)`
Logs a message to the server console.
- **message**: The string to log.
- **Note**: If debug mode is on, it broadcasts to players.

### `game.broadcast(String message)`
Broadcasts a message to all players on the server.
- **message**: The message content.
- **Prefix**: Messages are prefixed with `[ScriptBridge]`.

### `game.modifyPlayer(String playerName, String attribute, double value)`
Modifies a specific attribute of a player.
- **playerName**: The name of the target player.
- **attribute**: The attribute to modify. Supported values:
  - `"health"`: Sets the player's health.
  - `"food"`: Sets the player's food level.
- **value**: The new value for the attribute.

### `game.spawnBlock(int x, int y, int z, String blockId)`
Sets a block at the specified coordinates in the Overworld.
- **x, y, z**: The coordinates.
- **blockId**: The resource identifier of the block (e.g., `"minecraft:stone"`, `"minecraft:diamond_block"`).

---

## Client API (`scripts/client/`)

These methods are available only when the script is running in the client environment.

### `game.log(String message)`
Logs a message to the client log (game output).
- **message**: The string to log.
- **Note**: If debug mode is on, it shows in the local chat.

### `game.chat(String message)`
Displays a message in the player's local chat (only visible to the player).
- **message**: The message content.
- **Prefix**: Messages are prefixed with `[ScriptBridge]`.

### `game.sendChatMessage(String message)`
Sends a chat message or command to the server as if the player typed it.
- **message**: The text to send. Can be a chat message or a command (starting with `/`).

### `game.getPlayerName()`
Returns the name of the current client player.
- **Returns**: `String` - The player's name.

---

## Examples

### Client Script (`scripts/client/hello.js`)
```javascript
// Say hello in local chat
game.chat("Hello " + game.getPlayerName() + "!");

// Send a message to the server
game.sendChatMessage("Hello everyone!");

// Log to console
game.log("Client script executed.");
```

### Server Script (`scripts/server/admin.js`)
```javascript
// Broadcast to all players
game.broadcast("Server maintenance in 5 minutes!");

// Heal a player named 'Steve'
game.modifyPlayer("Steve", "health", 20.0);

// Place a block
game.spawnBlock(0, 100, 0, "minecraft:gold_block");
```
