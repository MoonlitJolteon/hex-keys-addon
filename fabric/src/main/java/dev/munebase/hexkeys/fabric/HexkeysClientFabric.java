package dev.munebase.hexkeys.fabric;

import net.fabricmc.api.ClientModInitializer;
import dev.munebase.hexkeys.HexkeysClient;

/**
 * Fabric client loading entrypoint.
 */
public class HexkeysClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HexkeysClient.init();
    }
}
