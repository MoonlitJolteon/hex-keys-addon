package dev.munebase.hexkeys.forge;

import dev.munebase.hexkeys.HexkeysAbstractions;
import dev.munebase.hexkeys.registry.DimensionRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class HexkeysAbstractionsImpl {
    /**
     * This is the actual implementation of {@link HexkeysAbstractions#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static void processAddingDim(MinecraftServer server, ServerWorld world)
    {
        server.markWorldsDirty();
        MinecraftForge.EVENT_BUS.post(new LevelEvent.Load(world));
    }

    public static void commonSetup() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(HexkeysAbstractionsImpl::extraForForge);
    }

    private static void extraForForge(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DimensionRegistry.registerNoiseSettings();
            DimensionRegistry.registerChunkGenerators();
        });
    }
}
