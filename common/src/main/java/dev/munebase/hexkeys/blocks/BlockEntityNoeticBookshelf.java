package dev.munebase.hexkeys.blocks;

import at.petrak.hexcasting.api.block.HexBlockEntity;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class BlockEntityNoeticBookshelf extends HexBlockEntity
{
	private static final String PATTERN_TAG = "pattern";
	private static final String IOTA_TAG = "iota";
	private static final String DUMMY_TAG = "dummy";
	private static final String KEYBIND_ID_TAG = "NoeticKeybindId";
	private static final String DISPLAY_CODE_TAG = "NoeticDisplayCode";

	@Nullable
	private NbtCompound iotaTag;
	@Nullable
	private String keybindId;
	@Nullable
	private String displayCode;

	public BlockEntityNoeticBookshelf(BlockPos pos, BlockState state) {
		super(HexkeysBlockEntities.NOETIC_BOOKSHELF_BLOCK_ENTITY.get(), pos, state);
	}

	@Nullable
	public NbtCompound getIotaTag() {
		return iotaTag;
	}

	public boolean hasStoredIota() {
		return iotaTag != null;
	}

	@Nullable
	public Iota getStoredIota(ServerWorld world) {
		if (iotaTag == null) {
			return null;
		}

		return IotaType.deserialize(iotaTag, world);
	}

	public void setStoredIota(Iota iota) {
		iotaTag = IotaType.serialize(iota);
		updateBooksState();
	}

	public void clearStoredIota() {
		iotaTag = null;
		updateBooksState();
	}

	private void updateBooksState() {
		if (world == null) {
			markDirty();
			return;
		}

		BlockState oldState = getCachedState();
		if (!oldState.contains(BlockNoeticBookshelf.HAS_BOOKS)) {
			markDirty();
			return;
		}

		boolean hasBooks = hasStoredIota();
		if (oldState.get(BlockNoeticBookshelf.HAS_BOOKS) != hasBooks) {
			BlockState newState = oldState.with(BlockNoeticBookshelf.HAS_BOOKS, hasBooks);
			world.setBlockState(pos, newState, 3);
			world.updateListeners(pos, oldState, newState, 3);
		} else {
			markDirty();
		}
	}

	@Nullable
	public String getKeybindId() {
		return keybindId;
	}

	public void setKeybindId(String keybindId) {
		this.keybindId = keybindId;
		markDirty();
	}

	@Nullable
	public String getDisplayCode() {
		return displayCode;
	}

	public void setDisplayCode(String displayCode) {
		this.displayCode = displayCode;
		markDirty();
	}

	@Override
	protected void saveModData(NbtCompound nbt) {
		if (iotaTag != null) {
			nbt.put(IOTA_TAG, iotaTag);
		} else {
			nbt.putBoolean(DUMMY_TAG, false);
		}

		if (keybindId != null && !keybindId.isBlank()) {
			nbt.putString(KEYBIND_ID_TAG, keybindId);
		}
		if (displayCode != null && !displayCode.isBlank()) {
			nbt.putString(DISPLAY_CODE_TAG, displayCode);
		}
	}

	@Override
	protected void loadModData(NbtCompound nbt) {
		if (nbt.contains(IOTA_TAG)) {
			iotaTag = nbt.getCompound(IOTA_TAG);
		} else if (nbt.contains(DUMMY_TAG)) {
			iotaTag = null;
		} else {
			iotaTag = null;
		}

		if (nbt.contains(KEYBIND_ID_TAG)) {
			keybindId = nbt.getString(KEYBIND_ID_TAG);
		} else {
			keybindId = null;
		}

		if (nbt.contains(DISPLAY_CODE_TAG)) {
			displayCode = nbt.getString(DISPLAY_CODE_TAG);
		} else {
			displayCode = null;
		}
	}
}