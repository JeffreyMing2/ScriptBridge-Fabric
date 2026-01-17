package net.mingpixel.scriptbridgeFabric.scripting;

import net.minecraft.server.MinecraftServer;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ScriptManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("ScriptBridge-Manager");
    private Context context;
    private final MinecraftServer server;
    private final Path scriptsDir;

    public ScriptManager(MinecraftServer server, Path gameDir) {
        this.server = server;
        this.scriptsDir = gameDir.resolve("scripts");
        initializeContext();
    }

    private void initializeContext() {
        try {
            // Initialize GraalVM context with access to Java
            this.context = Context.newBuilder("js")
                    .allowAllAccess(true)
                    .option("engine.WarnInterpreterOnly", "false") // Suppress warning if running without GraalVM JDK
                    .build();
            
            // Bind the API
            ScriptApi api = new ScriptApi(server);
            context.getBindings("js").putMember("game", api);
            
            LOGGER.info("Scripting engine initialized.");
        } catch (Exception e) {
            LOGGER.error("Failed to initialize scripting engine", e);
        }
    }

    public void loadScripts() {
        if (!Files.exists(scriptsDir)) {
            try {
                Files.createDirectories(scriptsDir);
                LOGGER.info("Created scripts directory at {}", scriptsDir);
                createSampleScript();
            } catch (IOException e) {
                LOGGER.error("Failed to create scripts directory", e);
                return;
            }
        }

        try (Stream<Path> paths = Files.walk(scriptsDir)) {
            paths.filter(Files::isRegularFile)
                 .filter(p -> p.toString().endsWith(".js"))
                 .forEach(this::executeScript);
        } catch (IOException e) {
            LOGGER.error("Error loading scripts", e);
        }
    }

    private void createSampleScript() {
        try {
            String sample = """
                            // Sample Script
                            game.log('Hello from Javascript!');
                            game.broadcast('ScriptBridge loaded!');
                            
                            // Example: Modify player (uncomment to use)
                            // game.modifyPlayer('Dev', 'health', 20.0);
                            
                            // Example: Spawn block (uncomment to use)
                            // game.spawnBlock(0, 100, 0, 'minecraft:diamond_block');
                            """;
            Files.writeString(scriptsDir.resolve("example.js"), sample);
        } catch (IOException e) {
            LOGGER.error("Failed to create sample script", e);
        }
    }

    public void executeScript(Path path) {
        try {
            Source source = Source.newBuilder("js", path.toFile()).build();
            context.eval(source);
            LOGGER.info("Executed script: {}", path.getFileName());
        } catch (IOException e) {
            LOGGER.error("Failed to read script: " + path, e);
        } catch (Exception e) {
            LOGGER.error("Error executing script: " + path, e);
        }
    }
    
    public void close() {
        if (context != null) {
            context.close();
        }
    }
}
