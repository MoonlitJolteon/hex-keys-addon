package dev.munebase.hexkeys.mixin;

import dev.munebase.hexkeys.utils.IEntityDataSaver;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class HexkeysEntityPlayerDataCopyAfterDeathMixin implements IEntityDataSaver
{
	@Inject(method = "copyFrom", at = @At("TAIL"))
    public void InjectCopyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info)
    {
        this.setPersistentNbtData(oldPlayer.getPersistentNbtData());
    }
}
