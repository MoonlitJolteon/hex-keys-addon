package dev.munebase.hexkeys.mixin;

import dev.munebase.hexkeys.utils.IEntityDataSaver;
import dev.munebase.hexkeys.utils.PlayerHelper;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class HexkeysEntityDataSaverMixin implements IEntityDataSaver
{
	private NbtCompound persistentData;

	@Override
	public NbtCompound getPersistentNbtData()
	{
		if (this.persistentData == null)
		{
			this.persistentData = new NbtCompound();
		}
		return persistentData;
	}

	@Inject(method = "writeNbt", at = @At("HEAD"))
	protected void injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info)
	{
		if (persistentData != null)
		{
			nbt.put(PlayerHelper.PERSISTED_NBT_TAG, persistentData);
		}
	}

	@Inject(method = "readNbt", at = @At("HEAD"))
	protected void injectReadMethod(NbtCompound nbt, CallbackInfo info)
	{
		if(nbt.contains(PlayerHelper.PERSISTED_NBT_TAG)) {
			persistentData = nbt.getCompound(PlayerHelper.PERSISTED_NBT_TAG);
		}
	}
}
