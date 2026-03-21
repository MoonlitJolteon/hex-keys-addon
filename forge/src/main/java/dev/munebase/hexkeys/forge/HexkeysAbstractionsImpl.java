package dev.munebase.hexkeys.forge;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.common.lib.HexRegistries;
import dev.munebase.hexkeys.registry.DimensionRegistry;
import dev.munebase.hexkeys.registry.HexkeysIotaTypeRegistry;
import dev.munebase.hexkeys.registry.HexkeysPatternRegistry;
import net.minecraft.util.Identifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

public class HexkeysAbstractionsImpl {
    public static void commonSetup() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(HexkeysAbstractionsImpl::extraForForge);
    }

    public static void registerHexcastingEntries() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(HexkeysAbstractionsImpl::onRegister);
    }

    private static void onRegister(RegisterEvent event) {
        event.register(HexRegistries.ACTION, helper -> {
            for (var entry : HexkeysPatternRegistry.PATTERNS)
            {
                helper.register(entry.id(), new ActionRegistryEntry(entry.pattern(), entry.action()));
            }
            for (var entry : HexkeysPatternRegistry.PER_WORLD_PATTERNS)
            {
                helper.register(entry.id(), new ActionRegistryEntry(entry.pattern(), entry.action()));
            }
        });

        event.register(HexRegistries.IOTA_TYPE, helper ->
            helper.register(new Identifier("hexkeys", "mindscape"), HexkeysIotaTypeRegistry.MINDSCAPE));
    }

    private static void extraForForge(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DimensionRegistry.registerNoiseSettings();
            DimensionRegistry.registerChunkGenerators();
        });
    }
}
