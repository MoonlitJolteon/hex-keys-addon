package dev.munebase.hexkeys;

import dev.munebase.hexkeys.blocks.HexkeysBlocks;
import dev.munebase.hexkeys.casting.HexKeysDiscoverers;
import dev.munebase.hexkeys.items.HexkeysItems;
import dev.munebase.hexkeys.registry.DimensionRegistry;
import dev.munebase.hexkeys.registry.HexkeysIotaTypeRegistry;
import dev.munebase.hexkeys.registry.HexkeysItemRegistry;
import dev.munebase.hexkeys.registry.HexkeysPatternRegistry;
import dev.munebase.hexkeys.utils.ResourceLocHelper;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This is effectively the loading entrypoint for most of your code, at least
 * if you are using Architectury as intended.
 */
public class Hexkeys {
    public static final String MOD_ID = "hexkeys";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static final Identifier IDENTIFIER = ResourceLocHelper.prefix(MOD_ID);


    public static void init() {
        LOGGER.info("Hex Keys says hello!");

        HexKeysDiscoverers.init();
        HexkeysItemRegistry.init();
        HexkeysIotaTypeRegistry.init();
        HexkeysPatternRegistry.init();
        HexkeysAbstractions.commonSetup();

        HexkeysBlocks.register();
        HexkeysItems.register();

        LOGGER.info(HexkeysAbstractions.getConfigDirectory().toAbsolutePath().normalize().toString());
    }

    /**
     * Shortcut for identifiers specific to this mod.
     */
    public static Identifier id(String string) {
        return new Identifier(MOD_ID, string);
    }
}
