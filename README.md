# ScriptBridge-Fabric

[中文文档](README_zh-CN.md)

ScriptBridge-Fabric is a Minecraft Fabric mod that enables scripting capabilities using JavaScript (GraalVM). It allows you to run scripts on both the client and server sides, bridging the gap between Minecraft and dynamic scripting.

## Features

- **JavaScript Support**: Write scripts using modern JavaScript (ES6+) powered by GraalVM.
- **Client & Server Support**: Run scripts specifically for the client or server environment.
- **API Access**: Access Minecraft internals and mod APIs through the provided ScriptBridge API.

## Installation

1. Download the latest release.
2. Install [Fabric Loader](https://fabricmc.net/).
3. Place the `scriptbridge-fabric-*.jar` file into your Minecraft `mods` folder.

## Usage

Scripts are located in the `scripts` directory (relative to your game run directory, e.g., `.minecraft/scripts` or `run/scripts` in dev).

- **Client Scripts**: Place in `scripts/client/`
- **Server Scripts**: Place in `scripts/server/`

Example script (`scripts/client/demo.js`):
```js
// Your script here
console.log("Hello from ScriptBridge!");
```

## Building from Source

```bash
./gradlew build
```

## Contributing

We welcome contributions! Please see [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

## Community

- **Official QQ Group**: 1080675954

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
