package dev.munebase.hexkeys.worldData;

import dev.munebase.hexkeys.Hexkeys;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MindscapeStatus extends PersistentState
{
	private Set<UUID> playersWithMindscapes;

	public boolean hasMindscape(UUID playerUUID) {
		return playersWithMindscapes.contains(playerUUID);
	}

	public void giveMindscape(UUID playerUUID) {
		playersWithMindscapes.add(playerUUID);
		markDirty();
	}

	public MindscapeStatus() {
		super();
		playersWithMindscapes = new HashSet<>();
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		NbtList internallyLearned = new NbtList();
		for(UUID playerUuid : playersWithMindscapes){
			internallyLearned.add(NbtString.of(playerUuid.toString()));
		}
		nbt.put("internallyLearned", internallyLearned);
		return nbt;
	}

	public static MindscapeStatus createFromNbt(NbtCompound tag) {
		MindscapeStatus serverState = new MindscapeStatus();
		NbtList agreedPlayers = tag.getList("internallyLearned", NbtElement.STRING_TYPE);
		for(NbtElement agreedPlayer : agreedPlayers){
			serverState.giveMindscape(UUID.fromString(agreedPlayer.asString()));
		}
		return serverState;
	}

	public static MindscapeStatus getServerState(MinecraftServer server) {
		// First we get the persistentStateManager for the OVERWORLD
		PersistentStateManager persistentStateManager = server
				.getWorld(World.OVERWORLD).getPersistentStateManager();

		// Calling this reads the file from the disk if it exists, or creates a new one and saves it to the disk
		MindscapeStatus serverState = persistentStateManager.getOrCreate(
				MindscapeStatus::createFromNbt,
				MindscapeStatus::new,
				Hexkeys.MOD_ID + "_mindscape_state");

		serverState.markDirty(); // YOU MUST DO THIS!!!! Or data won't be saved correctly.

		return serverState;
	}
}
