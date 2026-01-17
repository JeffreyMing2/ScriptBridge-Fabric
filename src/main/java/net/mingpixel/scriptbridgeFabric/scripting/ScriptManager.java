package net.mingpixel.scriptbridgeFabric.scripting;

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
    private final Path scriptsDir;
    private final Object apiInstance;

    public ScriptManager(Path scriptsDir, Object apiInstance) {
        this.scriptsDir = scriptsDir;
        this.apiInstance = apiInstance;
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
            context.getBindings("js").putMember("game", apiInstance);
            
            LOGGER.info("Scripting engine initialized for path: {}", scriptsDir);
        } catch (Exception e) {
            LOGGER.error("Failed to initialize scripting engine", e);
        }
    }

    public void loadScripts() {
        if (!Files.exists(scriptsDir)) {
            try {
                Files.createDirectories(scriptsDir);
                LOGGER.info("Created scripts directory at {}", scriptsDir);
                // Sample script creation is delegated or handled externally if needed
                // For now, we won't auto-create sample scripts here to avoid confusion between client/server
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
    
    public void reload() {
        LOGGER.info("Reloading scripts...");
        close();
        initializeContext();
        loadScripts();
        LOGGER.info("Scripts reloaded.");
    }
    
    public void close() {
        if (context != null) {
            context.close();
        }
    }
}
