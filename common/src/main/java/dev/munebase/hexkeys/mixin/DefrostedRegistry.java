package dev.munebase.hexkeys.mixin;

import net.minecraft.util.registry.SimpleRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SimpleRegistry.class)
public interface DefrostedRegistry
{
	@Accessor("frozen")
	void setFrozen(boolean frozen);

	@Accessor("frozen")
	boolean getFrozen();
}
