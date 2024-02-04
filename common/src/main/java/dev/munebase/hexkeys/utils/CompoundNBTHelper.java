package dev.munebase.hexkeys.utils;

import net.minecraft.nbt.NbtCompound;

public class CompoundNBTHelper
{

	public static boolean verifyExistance(NbtCompound compoundTag, String tag)
	{
		return compoundTag.contains(tag);
	}

	public static NbtCompound getOrCreateTag(NbtCompound compoundTag, String tag)
	{
		if (!verifyExistance(compoundTag, tag))
		{
			compoundTag.put(tag, new NbtCompound());
		}

		return compoundTag.getCompound(tag);
	}
}
