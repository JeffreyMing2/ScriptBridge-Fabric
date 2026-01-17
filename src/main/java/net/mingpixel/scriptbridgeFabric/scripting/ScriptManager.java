package net.mingpixel.scriptbridgeFabric.scripting;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScriptManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("ScriptBridge-Manager");
    private Context context;
    private final Path scriptsDir;
    private final Object apiInstance;
    private boolean debugMode = false;

    public ScriptManager(Path scriptsDir, Object apiInstance) {
        this.scriptsDir = scriptsDir;
        this.apiInstance = apiInstance;
        initializeContext();
    }

    private void initializeContext() {
        // Save the original ClassLoader
        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        // Set TCCL to the mod's ClassLoader so Graal can find the language services
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

        try {
            // Initialize GraalVM context with access to Java
            // Explicitly use the class loader of the current class (Mod ClassLoader)
            // This is critical for Fabric/Knot environments to find GraalVM languages and resources
            this.context = Context.newBuilder("js")
                    .allowAllAccess(true)
                    .allowHostAccess(org.graalvm.polyglot.HostAccess.ALL)
                    .allowHostClassLookup(s -> true)
                    .option("engine.WarnInterpreterOnly", "false") // Suppress warning if running without GraalVM JDK
                    .hostClassLoader(this.getClass().getClassLoader()) // CRITICAL FIX: Set correct ClassLoader
                    .build();
            
            // Bind the API
            context.getBindings("js").putMember("game", apiInstance);
            // Bind the Mappings helper for class resolution
            context.getBindings("js").putMember("Mappings", new ScriptMappings());
            
            LOGGER.info("Scripting engine initialized for path: {}", scriptsDir);
        } catch (Throwable e) {
            LOGGER.error("Failed to initialize scripting engine", e);
            throw new RuntimeException("GraalJS initialization failed", e); // Re-throw to make it visible
        } finally {
            // Restore the original ClassLoader
            Thread.currentThread().setContextClassLoader(originalClassLoader);
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

    public void executeScript(String filename) {
        Path scriptPath = scriptsDir.resolve(filename);
        if (Files.exists(scriptPath) && Files.isRegularFile(scriptPath)) {
            executeScript(scriptPath);
        } else {
            LOGGER.warn("Script not found: {}", filename);
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

    public void setDebugMode(boolean debug) {
        this.debugMode = debug;
        if (apiInstance instanceof IScriptApi) {
            ((IScriptApi) apiInstance).setDebugMode(debug);
            LOGGER.info("Debug mode set to: {}", debug);
        }
    }

    public boolean toggleDebugMode() {
        setDebugMode(!this.debugMode);
        return this.debugMode;
    }

    public List<String> getScriptList() {
        if (!Files.exists(scriptsDir)) {
            return Collections.emptyList();
        }
        try (Stream<Path> paths = Files.walk(scriptsDir)) {
            return paths.filter(Files::isRegularFile)
                        .filter(p -> p.toString().endsWith(".js"))
                        .map(p -> scriptsDir.relativize(p).toString())
                        .collect(Collectors.toList());
        } catch (IOException e) {
            LOGGER.error("Failed to list scripts", e);
            return Collections.emptyList();
        }
    }
}
