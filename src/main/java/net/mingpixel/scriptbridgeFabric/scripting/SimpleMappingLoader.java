package net.mingpixel.scriptbridgeFabric.scripting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SimpleMappingLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger("ScriptBridge-Mapper");
    private static final Map<String, String> namedToIntermediary = new HashMap<>();
    private static boolean loaded = false;

    public static synchronized void load() {
        if (loaded) return;
        
        // Path matches the build.gradle output
        // archives_base_name is 'scriptbridge-fabric' by default in gradle.properties
        String path = "/assets/scriptbridge-fabric/mappings/bundled_mappings.tiny";
        
        try (InputStream stream = SimpleMappingLoader.class.getResourceAsStream(path)) {
            if (stream == null) {
                // This is expected in dev environment where mappings.tiny isn't bundled yet
                // or if the path is slightly different. In dev, FabricLoader handles it.
                LOGGER.debug("Bundled mappings not found at {}. Assuming dev environment or missing mappings.", path);
                return;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                int namedIndex = -1;
                int intermediaryIndex = -1;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split("\t");

                    if (line.startsWith("tiny")) {
                        // Header: tiny	2	0	intermediary	named
                        for (int i = 0; i < parts.length; i++) {
                            if (parts[i].equals("named")) namedIndex = i - 3; 
                            if (parts[i].equals("intermediary")) intermediaryIndex = i - 3;
                        }
                        continue;
                    }
                    
                    if (line.startsWith("c\t")) { 
                        // Class mapping: c  <name1>  <name2> ...
                        // We need named -> intermediary
                        if (namedIndex >= 0 && intermediaryIndex >= 0 && parts.length > Math.max(namedIndex, intermediaryIndex) + 1) {
                            // parts[0] is "c"
                            // parts[1] is col 0, parts[2] is col 1...
                            // So if intermediary is col 0, it is at parts[1]
                            String intermediary = parts[intermediaryIndex + 1];
                            String named = parts[namedIndex + 1];
                            
                            // Convert slashes to dots for Java Runtime usage
                            String dotNamed = named.replace('/', '.');
                            String dotIntermediary = intermediary.replace('/', '.');
                            
                            namedToIntermediary.put(dotNamed, dotIntermediary);
                        }
                    }
                }
            }
            loaded = true;
            LOGGER.info("Loaded {} class mappings from bundle.", namedToIntermediary.size());
        } catch (Exception e) {
            LOGGER.error("Failed to load bundled mappings", e);
        }
    }

    public static String getIntermediaryClass(String namedClass) {
        if (!loaded) load();
        return namedToIntermediary.getOrDefault(namedClass, namedClass);
    }
}
