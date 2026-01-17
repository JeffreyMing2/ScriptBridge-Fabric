# ScriptBridge-Fabric

[中文文档](README_zh-CN.md)

ScriptBridge-Fabric is a Minecraft Fabric mod that enables dynamic scripting capabilities using **JavaScript (GraalVM)**. It bridges the gap between Minecraft internals and runtime scripting, allowing you to run scripts on both the client and server sides without restarting the game.

## Features

- **JavaScript Support**: Write scripts using modern JavaScript (ES6+) powered by GraalVM.
- **Dual Environment**: specialized support for both **Client** and **Server** environments.
- **Hot Reloading**: Reload scripts instantly with `/script reload`.
- **API Access**: Interact with the game world, players, and chat through a simple `game` object.
- **Debugging**: Built-in debug tools to trace script execution.

## Installation

1. Download the latest release from the releases page.
2. Install [Fabric Loader](https://fabricmc.net/).
3. Place the `scriptbridge-fabric-*.jar` file into your Minecraft `mods` folder.
4. Launch the game.

## Directory Structure

Upon first launch, the mod will create a `scripts` directory in your game folder (e.g., `.minecraft/scripts` or server root).

- **Client Scripts**: `scripts/client/`  
  Scripts placed here run on your local game client.
- **Server Scripts**: `scripts/server/`  
  Scripts placed here run on the server (dedicated or singleplayer internal server).

## Usage

### Commands

The main command is `/script`.

| Command | Description |
| :--- | :--- |
| `/script run <filename>` | Run a script. Defaults to client scripts if on client. |
| `/script list` | List all available scripts in the current context. |
| `/script reload` | Reload all scripts and refresh the environment. |
| `/script debug` | Toggle debug mode (logs more info to chat). |
| `/script openfile` | Open the scripts directory in your file explorer. |
| `/script client run <file>` | Explicitly run a client script. |
| `/script client list` | Explicitly list client scripts. |
| `/script server run <file>` | Send a command to the server to run a script (requires OP). |

### Writing Scripts

Create `.js` files in the appropriate directory. You can use the global `game` object to interact with Minecraft.

**Example (`scripts/client/welcome.js`):**
```javascript
game.chat("Welcome back, " + game.getPlayerName() + "!");
game.log("Welcome script executed.");
```

**Example (`scripts/server/announce.js`):**
```javascript
game.broadcast("Hello from the server!");
game.spawnBlock(0, 80, 0, "minecraft:diamond_block");
```

## API Documentation

For a full list of available methods and objects, please refer to the [API Documentation](API.md).

For developers using VS Code or other IDEs, a TypeScript declaration file is available at [`scriptbridge.d.ts`](scriptbridge.d.ts) for autocomplete support.

## Building from Source

To build the mod yourself:

1. Clone the repository.
2. Run the build command:
   ```bash
   ./gradlew build
   ```
3. The output jar will be in `build/libs/`.

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## Community

- **Official QQ Group**: 1080675954

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
