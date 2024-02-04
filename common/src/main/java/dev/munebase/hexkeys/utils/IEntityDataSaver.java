package dev.munebase.hexkeys.utils;

import net.minecraft.nbt.NbtCompound;

public interface IEntityDataSaver
{
	default NbtCompound getPersistentNbtData() {
		return new NbtCompound();
	};
}
