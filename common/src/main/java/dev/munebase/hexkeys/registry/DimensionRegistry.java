package dev.munebase.hexkeys.registry;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;
import dev.munebase.hexkeys.Hexkeys;
import dev.munebase.hexkeys.HexkeysAbstractions;
import dev.munebase.hexkeys.dimensions.MindChunkGenerator;
import dev.munebase.hexkeys.utils.DimensionHelper;
import dev.munebase.hexkeys.utils.LogHelper;
import dev.munebase.hexkeys.utils.ResourceLocHelper;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorderListener;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import net.minecraft.world.level.storage.LevelStorage;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;

public class DimensionRegistry
{
	public static RegistryKey<ChunkGeneratorSettings> DIMENSION_NOISE_SETTINGS;

	public static class DimensionTypes
	{
		private static final Identifier dimType = ResourceLocHelper.prefix("mindscape");
		public static final RegistryKey<DimensionType> MINDSCAPE_DIM_TYPE = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, dimType);
	}

	public static void registerNoiseSettings()
	{
		DIMENSION_NOISE_SETTINGS = RegistryKey.of(Registry.CHUNK_GENERATOR_SETTINGS_KEY, Hexkeys.IDENTIFIER);
	}

	public static void registerChunkGenerators()
	{
		Registry.register(Registry.CHUNK_GENERATOR, Hexkeys.IDENTIFIER, MindChunkGenerator.providerCodec);
	}

	public static DimensionOptions mindscapeBuilder(MinecraftServer server, RegistryKey<DimensionOptions> dimensionKey)
	{
		DynamicRegistryManager registies = server.getRegistryManager();
		return new DimensionOptions(
				registies.get(Registry.DIMENSION_TYPE_KEY).entryOf(DimensionTypes.MINDSCAPE_DIM_TYPE),
				new MindChunkGenerator(server)
		);
	}

	public static ServerWorld createMindscape(MinecraftServer server, RegistryKey<World> worldKey)
	{
		RegistryKey<DimensionOptions> dimKey = RegistryKey.of(Registry.DIMENSION_KEY, worldKey.getValue());
		BiFunction<MinecraftServer, RegistryKey<DimensionOptions>, DimensionOptions> dimFactory = DimensionRegistry::mindscapeBuilder;
		System.out.println(dimKey);
		DimensionOptions dim = dimFactory.apply(server, dimKey);

		Executor executor = server.workerExecutor;
		LevelStorage.Session levelSave = server.session;
		WorldGenerationProgressListener progressListener = server.worldGenerationProgressListenerFactory.create(11);

		//configs
		SaveProperties serverConfiguration = server.getSaveProperties();
		GeneratorOptions worldGenSettings = serverConfiguration.getGeneratorOptions();

		// register the dimension
		Registry<DimensionOptions> dimRegistry = worldGenSettings.getDimensions();
		if (dimRegistry instanceof MutableRegistry)
		{
			final MutableRegistry<DimensionOptions> mutableRegistry = (MutableRegistry<DimensionOptions>) dimRegistry;
			mutableRegistry.add(dimKey, dim, Lifecycle.stable());
		}
		else
		{
			throw new IllegalStateException("Unable to register dimension '" + dimKey.getValue().toString() + "'! Registry not writable!");
		}

		//base the world info on overworld? Not actually sure if that's what I want for soul dimensions
		//todo revisit this later. Don't just forget about it.
		// ^ LOL
		UnmodifiableLevelProperties derivedWorldInfo = new UnmodifiableLevelProperties(serverConfiguration, serverConfiguration.getMainWorldProperties());


		ServerWorld newMindscape = new ServerWorld(
				server,
				executor,
				levelSave,
				derivedWorldInfo,
				worldKey,
				dim,
				progressListener,
				worldGenSettings.isDebugWorld(),
				BiomeAccess.hashSeed(worldGenSettings.getSeed()),
				ImmutableList.of(),
				false);

		server.getWorld(World.OVERWORLD).getWorldBorder().addListener(new WorldBorderListener.WorldBorderSyncer(newMindscape.getWorldBorder()));

		Map<RegistryKey<World>, ServerWorld> map = server.worlds;
		map.put(worldKey, newMindscape);

		HexkeysAbstractions.processAddingDim(server, newMindscape);

		LogHelper.info("New mindscape has been created: " + dimKey.getValue().toString());

		final int PLATFORM_RADIUS = 16;
		for (int x = -PLATFORM_RADIUS; x < PLATFORM_RADIUS; x++)
		{
			for (int z = -PLATFORM_RADIUS; z < PLATFORM_RADIUS; z++)
			{
				newMindscape.setBlockState(new BlockPos(x, DimensionHelper.FLOOR_LEVEL, z), Blocks.GRASS_BLOCK.getDefaultState());
				newMindscape.setBlockState(new BlockPos(x, DimensionHelper.FLOOR_LEVEL - 1, z), Blocks.DIRT.getDefaultState());
				newMindscape.setBlockState(new BlockPos(x, DimensionHelper.FLOOR_LEVEL - 2, z), Blocks.DIRT.getDefaultState());
				newMindscape.setBlockState(new BlockPos(x, DimensionHelper.FLOOR_LEVEL - 3, z), Blocks.DIRT.getDefaultState());
				newMindscape.setBlockState(new BlockPos(x, DimensionHelper.FLOOR_LEVEL - 4, z), Blocks.STONE.getDefaultState());
			}
		}

		return newMindscape;
	}
}
