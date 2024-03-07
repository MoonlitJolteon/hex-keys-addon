package dev.munebase.hexkeys.registry;

import dev.munebase.hexkeys.Hexkeys;
import dev.munebase.hexkeys.dimensions.MindChunkGenerator;
import dev.munebase.hexkeys.utils.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.*;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import dev.munebase.hexkeys.utils.DimensionHelper;

public class DimensionRegistry
{
	public static RegistryKey<ChunkGeneratorSettings> DIMENSION_NOISE_SETTINGS;

	public static class Dimensions
	{
		public static final RegistryKey<World> MINDSCAPE_DIMENSION_KEY = RegistryKey.of(Registry.WORLD_KEY,
				new Identifier(Hexkeys.MOD_ID, "mindscape"));
	}

	public static class DimensionTypes
	{
		private static final Identifier dimType = ResourceLocHelper.prefix("mindscape");
		public static final RegistryKey<DimensionType> MINDSCAPE_DIM_TYPE = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, dimType);
	}

	public static void registerDimensions()
	{
		Hexkeys.LOGGER.debug("Registering hexkeys dimensions..");
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

	public static ServerWorld createMindscape(MinecraftServer server, BlockPos spawnLocation)
	{
		ServerWorld mindscape = server.getWorld(Dimensions.MINDSCAPE_DIMENSION_KEY);

		StructurePlacementData settings = (new StructurePlacementData()).setIgnoreEntities(true).setMirror(BlockMirror.NONE.NONE).setRotation(BlockRotation.NONE);
		StructureTemplateManager manager = mindscape.getStructureTemplateManager();
		Identifier mindscapeLibraryLocation = new Identifier(Hexkeys.MOD_ID, "mindscape");

		Optional<StructureTemplate> templateOptional = manager.getTemplate(mindscapeLibraryLocation);
		if (templateOptional.isPresent())
		{
			StructureTemplate template = templateOptional.get();
			BlockPos pos = new BlockPos(spawnLocation.getX() + (-template.getSize().getX() / 2), DimensionHelper.FLOOR_LEVEL - (template.getSize().getY() - 20), spawnLocation.getZ() + (-template.getSize().getZ() / 2));
			template.place(mindscape, pos, new BlockPos(0, 0, 0), settings, mindscape.random, 0);
		}

		return mindscape;
	}

	public static void verifyMindscape(ServerWorld world)
	{
		if(!DimensionHelper.isDimensionOfType(world, DimensionTypes.MINDSCAPE_DIM_TYPE)) return;
		List<ServerPlayerEntity> players = world.getPlayers();
		players.forEach(player -> {
			NbtCompound mindNBT = PlayerHelper.getPersistentTag(player, Hexkeys.IDENTIFIER.toString());
			String mindscapeOwnerUUID = mindNBT.getString("CURRENT_MINDSCAPE_OWNER_UUID");
			BlockPos mindscapeCenter = DimensionHelper.getMindscapePos(UUID.fromString(mindscapeOwnerUUID));
			BlockPos playerPosition = player.getBlockPos();
			double distanceFromCenter = MathHelper.distanceBetweenBlockPos(mindscapeCenter, playerPosition);
			if(distanceFromCenter > 3000 || playerPosition.getY() < -40) {
				player.fallDistance = 0;
				player.teleport(mindscapeCenter.getX()+0.5, mindscapeCenter.getY(), mindscapeCenter.getZ()+0.5);
			}
		});
	}
}
