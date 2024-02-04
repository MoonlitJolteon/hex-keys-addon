package dev.munebase.hexkeys.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.network.packet.c2s.play.UpdateDifficultyC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdatePlayerAbilitiesC2SPacket;
import net.minecraft.network.packet.s2c.play.DifficultyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.ExperienceBarUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldProperties;

public class TeleportHelper
{
	public static void teleportEntity(Entity entity, ServerWorld destinationDimension, double x, double y, double z, float yRot, float xRot)
	{
		if (entity == null || entity.world.isClient() || !entity.canUsePortals())
		{
			return;
		}

		ServerWorld currentDimension = entity.getServer().getWorld(entity.getEntityWorld().getRegistryKey());


		boolean isChangingDimension = !currentDimension.getDimensionKey().getValue().equals(destinationDimension.getDimensionKey().getValue());
		final boolean entityIsPlayer = entity instanceof ServerPlayerEntity;
		final ServerPlayerEntity serverPlayerEntity = entityIsPlayer ? (ServerPlayerEntity) entity : null;

		if (isChangingDimension && !entity.canUsePortals())
		{
			//early exit
			return;
		}

		//no passengers allowed.
		if (entity.hasVehicle())
		{
			entity.getRootVehicle().detach();
		}


		//This section is mostly copied and then modified from TeleportCommand.performTeleport()
		if (entityIsPlayer)
		{
			ChunkPos chunkPos = new ChunkPos(new BlockPos(x, y, z));
			destinationDimension.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, entity.getId());
			entity.stopRiding();
			if (serverPlayerEntity.isSleeping())
			{
				serverPlayerEntity.wakeUp(true, true);
			}

			if (isChangingDimension)
			{
				serverPlayerEntity.teleport(destinationDimension, x, y, z, yRot, xRot);
			}
			else
			{
				serverPlayerEntity.networkHandler.requestTeleport(x, y, z, yRot, xRot);
			}

			serverPlayerEntity.setHeadYaw(yRot);

			//restore stuff. Annoyingly it doesn't happen automatically
			for (StatusEffectInstance effectinstance : serverPlayerEntity.getStatusEffects())
			{
				serverPlayerEntity.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(serverPlayerEntity.getId(), effectinstance));
			}

			WorldProperties worldInfo = serverPlayerEntity.world.getLevelProperties();
			//I'd always wondered what the deal was with xp not showing properly.
			serverPlayerEntity.networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(serverPlayerEntity.getAbilities()));
			serverPlayerEntity.networkHandler.sendPacket(new DifficultyS2CPacket(worldInfo.getDifficulty(), worldInfo.isDifficultyLocked()));
			serverPlayerEntity.networkHandler.sendPacket(new ExperienceBarUpdateS2CPacket(serverPlayerEntity.experienceProgress, serverPlayerEntity.totalExperience, serverPlayerEntity.experienceLevel));
		}
		else
		{
			float entityYRot = MathHelper.wrapDegrees(yRot);
			float entityXRot = MathHelper.wrapDegrees(xRot);
			entityXRot = MathHelper.clamp(entityXRot, -90.0F, 90.0F);
			if (isChangingDimension)
			{
				Entity originalEntity = entity;
				entity = originalEntity.getType().create(destinationDimension);
				if (entity == null)
				{
					//error
					LogHelper.error("Was unable to create an entity when trying to teleport it.");
					return;
				}
				entity.copyFrom(originalEntity);
				entity.updatePositionAndAngles(x, y, z, entityYRot, entityXRot);
				entity.setHeadYaw(entityYRot);

				originalEntity.updatePositionAndAngles(0, -200, 0, 0, 0);
				originalEntity.remove(Entity.RemovalReason.CHANGED_DIMENSION);

				destinationDimension.spawnEntity(entity);

				currentDimension.resetIdleTimeout();
				destinationDimension.resetIdleTimeout();
			}
			else
			{
				entity.updatePositionAndAngles(x, y, z, entityYRot, entityXRot);
				entity.setHeadYaw(entityYRot);
			}
		}

		//not sure if I care about elytra, SO todo decide later. might be fun to let them keep flying?
		if (!(entity instanceof LivingEntity) || !((LivingEntity) entity).isFallFlying())
		{
			entity.setVelocity(entity.getVelocity().multiply(1.0D, 0.0D, 1.0D));
			entity.setOnGround(true);
		}

		if (entity instanceof PathAwareEntity)
		{
			((PathAwareEntity) entity).getNavigation().stop();
		}
	}
}