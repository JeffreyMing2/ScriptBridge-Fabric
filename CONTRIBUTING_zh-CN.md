# 参与 ScriptBridge-Fabric 开发

首先，感谢你有兴趣为 ScriptBridge-Fabric 做出贡献！我们欢迎所有人的参与。

## 🛠 开发环境搭建

1.  **克隆仓库**:
    ```bash
    git clone https://github.com/MingPixel/ScriptBridge-Fabric.git
    cd ScriptBridge-Fabric
    ```

2.  **设置环境**:
    *   本项目使用 Gradle。你不需要手动安装它，直接使用项目中的 `gradlew` 包装器即可。
    *   运行以下命令生成 IDE 配置文件（适用于 IntelliJ IDEA, Eclipse, 或 VS Code）：
    ```bash
    ./gradlew genSources
    ```
    *   **VS Code 用户**: 请安装 "Extension Pack for Java" 插件并打开项目根目录。它会自动识别 Gradle 项目。

3.  **构建项目**:
    ```bash
    ./gradlew build
    ```

## 📝 代码规范

### Java
*   我们遵循标准的 Java 命名规范。
*   使用 4 个空格进行缩进。
*   请保持代码整洁、可读。

### JavaScript (脚本)
*   我们使用 GraalVM 支持的 ES6+ 语法。
*   如果你贡献的是示例脚本，请确保包含详细的注释。

## 🤝 如何贡献代码

1.  在 GitHub 上 **Fork** 本仓库。
2.  将你的 Fork **Clone** 到本地。
3.  为你的功能或修复 **创建一个新分支** (`git checkout -b feature/amazing-feature`)。
4.  **提交** 你的更改 (`git commit -m 'Add some amazing feature'`)。
5.  **推送到** 你的分支 (`git push origin feature/amazing-feature`)。
6.  **发起 Pull Request**。

## 🐛 Bug 报告

如果你发现了 Bug，请在 GitHub 上提交 Issue。请包含：
*   Minecraft 版本
*   Fabric Loader 版本
*   ScriptBridge 版本
*   导致问题的脚本片段（如果适用）
*   日志或崩溃报告

## 💡 功能请求

我们欢迎新想法！如果你想要添加新功能（例如新的 API 包装器方法），请先提交 Issue 进行讨论。

## 📜 许可证

参与贡献即表示你同意你的贡献将基于 MIT 许可证进行授权。
