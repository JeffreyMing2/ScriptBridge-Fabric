# ScriptBridge-Fabric API 文档

ScriptBridge-Fabric 向所有 JavaScript 脚本暴露了一个名为 `game` 的全局对象。根据脚本是在 **客户端 (Client)** 还是 **服务端 (Server)** 运行，该对象提供的方法会有所不同。

## 全局方法 (客户端和服务端均可用)

### `game.setDebugMode(boolean debug)`
启用或禁用当前脚本引擎的调试模式。
- **debug**: `true` 启用，`false` 禁用。
- **效果**: 启用后，`game.log()` 的消息也会显示在游戏内聊天栏（客户端）或广播给管理员（服务端）。

---

## 服务端 API (`scripts/server/`)

这些方法仅在脚本运行于服务端环境时可用。

### `game.log(String message)`
记录一条消息到服务端控制台。
- **message**: 要记录的字符串。
- **注意**: 如果开启了调试模式，该消息也会广播给玩家。

### `game.broadcast(String message)`
向服务器上的所有玩家广播一条消息。
- **message**: 消息内容。
- **前缀**: 消息将带有 `[ScriptBridge]` 前缀。

### `game.modifyPlayer(String playerName, String attribute, double value)`
修改玩家的特定属性。
- **playerName**: 目标玩家的名称。
- **attribute**: 要修改的属性。支持的值：
  - `"health"`: 设置玩家的生命值。
  - `"food"`: 设置玩家的饱食度。
- **value**: 该属性的新值。

### `game.spawnBlock(int x, int y, int z, String blockId)`
在主世界的指定坐标设置一个方块。
- **x, y, z**: 坐标。
- **blockId**: 方块的资源标识符 (例如 `"minecraft:stone"`, `"minecraft:diamond_block"`)。

---

## 客户端 API (`scripts/client/`)

这些方法仅在脚本运行于客户端环境时可用。

### `game.log(String message)`
记录一条消息到客户端日志 (游戏输出)。
- **message**: 要记录的字符串。
- **注意**: 如果开启了调试模式，该消息也会显示在本地聊天栏中。

### `game.chat(String message)`
在玩家的本地聊天栏中显示一条消息 (仅玩家自己可见)。
- **message**: 消息内容。
- **前缀**: 消息将带有 `[ScriptBridge]` 前缀。

### `game.sendChatMessage(String message)`
模拟玩家输入并发送聊天消息或命令到服务器。
- **message**: 要发送的文本。可以是聊天消息或命令 (以 `/` 开头)。

### `game.getPlayerName()`
获取当前客户端玩家的名称。
- **返回**: `String` - 玩家名称。

---

## 示例

### 客户端脚本 (`scripts/client/hello.js`)
```javascript
// 在本地聊天栏说你好
game.chat("你好 " + game.getPlayerName() + "!");

// 发送消息给服务器（所有人可见）
game.sendChatMessage("大家好！");

// 记录到控制台
game.log("客户端脚本已执行。");
```

### 服务端脚本 (`scripts/server/admin.js`)
```javascript
// 广播给所有玩家
game.broadcast("服务器将在 5 分钟后维护！");

// 治疗名为 'Steve' 的玩家
game.modifyPlayer("Steve", "health", 20.0);

// 放置一个方块
game.spawnBlock(0, 100, 0, "minecraft:gold_block");
```
