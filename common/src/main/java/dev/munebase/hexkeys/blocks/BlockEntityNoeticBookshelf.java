package dev.munebase.hexkeys.blocks;

import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class BlockEntityNoeticBookshelf extends BlockEntityAkashicBookshelf
{
	private static final String KEYBIND_ID_TAG = "NoeticKeybindId";
	private static final String DISPLAY_CODE_TAG = "NoeticDisplayCode";

	@Nullable
	private String keybindId;
	@Nullable
	private String displayCode;

	public BlockEntityNoeticBookshelf(BlockPos pos, BlockState state) {
		super(pos, state);
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
		super.saveModData(nbt);
		if (keybindId != null && !keybindId.isBlank()) {
			nbt.putString(KEYBIND_ID_TAG, keybindId);
		}
		if (displayCode != null && !displayCode.isBlank()) {
			nbt.putString(DISPLAY_CODE_TAG, displayCode);
		}
	}

	@Override
	protected void loadModData(NbtCompound nbt) {
		super.loadModData(nbt);
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