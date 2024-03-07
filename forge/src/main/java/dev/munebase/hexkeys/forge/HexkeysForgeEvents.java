package dev.munebase.hexkeys.forge;

import dev.munebase.hexkeys.Hexkeys;
import dev.munebase.hexkeys.registry.DimensionRegistry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid=Hexkeys.MOD_ID)
public class HexkeysForgeEvents
{
	@SubscribeEvent
	public void worldTickEvent(TickEvent.LevelTickEvent event)
	{
		World world = event.level;
		if(!world.isClient()) {
			DimensionRegistry.verifyMindscape((ServerWorld) world);
		}
	}
}
