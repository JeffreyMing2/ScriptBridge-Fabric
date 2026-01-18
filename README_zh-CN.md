# <img src="images/logo.png" alt="ScriptBridge Logo" width="64" height="64" align="center"/> ScriptBridge-Fabric

[![Fabric](https://img.shields.io/badge/Modloader-Fabric-blue?style=flat-square)](https://fabricmc.net/)
[![License](https://img.shields.io/badge/License-MIT-green.svg?style=flat-square)](LICENSE)
[![GraalVM](https://img.shields.io/badge/Powered%20By-GraalVM-orange?style=flat-square)](https://www.graalvm.org/)

[**English Documentation**](README.md) | [**API 文档**](API_zh-CN.md)

> **在 Minecraft 中释放 JavaScript 的力量。**
> ScriptBridge-Fabric 连接了 Minecraft 内部机制与运行时脚本，让你无需重启游戏即可在客户端和服务端运行动态 JavaScript 代码。

---

## 📖 简介

**ScriptBridge-Fabric** 是一个开发者友好的模组，它将 **GraalVM** JavaScript 引擎嵌入到了 Minecraft 中。与简单的宏模组不同，ScriptBridge 提供：

1.  **智能原生访问**: 可直接访问 `net.minecraft.*` 类。引擎会自动 **解析混淆映射**，因此你的脚本可以在开发环境（Named）和生产环境（Intermediary）中通用！
2.  **安全包装器**: 使用我们高级的 `ScriptPlayer`、`ScriptWorld` 和 `ScriptInventory` API，让脚本编写更稳定、更简单。
3.  **双端环境**: 区分 **客户端** (UI, 渲染, 聊天) 和 **服务端** (世界, 玩家, 逻辑) 的独立运行上下文。
4.  **热重载**: 编辑脚本并使用 `/script reload` 瞬间应用更改。

## ✨ 功能特性

- ⚡ **高性能**: 由 GraalVM 驱动，提供近乎原生的执行速度。
- 🔄 **热重载**: 无需重启。在游戏运行时即可调整逻辑。
- 🛠️ **辅助 API**: 内置 `game` 对象，提供丰富的包装器 (`game.getPlayer()`, `game.getWorld()`)。
- 🧠 **自动重映射**: 只要写 `net.minecraft.client.MinecraftClient`，模组会自动处理剩下的事情。
- 🛡️ **沙盒安全**: 危险的 Java 类（`Runtime`, `Process`, `IO`）被阻止，以确保安全。
- 💻 **TypeScript 支持**: 包含类型定义文件，支持 VS Code 自动补全。

## 📸 截图展示

*(在此处添加截图。例如：发送聊天消息的脚本，或生成结构的服务器脚本。)*

| 客户端脚本演示 | 服务端管理演示 |
| :---: | :---: |
| ![Client Demo](images/client_demo.png) | ![Server Demo](images/server_demo.png) |
| *自定义 HUD 逻辑* | *管理员工具实战* |

## 📥 安装指南

1.  **前置要求**: 为你的 Minecraft 版本安装 [Fabric Loader](https://fabricmc.net/)。
2.  **下载**: 从 [Releases](https://github.com/MingPixel/ScriptBridge-Fabric/releases) 页面下载最新的 `scriptbridge-fabric-*.jar`。
3.  **安装**: 将 jar 文件放入 `.minecraft/mods` 文件夹。
4.  **启动**: 启动游戏。`scripts` 目录将自动生成。

## 🚀 快速上手

### 目录结构

首次启动后，你会在游戏目录下看到 `scripts` 文件夹：

```text
.minecraft/
└── scripts/
    ├── client/  <-- 此处的脚本在本地客户端运行
    └── server/  <-- 此处的脚本在内置/专用服务端运行
```

### 你的第一个脚本

在 `scripts/client/` 中创建一个名为 `hello.js` 的文件：

```javascript
// scripts/client/hello.js
var player = game.getPlayer();
if (player) {
    player.chat("你好 " + player.getName() + "!");
    player.jump();
}
```

在游戏中运行：
```mcfunction
/script run hello.js
```

### 常用命令

| 命令 | 描述 |
| :--- | :--- |
| `/script run <文件>` | 运行脚本（在客户端默认运行客户端脚本）。 |
| `/script reload` | 重载所有脚本并刷新引擎。 |
| `/script list` | 显示可用脚本列表。 |
| `/script debug` | 切换聊天栏调试输出。 |
| `/script openfile` | 在系统文件管理器中打开脚本文件夹。 |

## 📚 API 参考

ScriptBridge 提供两层 API。

1.  **辅助 API**: 快速简单。
    *   `game.getPlayer()` -> 返回 `ScriptPlayer` 包装对象。
    *   `game.getWorld()` -> 返回 `ScriptWorld` 包装对象。
    *   `game.setClipboard(text)`
    *   ...等等。

2.  **原生 API**: 带有自动重映射功能的完整 Java 访问。
    ```javascript
    // 随处可用！
    const MinecraftClient = Java.type('net.minecraft.client.MinecraftClient');
    ```

👉 **[阅读完整 API 文档](API_zh-CN.md)**

## 🧑‍💻 开发者支持

我们提供 TypeScript 定义文件以获得更好的编码体验。

1.  在 **VS Code** 中打开 `scripts` 文件夹。
2.  将 `scriptbridge.d.ts`（在模组源码或仓库中可以找到）复制到你的工作区。
3.  享受 `game` 和 `Java` 对象的完整自动补全！

## 🤝 参与贡献

我们欢迎 Pull Requests！详情请参阅 [贡献指南](CONTRIBUTING_zh-CN.md)。

## 📄 许可证

本项目采用 [MIT 许可证](LICENSE)。

---

<p align="center">
  Made with ❤️ by MingPixel
</p>
