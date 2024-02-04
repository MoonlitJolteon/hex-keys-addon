package dev.munebase.hexkeys.utils;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerHelper
{
	public static final String PERSISTED_NBT_TAG = "hexkeys.data";

	public static NbtCompound getPersistentTag(ServerPlayerEntity playerEntity, String tag)
	{
		NbtCompound persistentNBT = CompoundNBTHelper.getOrCreateTag(playerEntity.getPersistentNbtData(), PERSISTED_NBT_TAG);
		return CompoundNBTHelper.getOrCreateTag(persistentNBT, tag);
	}
}
