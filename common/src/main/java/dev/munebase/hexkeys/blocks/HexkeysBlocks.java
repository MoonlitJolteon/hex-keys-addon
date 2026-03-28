package dev.munebase.hexkeys.blocks;

import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicBookshelf;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.munebase.hexkeys.Hexkeys;
import dev.munebase.hexkeys.items.HexkeysItems;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.function.Supplier;

public class HexkeysBlocks
{
	public static DeferredRegister<Block> blocks = DeferredRegister.create(Hexkeys.MOD_ID, RegistryKeys.BLOCK);

	public static final RegistrySupplier<BlockMindscapeBarrier> MINDSCAPE_BARRIER_BLOCK = block("mindscape_barrier", () -> new BlockMindscapeBarrier(AbstractBlock.Settings.copy(Blocks.BARRIER).strength(-1.0F, 3600000.0F).dropsNothing().allowsSpawning(HexkeysBlocks::never)));
	public static final RegistrySupplier<BlockNoeticBookshelf> NOETIC_BOOKSHELF_BLOCK = block("noetic_bookshelf", () -> new BlockNoeticBookshelf(AbstractBlock.Settings.copy(Blocks.BOOKSHELF).strength(3.0F, 4.0F).luminance(state -> state.get(BlockAkashicBookshelf.HAS_BOOKS) ? 4 : 0)));

	public static <T extends Block> RegistrySupplier<T> block(String name, Supplier<T> block) {
		return block(name, block, HexkeysItems.defaultSettings());
	}

	public static <T extends Block> RegistrySupplier<T> block(String name, Supplier<T> block, Item.Settings settings) {
		RegistrySupplier<T> blockRegistered = blockNoItem(name, block);
		HexkeysItems.item(name, () -> new BlockItem(blockRegistered.get(), settings));
		return blockRegistered;
	}

	public static <T extends Block> RegistrySupplier<T> blockNoItem(String name, Supplier<T> block) {
		return blocks.register(new Identifier(Hexkeys.MOD_ID, name), block);
	}

	public static void register(){
		blocks.register();
	}

	private static Boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
		return false;
	}
}
