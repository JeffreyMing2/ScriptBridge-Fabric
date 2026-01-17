# ScriptBridge-Fabric

ScriptBridge-Fabric 是一个 Minecraft Fabric 模组，它启用了使用 JavaScript (GraalVM) 进行脚本编写的功能。它允许你在客户端和服务器端运行脚本，连接了 Minecraft 和动态脚本。

## 功能特性

- **JavaScript 支持**: 使用由 GraalVM 支持的现代 JavaScript (ES6+) 编写脚本。
- **客户端与服务端支持**: 分别为客户端或服务器环境运行脚本。
- **API 访问**: 通过提供的 ScriptBridge API 访问 Minecraft 内部和模组 API。

## 安装

1. 下载最新版本。
2. 安装 [Fabric Loader](https://fabricmc.net/)。
3. 将 `scriptbridge-fabric-*.jar` 文件放入你的 Minecraft `mods` 文件夹中。

## 使用说明

脚本位于 `scripts` 目录中（相对于你的游戏运行目录，例如 `.minecraft/scripts` 或开发环境中的 `run/scripts`）。

- **客户端脚本**: 放入 `scripts/client/`
- **服务端脚本**: 放入 `scripts/server/`

示例脚本 (`scripts/client/demo.js`):
```js
// 你的脚本
console.log("Hello from ScriptBridge!");
```

## 源码构建

```bash
./gradlew build
```

## 许可证

本项目采用 MIT 许可证 - 详情请参阅 [LICENSE](LICENSE) 文件。
