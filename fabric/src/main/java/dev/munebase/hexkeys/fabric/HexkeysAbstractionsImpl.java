package dev.munebase.hexkeys.fabric;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.common.lib.hex.HexActions;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import dev.munebase.hexkeys.registry.DimensionRegistry;
import dev.munebase.hexkeys.registry.HexkeysIotaTypeRegistry;
import dev.munebase.hexkeys.registry.HexkeysPatternRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class HexkeysAbstractionsImpl
{
	public static void commonSetup() {
		DimensionRegistry.registerNoiseSettings();
		DimensionRegistry.registerChunkGenerators();
	}

	public static void registerHexcastingEntries() {
		for (var entry : HexkeysPatternRegistry.PATTERNS)
		{
			Registry.register(HexActions.REGISTRY, entry.id(), new ActionRegistryEntry(entry.pattern(), entry.action()));
		}

		for (var entry : HexkeysPatternRegistry.PER_WORLD_PATTERNS)
		{
			Registry.register(HexActions.REGISTRY, entry.id(), new ActionRegistryEntry(entry.pattern(), entry.action()));
		}

		Registry.register(HexIotaTypes.REGISTRY, new Identifier("hexkeys", "mindscape"), HexkeysIotaTypeRegistry.MINDSCAPE);
		Registry.register(HexIotaTypes.REGISTRY, new Identifier("hexkeys", "noetic_bookshelf"), HexkeysIotaTypeRegistry.NOETIC_BOOKSHELF);
	}
}
