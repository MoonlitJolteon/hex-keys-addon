package dev.munebase.hexkeys.dimensions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.munebase.hexkeys.registry.DimensionRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.datafixer.fix.BiomesFix;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.StructureSet;
import net.minecraft.util.dynamic.RegistryOps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.noise.NoiseConfig;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MindChunkGenerator extends ChunkGenerator
{

	public static final Codec<MindChunkGenerator> providerCodec = RecordCodecBuilder.create(builder -> builder.group(
			RegistryOps.createRegistryCodec(Registry.STRUCTURE_SET_KEY).forGetter(MindChunkGenerator::getStructureSetRegistry),
			RegistryOps.createRegistryCodec(Registry.BIOME_KEY).forGetter(MindChunkGenerator::getBiomeRegistry)
	).apply(builder, MindChunkGenerator::new));

	private final Registry<Biome> biomeRegistry;
	private final Registry<StructureSet> structureSets;

	public MindChunkGenerator(MinecraftServer server)
	{
		this(server.getRegistryManager().get(Registry.STRUCTURE_SET_KEY), server.getRegistryManager().get(Registry.BIOME_KEY));
	}

	public MindChunkGenerator(Registry<StructureSet> structureSets, Registry<Biome> biomes)
	{
		super(structureSets, Optional.empty(), new FixedBiomeSource(biomes.entryOf(BiomeKeys.MUSHROOM_FIELDS)));
		this.structureSets = structureSets;
		this.biomeRegistry = biomes;
	}

	@Override
	protected Codec<? extends ChunkGenerator> getCodec()
	{
		return providerCodec;
	}

	@Override
	public void carve(ChunkRegion chunkRegion, long seed, NoiseConfig noiseConfig, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver carverStep)
	{

	}

	public Registry<Biome> getBiomeRegistry()
	{
		return this.biomeRegistry;
	}

	@Override
	public void buildSurface(ChunkRegion region, StructureAccessor structures, NoiseConfig noiseConfig, Chunk chunk) {}

	@Override
	public void populateEntities(ChunkRegion region) {}

	@Override
	public int getWorldHeight()
	{
		return 256;
	}

	@Override
	public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, NoiseConfig noiseConfig, StructureAccessor structureAccessor, Chunk chunk)
	{
		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	public int getSeaLevel()
	{
		return 0;
	}

	@Override
	public int getMinimumY()
	{
		return 0;
	}

	@Override
	public int getHeight(int x, int z, Heightmap.Type heightmap, HeightLimitView world, NoiseConfig noiseConfig)
	{
		return 0;
	}

	@Override
	public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, NoiseConfig noiseConfig)
	{
		return new VerticalBlockSample(0, new BlockState[0]);
	}

	@Override
	public void getDebugHudText(List<String> text, NoiseConfig noiseConfig, BlockPos pos)
	{

	}

	public Registry<StructureSet> getStructureSetRegistry()
	{
		return this.structureSets;
	}
}
