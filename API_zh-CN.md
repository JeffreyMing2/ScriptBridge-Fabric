# ScriptBridge-Fabric API 文档

ScriptBridge-Fabric 提供了两层 API：
1. **辅助 API** (`game` 对象)：用于简化常见操作。
2. **原生 Java API** (GraalVM)：用于完全访问 Minecraft 内部机制。

---

## 1. 辅助 API (`game` 对象)

`game` 对象为常见任务提供了一个简化的接口。

### 全局方法
*在客户端和服务端均可用。*

#### `game.setDebugMode(boolean debug)`
启用或禁用调试模式。
- **debug**: `true` 启用。
- **效果**: 日志将广播到聊天栏/控制台。

---

### 服务端辅助 API
*仅在 `scripts/server/` 中可用。*

#### `game.log(String message)`
记录到服务端控制台（如果开启调试模式也会发送到聊天栏）。

#### `game.broadcast(String message)`
向所有玩家广播消息。

#### `game.modifyPlayer(String playerName, String attribute, double value)`
修改玩家属性。
- **attribute**: `"health"` (生命值), `"food"` (饱食度)。

#### `game.spawnBlock(int x, int y, int z, String blockId)`
在主世界生成方块。

---

### 客户端辅助 API
*仅在 `scripts/client/` 中可用。*

#### `game.log(String message)`
记录到客户端日志。

#### `game.chat(String message)`
在本地聊天栏显示消息（仅客户端可见）。

#### `game.sendChatMessage(String message)`
向服务器发送消息或命令。

#### `game.getPlayerName()`
获取当前玩家名称。

---

## 2. 高级功能: 直接 Java 访问

ScriptBridge 使用 **GraalVM** 并开启了 `allowAllAccess(true)`，这意味着你可以使用 `Java.type()` 访问游戏或模组环境中的 **任何 Java 类**。

### 用法
```javascript
const JavaString = Java.type('java.lang.String');
const System = Java.type('java.lang.System');
```

### 客户端原生示例
你可以直接访问 `MinecraftClient`。

```javascript
// scripts/client/native_test.js
const MinecraftClient = Java.type('net.minecraft.client.MinecraftClient');
const Text = Java.type('net.minecraft.text.Text');

// 获取客户端实例
const client = MinecraftClient.getInstance();

// 检查玩家是否存在
if (client.player != null) {
    // 发送原始数据包或修改客户端状态
    client.player.sendMessage(Text.of("§a来自原生 Java API 的问候！"), false);
    
    // 示例: 设置 FOV (如果可访问) 或其他选项
    // client.options.fov.setValue(110.0); 
}
```

### 服务端原生示例
你可以访问 `MinecraftServer` 和内部逻辑。

```javascript
// scripts/server/native_test.js
const Items = Java.type('net.minecraft.item.Items');
const ItemStack = Java.type('net.minecraft.item.ItemStack');

// 注意: 访问服务器实例通常需要从上下文或静态辅助类中获取
// 因为 'game' 对象实现持有服务器实例，你可能需要反射或静态 getter。
// 不过，许多 Fabric 类都有静态注册表。

// 示例: 打印注册表条目名称
const stone = Items.STONE;
game.log("原生物品名称: " + stone.toString());
```

### 重要提示
- **映射 (Mappings)**: 你必须使用正确的类名（取决于运行时环境是 Intermediary 还是 Yarn）。在生产环境的 Fabric 环境中（已重映射），如果不是在带有 Yarn 映射的开发环境中运行，你可能需要使用 Intermediary 名称，或者依赖模组加载器的重映射处理。*对于大多数最终用户，此功能是实验性的，并且依赖于运行时映射。*
- **反射**: 如果需要，你可以使用 Java 反射。
- **权限**: 这允许你做模组能做的任何事情。请谨慎使用。
