package dev.munebase.hexkeys.worldData;

import dev.munebase.hexkeys.Hexkeys;
import dev.munebase.hexkeys.inventories.KleinInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KleinStorageData extends PersistentState
{
	private final KleinStorageData instance;
	private Map<UUID, KleinInventory> invMap;

	public KleinInventory getKleinInventory(UUID playerUUID)
	{
		KleinInventory inv = invMap.get(playerUUID);
		if (inv == null) inv = new KleinInventory();
		KleinInventory finalInv = inv;
		inv.addListener(sender -> {
			instance.setKleinInv(playerUUID, finalInv);
			instance.markDirty();
		});
		return inv;
	}

	public void setKleinInv(UUID playerUUID, KleinInventory inv)
	{
		invMap.put(playerUUID, inv);
		markDirty();
	}

	public KleinStorageData()
	{
		super();
		this.invMap = new HashMap<UUID, KleinInventory>();
		this.instance = this;
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt)
	{
		NbtCompound kleinInventoryMap = new NbtCompound();
		for (UUID playerUUID : invMap.keySet())
		{
			KleinInventory inv = invMap.get(playerUUID);
			kleinInventoryMap.put(playerUUID.toString(), inv.toNbtList());
		}
		nbt.put("klein_map", kleinInventoryMap);
		return nbt;
	}

	public static KleinStorageData createFromNBT(NbtCompound tag)
	{
		KleinStorageData serverState = new KleinStorageData();
		NbtCompound kleinInventoryMap = tag.getCompound("klein_map");
		for (String playerUUIDString : kleinInventoryMap.getKeys())
		{
			KleinInventory inv = KleinInventory.kleinInvFromNBT(kleinInventoryMap.getList(playerUUIDString, NbtElement.COMPOUND_TYPE));
//			inv.addListener(new InventoryChangedListener()
//			{
//				@Override
//				public void onInventoryChanged(Inventory sender)
//				{
//					serverState.setKleinInv(UUID.fromString(playerUUIDString), inv);
//					serverState.markDirty();
//				}
//			});

			serverState.setKleinInv(UUID.fromString(playerUUIDString), inv);
		}
		return serverState;
	}

	public static KleinStorageData getServerState(MinecraftServer server)
	{
		PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

		KleinStorageData serverState = persistentStateManager.getOrCreate(
				KleinStorageData::createFromNBT,
				KleinStorageData::new,
				Hexkeys.MOD_ID + "_klein_storage_state"
		);

		serverState.markDirty();
		return serverState;
	}
}
