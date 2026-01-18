# Contributing to ScriptBridge-Fabric

First off, thank you for considering contributing to ScriptBridge-Fabric! We welcome contributions from everyone.

## 🛠 Development Setup

1.  **Clone the repository**:
    ```bash
    git clone https://github.com/MingPixel/ScriptBridge-Fabric.git
    cd ScriptBridge-Fabric
    ```

2.  **Setup the environment**:
    *   This project uses Gradle. You don't need to install it manually; use the `gradlew` wrapper.
    *   Run the setup command to generate IDE configuration files (for IntelliJ IDEA, Eclipse, or VS Code):
    ```bash
    ./gradlew genSources
    ```
    *   **VS Code Users**: Install the "Extension Pack for Java" and open the project root. It should auto-detect the Gradle project.

3.  **Build the project**:
    ```bash
    ./gradlew build
    ```

## 📝 Code Style

### Java
*   We follow the standard Java naming conventions.
*   Use 4 spaces for indentation.
*   Please keep the code clean and readable.

### JavaScript (Scripts)
*   We use ES6+ syntax supported by GraalVM.
*   If you are contributing example scripts, please ensure they are well-commented.

## 🤝 How to Contribute

1.  **Fork** the repository on GitHub.
2.  **Clone** your fork locally.
3.  **Create a new branch** for your feature or bugfix (`git checkout -b feature/amazing-feature`).
4.  **Commit** your changes (`git commit -m 'Add some amazing feature'`).
5.  **Push** to the branch (`git push origin feature/amazing-feature`).
6.  **Open a Pull Request**.

## 🐛 Bug Reporting

If you find a bug, please open an issue on GitHub. Include:
*   Minecraft version
*   Fabric Loader version
*   ScriptBridge version
*   A snippet of the script that causes the issue (if applicable)
*   Logs or crash reports

## 💡 Feature Requests

We are open to new ideas! If you want a new feature (e.g., a new wrapper method), please open an issue to discuss it first.

## 📜 License

By contributing, you agree that your contributions will be licensed under the MIT License.
