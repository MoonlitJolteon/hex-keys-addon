package dev.munebase.hexkeys.utils;

import dev.munebase.hexkeys.Hexkeys;
import dev.munebase.hexkeys.registry.DimensionRegistry;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.Locale;
import java.util.UUID;

public class DimensionHelper
{
	public static final int FLOOR_LEVEL = 64;

	public static boolean isInMindscape(LivingEntity entity)
	{
		return isDimensionOfType(entity.getEntityWorld(), DimensionRegistry.DimensionTypes.MINDSCAPE_DIM_TYPE);
	}

	public static boolean isDimensionOfType(World world, RegistryKey<DimensionType> dimTypeKey)
	{
		// I think it has to be done this way since the soul dimension is not
		// shared between players like it is in random things SpectreKey dimension
		DimensionType type = world.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY).get(dimTypeKey);
		return type != null && type.equals(world.getDimension());
	}

	public static void FlipDimension(ServerPlayerEntity player, MinecraftServer server)
	{
		NbtCompound mindNBT = PlayerHelper.getPersistentTag(player, Hexkeys.IDENTIFIER.toString());
		ServerWorld destination;
		double x = 0.5;
		double y = FLOOR_LEVEL + 2;
		double z = 0.5;

		if (isInMindscape(player))
		{
			RegistryKey<World> destinationKey =
					RegistryKey.of(
							Registry.WORLD_KEY,
							new Identifier(
									mindNBT.getString(NBTKeys.LAST_DIMENSION_MOD_ID),
									mindNBT.getString(NBTKeys.LAST_DIMENSION_MOD_DIMENSION))
					);

			x = mindNBT.getDouble(NBTKeys.LAST_DIMENSION_X);
			y = mindNBT.getDouble(NBTKeys.LAST_DIMENSION_Y);
			z = mindNBT.getDouble(NBTKeys.LAST_DIMENSION_Z);

			try
			{
				destination = server.getWorld(destinationKey);
			}
			catch (Exception e)//sometimes people remove mods. Protect against unknown by sending them to overworld spawn.
			{
				destination = server.getOverworld();

				final BlockPos sharedSpawnPos = destination.getSpawnPos();
				x = sharedSpawnPos.getX();
				y = sharedSpawnPos.getY();
				z = sharedSpawnPos.getZ();
			}
		}
		else
		{
			destination = getOrCreateMindscape(player.getUuid(), server);
		}

		Identifier location = player.getEntityWorld().getDimensionKey().getValue();

		if (!isInMindscape(player))
		{
			mindNBT = PlayerHelper.getPersistentTag(player, Hexkeys.IDENTIFIER.toString());
			// XYZ
			mindNBT.putDouble(NBTKeys.LAST_DIMENSION_X, player.getX());
			mindNBT.putDouble(NBTKeys.LAST_DIMENSION_Y, player.getY());
			mindNBT.putDouble(NBTKeys.LAST_DIMENSION_Z, player.getZ());

			// save the dimension info
			mindNBT.putString(NBTKeys.LAST_DIMENSION_MOD_ID, location.getNamespace());
			mindNBT.putString(NBTKeys.LAST_DIMENSION_MOD_DIMENSION, location.getPath());
		}

		Vector3d newPosByDestination = new Vector3d(x,y,z);

		TeleportHelper.teleportEntity(
				player,
				destination,
				newPosByDestination.x,
				y,
				newPosByDestination.z,
				player.getHeadYaw(),
				player.getPitch()
		);
	}

	public static boolean mindscapeExists(UUID playerUUID, MinecraftServer server) {
		Identifier dim = prefix(playerUUID.toString());
		RegistryKey<World> worldKey = RegistryKey.of(Registry.WORLD_KEY, dim);
		ServerWorld mindscapeForPlayer = server.getWorld(worldKey);
		return mindscapeForPlayer != null;
	}

	private static ServerWorld getOrCreateMindscape(UUID playerUUID, MinecraftServer server)
	{
		Identifier dim = prefix(playerUUID.toString());
		RegistryKey<World> worldKey = RegistryKey.of(Registry.WORLD_KEY, dim);

		ServerWorld mindscapeForPlayer = server.getWorld(worldKey);
		return mindscapeForPlayer != null
				? mindscapeForPlayer
				: DimensionRegistry.createMindscape(server, worldKey);
	}

	private static Identifier prefix(String path)
	{
		return new Identifier(Hexkeys.MOD_ID, path.toLowerCase(Locale.ROOT));
	}

	public static class NBTKeys
	{
		public static final String LAST_DIMENSION_X = "LAST_DIMENSION_X";
		public static final String LAST_DIMENSION_Y = "LAST_DIMENSION_Y";
		public static final String LAST_DIMENSION_Z = "LAST_DIMENSION_Z";
		public static final String LAST_DIMENSION_MOD_ID = "LAST_DIMENSION_MOD_ID";
		public static final String LAST_DIMENSION_MOD_DIMENSION = "LAST_DIMENSION_MOD_DIMENSION";
	}
}
