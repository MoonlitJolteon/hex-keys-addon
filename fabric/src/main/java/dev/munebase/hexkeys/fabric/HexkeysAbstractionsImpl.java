package dev.munebase.hexkeys.fabric;

import net.fabricmc.loader.api.FabricLoader;
import dev.munebase.hexkeys.HexkeysAbstractions;

import java.nio.file.Path;

public class HexkeysAbstractionsImpl {
    /**
     * This is the actual implementation of {@link HexkeysAbstractions#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
