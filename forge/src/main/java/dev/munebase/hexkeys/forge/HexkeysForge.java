package dev.munebase.hexkeys.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.munebase.hexkeys.Hexkeys;
import dev.munebase.hexkeys.registry.DimensionRegistry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * This is your loading entrypoint on forge, in case you need to initialize
 * something platform-specific.
 */
@Mod(Hexkeys.MOD_ID)
public class HexkeysForge
{
	public HexkeysForge()
	{
		// Submit our event bus to let architectury register our content on the right time
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		EventBuses.registerModEventBus(Hexkeys.MOD_ID, bus);
		bus.addListener(HexkeysClientForge::init);
		Hexkeys.init();
	}

	@SubscribeEvent
	public void worldTickEvent(TickEvent.LevelTickEvent event)
	{
		World world = event.level;
		if(!world.isClient()) {
			DimensionRegistry.verifyMindscape((ServerWorld) world);
		}
	}
}
