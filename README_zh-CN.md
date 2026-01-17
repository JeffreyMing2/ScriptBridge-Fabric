# ScriptBridge-Fabric

ScriptBridge-Fabric 是一个 Minecraft Fabric 模组，它启用了使用 **JavaScript (GraalVM)** 进行动态脚本编写的功能。它连接了 Minecraft 内部机制和运行时脚本，允许你在客户端和服务器端运行脚本，而无需重启游戏。

## 功能特性

- **JavaScript 支持**: 使用由 GraalVM 支持的现代 JavaScript (ES6+) 编写脚本。
- **双端支持**: 分别为 **客户端** 和 **服务端** 环境提供专门支持。
- **热重载**: 使用 `/script reload` 命令即可瞬间重载脚本。
- **API 访问**: 通过简单的 `game` 对象与游戏世界、玩家和聊天栏进行交互。
- **调试功能**: 内置调试工具，方便追踪脚本执行情况。

## 安装

1. 从发布页面下载最新版本。
2. 安装 [Fabric Loader](https://fabricmc.net/)。
3. 将 `scriptbridge-fabric-*.jar` 文件放入你的 Minecraft `mods` 文件夹中。
4. 启动游戏。

## 目录结构

首次启动时，模组会在你的游戏文件夹中创建一个 `scripts` 目录（例如 `.minecraft/scripts` 或服务器根目录）。

- **客户端脚本**: `scripts/client/`  
  放置在这里的脚本将在你的本地游戏客户端上运行。
- **服务端脚本**: `scripts/server/`  
  放置在这里的脚本将在服务器上运行（包括专用服务器或单人游戏的内置服务端）。

## 使用说明

### 命令

主要命令是 `/script`。

| 命令 | 描述 |
| :--- | :--- |
| `/script run <文件名>` | 运行一个脚本。如果在客户端，默认运行客户端脚本。 |
| `/script list` | 列出当前环境下所有可用的脚本。 |
| `/script reload` | 重载所有脚本并刷新环境。 |
| `/script debug` | 切换调试模式（在聊天栏显示更多信息）。 |
| `/script openfile` | 在文件管理器中打开脚本目录。 |
| `/script client run <文件>` | 显式运行一个客户端脚本。 |
| `/script client list` | 显式列出客户端脚本。 |
| `/script server run <文件>` | 发送命令到服务器运行脚本（需要 OP 权限）。 |

### 编写脚本

在相应的目录中创建 `.js` 文件。你可以使用全局 `game` 对象与 Minecraft 进行交互。

**示例 (`scripts/client/welcome.js`):**
```javascript
game.chat("欢迎回来, " + game.getPlayerName() + "!");
game.log("欢迎脚本已执行。");
```

**示例 (`scripts/server/announce.js`):**
```javascript
game.broadcast("来自服务器的问候！");
game.spawnBlock(0, 80, 0, "minecraft:diamond_block");
```

## API 文档

有关可用方法和对象的完整列表，请参阅 [API 文档](API_zh-CN.md)。

## 源码构建

如果你想自己构建模组：

1. 克隆仓库。
2. 运行构建命令：
   ```bash
   ./gradlew build
   ```
3. 输出的 jar 文件将位于 `build/libs/` 目录下。

## 参与贡献

我们欢迎各种形式的贡献！请查看 [贡献指南](CONTRIBUTING_zh-CN.md) 了解详情。

## 社区与联系方式

- **官方交流群 (QQ)**: 1080675954

## 许可证

本项目采用 MIT 许可证 - 详情请参阅 [LICENSE](LICENSE) 文件。
