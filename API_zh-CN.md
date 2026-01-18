# ScriptBridge-Fabric API 文档

ScriptBridge-Fabric 为 Minecraft 提供了一个强大的 JavaScript 环境。
它暴露了一个全局 `game` 对象以便于交互，并支持带有自动映射解析功能的 **原生 Java 访问**。

---

## 1. 辅助 API (`game` 对象)

`game` 对象是你的主要入口点。它的方法在客户端和服务端之间略有不同。

### 全局方法
*在客户端和服务端均可用。*

#### `game.setDebugMode(boolean debug)`
启用或禁用调试模式。
- **debug**: `true` 启用。
- **效果**: 日志将广播到聊天栏/控制台。

---

### 客户端辅助 API (`scripts/client/`)

#### 包装类 (Wrappers)
客户端 API 提供了围绕 Minecraft 内部对象的安全包装类，使脚本编写更容易、更稳定。

#### `game.getPlayer()` -> `ScriptPlayer`
返回本地玩家的包装对象。
*   **`player.getName()`**: 获取玩家名称。
*   **`player.getHealth()` / `player.getMaxHealth()`**: 获取生命值信息。
*   **`player.getFoodLevel()`**: 获取饥饿值。
*   **`player.getPos()`**: 返回 `{x, y, z}` 坐标映射。
*   **`player.getX()`, `player.getY()`, `player.getZ()`**: 获取坐标。
*   **`player.getYaw()`, `player.getPitch()`**: 获取朝向。
*   **`player.isOnGround()`**: 检查是否在地面上。
*   **`player.chat(message)`**: 发送聊天消息。
*   **`player.jump()`**: 使玩家跳跃。
*   **`player.getInventory()`**: 返回 `ScriptInventory` 对象。
*   **`player.getMainHandItem()`**: 返回主手 `ScriptItem`。
*   **`player.getOffHandItem()`**: 返回副手 `ScriptItem`。

#### `game.getWorld()` -> `ScriptWorld`
返回客户端世界的包装对象。
*   **`world.getTime()`**: 获取世界总时间。
*   **`world.getTimeOfDay()`**: 获取当前时间 (0-24000)。
*   **`world.isRaining()`**: 检查是否下雨。
*   **`world.isThundering()`**: 检查是否打雷。
*   **`world.getDimension()`**: 获取维度 ID (如 `minecraft:overworld`)。
*   **`world.getBlock(x, y, z)`**: 返回 `ScriptBlock` 对象。

#### `ScriptBlock`
*   **`block.getId()`**: 获取方块 ID (如 `minecraft:stone`)。
*   **`block.getName()`**: 获取本地化名称。
*   **`block.isSolid()`**: 检查是否为固体。
*   **`block.isAir()`**: 检查是否为空气。
*   **`block.getX()`, `block.getY()`, `block.getZ()`**: 获取坐标。

#### `ScriptInventory`
*   **`inv.getSize()`**: 获取总槽位数。
*   **`inv.getStack(slot)`**: 获取指定槽位的 `ScriptItem`。
*   **`inv.getAll()`**: 获取所有物品的列表。

#### `ScriptItem`
*   **`item.getId()`**: 获取物品 ID。
*   **`item.getName()`**: 获取物品名称。
*   **`item.getCount()`**: 获取堆叠数量。
*   **`item.isEmpty()`**: 检查是否为空。
*   **`item.getDamage()` / `item.getMaxDamage()`**: 获取耐久度信息。

#### 其他客户端方法
*   **`game.log(message)`**: 记录到客户端日志。
*   **`game.chat(message)`**: 在本地聊天栏显示消息（仅自己可见）。
*   **`game.sendChatMessage(message)`**: 向服务器发送聊天/命令。
*   **`game.setClipboard(text)`**: 设置系统剪贴板内容。
*   **`game.getClipboard()`**: 获取系统剪贴板内容。

---

### 服务端辅助 API (`scripts/server/`)

*   **`game.log(message)`**: 记录到服务端控制台。
*   **`game.broadcast(message)`**: 向所有玩家广播消息。
*   **`game.modifyPlayer(name, attribute, value)`**: 修改玩家属性 (`health`, `food`)。
*   **`game.spawnBlock(x, y, z, blockId)`**: 在主世界生成方块。

---

## 2. 原生 Java 访问 (智能且安全)

ScriptBridge 使用 **GraalVM** 允许访问 Minecraft 内部类。

### 自动重映射 (Automatic Remapping)
你无需担心混淆名称！引擎会自动将标准类名解析为其运行时 Intermediary 名称。

```javascript
// 这在开发环境和生产环境都能正常工作！
const MinecraftClient = Java.type('net.minecraft.client.MinecraftClient');
```

### 安全限制
出于安全原因，对以下内容的访问已被 **阻止**：
*   `java.lang.Runtime`
*   `java.lang.Process`
*   `java.lang.System` (部分)
*   `java.io.*` (文件 I/O 受限)

### 示例：客户端原生访问
```javascript
const MinecraftClient = Java.type('net.minecraft.client.MinecraftClient');
const client = MinecraftClient.getInstance();

if (client.player != null) {
    client.player.jump();
}
```
