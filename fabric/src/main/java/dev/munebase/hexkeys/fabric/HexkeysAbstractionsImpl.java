package dev.munebase.hexkeys.fabric;

import dev.munebase.hexkeys.registry.DimensionRegistry;
import net.fabricmc.loader.api.FabricLoader;
import dev.munebase.hexkeys.HexkeysAbstractions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import java.nio.file.Path;

public class HexkeysAbstractionsImpl
{
	/**
	 * This is the actual implementation of {@link HexkeysAbstractions#getConfigDirectory()}.
	 */
	public static Path getConfigDirectory()
	{
		return FabricLoader.getInstance().getConfigDir();
	}

	public static void processAddingDim(MinecraftServer server, ServerWorld world)
	{

	}

	public static void commonSetup() {
		DimensionRegistry.registerNoiseSettings();
		DimensionRegistry.registerChunkGenerators();
	}
}
