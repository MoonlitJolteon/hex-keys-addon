package dev.munebase.hexkeys.casting;

import at.petrak.hexcasting.api.addldata.ADMediaHolder;
import at.petrak.hexcasting.api.misc.DiscoveryHandlers;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import dev.munebase.hexkeys.inventories.KleinInventory;
import dev.munebase.hexkeys.worldData.KleinStorageData;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class HexKeysDiscoverers
{
	public static void init()
	{
		DiscoveryHandlers.addMediaHolderDiscoverer(HexKeysDiscoverers::getKleinMedia);
	}

	private static List<ADMediaHolder> getKleinMedia(CastingHarness castingHarness)
	{
		CastingContext ctx = castingHarness.getCtx();
		ServerPlayerEntity player = ctx.getCaster();
		KleinInventory kleinInventory = KleinStorageData.getServerState(ctx.getWorld().getServer()).getKleinInventory(player.getUuid());
		List<ADMediaHolder> holders = new ArrayList<>();
		for (int i = 0; i < kleinInventory.size(); i++)
		{
			ItemStack itemstack = kleinInventory.getStack(i);
			ADMediaHolder originalHolder = IXplatAbstractions.INSTANCE.findMediaHolder(itemstack);
			if(originalHolder != null)
			{
				ADMediaHolder holder = new ADMediaHolder()
				{
					private ADMediaHolder origHolder = originalHolder;
					private double efficiency = 0.75;
					@Override
					public int getMedia()
					{
						return this.origHolder.getMedia();
					}
					@Override
					public int getMaxMedia()
					{
						return (this.origHolder.getMaxMedia());
					}
					@Override
					public void setMedia(int media) {}
					@Override
					public boolean canRecharge()
					{
						return this.origHolder.canRecharge();
					}
					@Override
					public boolean canProvide()
					{
						return this.origHolder.canProvide();
					}
					@Override
					public int getConsumptionPriority()
					{
						return this.origHolder.getConsumptionPriority();
					}
					@Override
					public boolean canConstructBattery()
					{
						return this.origHolder.canConstructBattery();
					}
					@Override
					public int withdrawMedia(int cost, boolean simulate)
					{
						return this.origHolder.withdrawMedia(MathHelper.floor(cost / efficiency), simulate);
					}
				};
				holders.add(holder);
			}
		}
		return holders;
	}
}
