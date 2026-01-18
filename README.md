# <img src="images/logo.png" alt="ScriptBridge Logo" width="64" height="64" align="center"/> ScriptBridge-Fabric

[![Fabric](https://img.shields.io/badge/Modloader-Fabric-blue?style=flat-square)](https://fabricmc.net/)
[![License](https://img.shields.io/badge/License-MIT-green.svg?style=flat-square)](LICENSE)
[![GraalVM](https://img.shields.io/badge/Powered%20By-GraalVM-orange?style=flat-square)](https://www.graalvm.org/)

[**中文文档 (Chinese)**](README_zh-CN.md) | [**API Documentation**](API.md)

> **Unleash the power of JavaScript in Minecraft.**
> ScriptBridge-Fabric bridges the gap between Minecraft internals and runtime scripting, allowing you to run dynamic JavaScript code on both the client and server sides without restarting the game.

---

## 📖 Introduction

**ScriptBridge-Fabric** is a developer-friendly mod that embeds the **GraalVM** JavaScript engine into Minecraft. Unlike simple macro mods, ScriptBridge provides:

1.  **Full Native Access**: Access `net.minecraft.*` classes directly from JS.
2.  **Dual Environment**: Distinct contexts for **Client** (UI, Rendering, Chat) and **Server** (World, Players, Logic).
3.  **Hot Reloading**: Edit your scripts and apply changes instantly with `/script reload`.

Whether you want to automate server administration, create custom client-side HUDs, or prototype mod features, ScriptBridge is your tool.

## ✨ Features

- ⚡ **High Performance**: Powered by GraalVM for near-native execution speed.
- 🔄 **Hot Reload**: No restart required. Tweak logic while the game runs.
- 🛠️ **Helper API**: Built-in `game` object for common tasks (chat, logging, spawning blocks).
- 🔓 **Unrestricted Access**: Use `Java.type()` to access any class in the classpath (Modders beware: great power comes with great responsibility!).
- 💻 **TypeScript Support**: Includes type definitions for VS Code autocompletion.

## 📸 Screenshots

*(Add your screenshots here. For example: A script sending a chat message, or a server script spawning a structure.)*

| Client Script Demo | Server Management |
| :---: | :---: |
| ![Client Demo](images/client_demo.png) | ![Server Demo](images/server_demo.png) |
| *Custom HUD logic* | *Admin tools in action* |

## 📥 Installation

1.  **Prerequisites**: Install [Fabric Loader](https://fabricmc.net/) for your Minecraft version.
2.  **Download**: Get the latest `scriptbridge-fabric-*.jar` from the [Releases](https://github.com/MingPixel/ScriptBridge-Fabric/releases) page.
3.  **Install**: Drop the jar file into your `.minecraft/mods` folder.
4.  **Launch**: Start the game. The `scripts` directory will be created automatically.

## 🚀 Getting Started

### Directory Structure

After the first launch, you will see a `scripts` folder in your game directory:

```text
.minecraft/
└── scripts/
    ├── client/  <-- Scripts here run on your local client
    └── server/  <-- Scripts here run on the internal/dedicated server
```

### Your First Script

Create a file named `hello.js` in `scripts/client/`:

```javascript
// scripts/client/hello.js
game.chat("Hello World from ScriptBridge!");
game.log("This is a log message.");
```

Run it in-game:
```mcfunction
/script run hello.js
```

### Commands

| Command | Description |
| :--- | :--- |
| `/script run <file>` | Run a script (defaults to client side). |
| `/script reload` | Reload all scripts and refresh the engine. |
| `/script list` | Show available scripts. |
| `/script debug` | Toggle debug output in chat. |
| `/script openfile` | Open the scripts folder in your OS file manager. |

## 📚 API Reference

ScriptBridge offers a two-layered API.

1.  **Helper API**: Quick and easy.
    *   `game.chat(msg)`
    *   `game.broadcast(msg)`
    *   `game.spawnBlock(x,y,z,id)`
    *   ...and more.

2.  **Native API**: Full Java access.
    ```javascript
    const MinecraftClient = Java.type('net.minecraft.client.MinecraftClient');
    const client = MinecraftClient.getInstance();
    ```

👉 **[Read the Full API Documentation](API.md)**

## 🧑‍💻 For Developers

We provide TypeScript definitions for a better coding experience.

1.  Open the `scripts` folder in **VS Code**.
2.  Copy `scriptbridge.d.ts` (found in the mod source or repo) to your workspace.
3.  Enjoy full autocomplete for `game` and `Java` objects!

## 🤝 Contributing

We welcome Pull Requests! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for details.

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

<p align="center">
  Made with ❤️ by MingPixel
</p>
