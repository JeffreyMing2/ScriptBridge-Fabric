package net.mingpixel.scriptbridgeFabric.scripting;

import net.fabricmc.loader.api.FabricLoader;

public class ScriptMappings {

    public ScriptMappings() {
        // Try to load bundled mappings when this helper is instantiated
        SimpleMappingLoader.load();
    }

    /**
     * Resolves a "named" class name (e.g. "net.minecraft.client.MinecraftClient")
     * to its runtime name (e.g. "net.minecraft.class_310" in production).
     *
     * @param namedClass The fully qualified class name in the "named" namespace.
     * @return The runtime class name, or the original name if resolution fails.
     */
    public String getClassName(String namedClass) {
        // 1. Try FabricLoader (Works in Dev Environment)
        try {
            return FabricLoader.getInstance()
                .getMappingResolver()
                .mapClassName("named", namedClass);
        } catch (IllegalArgumentException e) {
            // This usually means "named" namespace doesn't exist (Production)
        }

        // 2. Try Bundled Mappings (Works in Production)
        String mapped = SimpleMappingLoader.getIntermediaryClass(namedClass);
        
        // 3. Return mapped name, or original if no mapping found
        return mapped != null ? mapped : namedClass;
    }
}
