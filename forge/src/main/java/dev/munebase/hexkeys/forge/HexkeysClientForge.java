package dev.munebase.hexkeys.forge;

import dev.munebase.hexkeys.HexkeysClient;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Forge client loading entrypoint.
 */
public class HexkeysClientForge {
    public static void init(FMLClientSetupEvent event) {
        HexkeysClient.init();
    }
}
