package dev.munebase.hexkeys.forge;

import dev.munebase.hexkeys.HexkeysAbstractions;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class HexkeysAbstractionsImpl {
    /**
     * This is the actual implementation of {@link HexkeysAbstractions#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
