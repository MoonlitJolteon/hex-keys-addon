package dev.munebase.hexkeys.blocks;

import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicBookshelf;
import dev.munebase.dynamickeybinds.DynamicKeyRegistry;
import dev.munebase.dynamickeybinds.DynamicKeyRegistryProvider;
import dev.munebase.dynamickeybinds.action.DynamicKeybindAction;
import dev.munebase.dynamickeybinds.model.DisplayArg;
import dev.munebase.dynamickeybinds.model.DisplaySpec;
import dev.munebase.hexkeys.Hexkeys;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class BlockNoeticBookshelf extends BlockAkashicBookshelf
{
	private static final String NOETIC_KEYBIND_ID_PREFIX = "hknb_";
	public static final String NOETIC_KEYBIND_ACTION_ID = "hexkeys:noetic_bookshelf";
	private static final String NOETIC_CATEGORY = "hexkeys.noetic_bookshelf";
	private static final int DISPLAY_CODE_LENGTH = 6;
	private static final String DISPLAY_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	public BlockNoeticBookshelf(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	public @Nullable BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new BlockEntityNoeticBookshelf(pos, state);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);

		var blockEntity = world.getBlockEntity(pos);
		if (!(blockEntity instanceof BlockEntityNoeticBookshelf noeticBookshelfEntity)) {
			return;
		}

		String keybindId = noeticBookshelfEntity.getKeybindId();
		if (keybindId == null || keybindId.isBlank()) {
			keybindId = makeDefaultKeybindId(world, pos);
			noeticBookshelfEntity.setKeybindId(keybindId);
		}

		String displayCode = getDisplayCode(world, pos, noeticBookshelfEntity);

		if (!world.isClient) {
			return;
		}

		try {
			DynamicKeyRegistry registry = DynamicKeyRegistryProvider.getRegistry();
			if (registry.getKeyBindById(keybindId) == null) {
				Optional<DynamicKeybindAction> action = makeNoeticKeybindAction(world, pos, keybindId);
				DisplaySpec displaySpec = DisplaySpec.ofTranslationKeyWithFallbackAndArgs(
					"key.hexkeys.noetic_bookshelf",
					"Noetic Bookshelf %s",
					List.of(new DisplayArg.StringArg(displayCode))
				);
				registry.registerDynamicKey(keybindId, GLFW.GLFW_KEY_UNKNOWN, NOETIC_CATEGORY, action, displaySpec);
			}
		} catch (IllegalStateException | IllegalArgumentException e) {
			Hexkeys.LOGGER.warn("Failed to register Noetic Bookshelf keybind", e);
		}
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		unregisterNoeticKeybind(world, pos);
		super.onBreak(world, pos, state, player);
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			unregisterNoeticKeybind(world, pos);
		}

		super.onStateReplaced(state, world, pos, newState, moved);
	}

	private static void unregisterNoeticKeybind(World world, BlockPos pos) {
		if (!world.isClient) {
			return;
		}

		try {
			DynamicKeyRegistry registry = DynamicKeyRegistryProvider.getRegistry();
			var blockEntity = world.getBlockEntity(pos);
			if (!(blockEntity instanceof BlockEntityNoeticBookshelf noeticBookshelfEntity)) {
				return;
			}

			String keybindId = noeticBookshelfEntity.getKeybindId();
			if (keybindId == null || keybindId.isBlank()) {
				keybindId = makeDefaultKeybindId(world, pos);
			}

			var keyMapping = registry.getKeyBindById(keybindId);
			if (keyMapping != null) {
				registry.unregisterDynamicKey(keyMapping);
			}
		} catch (IllegalStateException e) {
			Hexkeys.LOGGER.warn("Failed to unregister Noetic Bookshelf keybind", e);
		}
	}

	private static String makeDefaultKeybindId(World world, BlockPos pos) {
		var blockEntity = world.getBlockEntity(pos);
		if (!(blockEntity instanceof BlockEntityNoeticBookshelf noeticBookshelfEntity)) {
			return NOETIC_KEYBIND_ID_PREFIX + generateDisplayCode(world, pos);
		}
		return NOETIC_KEYBIND_ID_PREFIX + getDisplayCode(world, pos, noeticBookshelfEntity);
	}

	private static String generateDisplayCode(World world, BlockPos pos) {
		StringBuilder code = new StringBuilder(DISPLAY_CODE_LENGTH);
		long seed = pos.asLong();
		seed = 31L * seed + world.getRegistryKey().getValue().hashCode();
		Random random = new Random(seed);
		for (int index = 0; index < DISPLAY_CODE_LENGTH; index++) {
			int randomIndex = random.nextInt(DISPLAY_CHARSET.length());
			code.append(DISPLAY_CHARSET.charAt(randomIndex));
		}
		return code.toString();
	}

	private static String getDisplayCode(World world, BlockPos pos, BlockEntityNoeticBookshelf noeticBookshelfEntity) {
		String displayCode = noeticBookshelfEntity.getDisplayCode();
		if (displayCode == null || displayCode.isBlank()) {
			displayCode = generateDisplayCode(world, pos);
			noeticBookshelfEntity.setDisplayCode(displayCode);
		}
		return displayCode;
	}

	private static Optional<DynamicKeybindAction> makeNoeticKeybindAction(World world, BlockPos pos, String keybindId) {
		NbtCompound data = new NbtCompound();
		data.putString("KeyID", keybindId);
		data.putString("Dimension", world.getRegistryKey().getValue().toString());
		data.putInt("X", pos.getX());
		data.putInt("Y", pos.getY());
		data.putInt("Z", pos.getZ());
		return Optional.of(new DynamicKeybindAction(NOETIC_KEYBIND_ACTION_ID, data));
	}
}