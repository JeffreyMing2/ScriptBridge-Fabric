package net.mingpixel.scriptbridgeFabric.scripting;

import net.fabricmc.loader.api.FabricLoader;

public class ScriptMappings {
    /**
     * Resolves a "named" class name (e.g. "net.minecraft.client.MinecraftClient")
     * to its runtime name (e.g. "net.minecraft.class_310" in production).
     *
     * @param namedClass The fully qualified class name in the "named" namespace.
     * @return The runtime class name, or the original name if resolution fails.
     */
    public String getClassName(String namedClass) {
        try {
            return FabricLoader.getInstance()
                .getMappingResolver()
                .mapClassName("named", namedClass);
        } catch (IllegalArgumentException e) {
            // Fallback if mapping fails (e.g., class not found or namespace issue)
            return namedClass;
        }
    }
}
