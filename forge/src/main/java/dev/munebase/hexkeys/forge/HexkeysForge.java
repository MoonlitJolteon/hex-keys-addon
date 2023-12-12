package dev.munebase.hexkeys.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.munebase.hexkeys.Hexkeys;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * This is your loading entrypoint on forge, in case you need to initialize
 * something platform-specific.
 */
@Mod(Hexkeys.MOD_ID)
public class HexkeysForge {
    public HexkeysForge() {
        // Submit our event bus to let architectury register our content on the right time
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(Hexkeys.MOD_ID, bus);
        bus.addListener(HexkeysClientForge::init);
        Hexkeys.init();
    }
}
