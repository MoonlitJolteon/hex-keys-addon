package dev.munebase.hexkeys.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class HexkeysMixin {
    @Shadow @Final private static Logger LOGGER;

    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        LOGGER.info("Hello from hexkeys client mixin!");
    }
}