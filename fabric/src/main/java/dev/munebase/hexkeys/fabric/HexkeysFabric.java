package dev.munebase.hexkeys.fabric;

import dev.munebase.hexkeys.registry.DimensionRegistry;
import net.fabricmc.api.ModInitializer;
import dev.munebase.hexkeys.Hexkeys;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;

/**
 * This is your loading entrypoint on fabric(-likes), in case you need to initialize
 * something platform-specific.
 * <br/>
 * Since quilt can load fabric mods, you develop for two platforms in one fell swoop.
 * Feel free to check out the <a href="https://github.com/architectury/architectury-templates">Architectury templates</a>
 * if you want to see how to add quilt-specific code.
 */
public class HexkeysFabric implements ModInitializer
{
	@Override
	public void onInitialize()
	{
		Hexkeys.init();

		ServerTickEvents.START_WORLD_TICK.register((ServerWorld world) -> {
			DimensionRegistry.verifyMindscape(world);
		});
	}
}
