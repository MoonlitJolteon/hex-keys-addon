package dev.munebase.hexkeys.blocks;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.munebase.hexkeys.Hexkeys;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class HexkeysBlockEntities
{
	public static final DeferredRegister<BlockEntityType<?>> blockEntities = DeferredRegister.create(Hexkeys.MOD_ID, RegistryKeys.BLOCK_ENTITY_TYPE);

	public static final RegistrySupplier<BlockEntityType<BlockEntityNoeticBookshelf>> NOETIC_BOOKSHELF_BLOCK_ENTITY = blockEntity(
		"noetic_bookshelf",
		() -> BlockEntityType.Builder.create(BlockEntityNoeticBookshelf::new, HexkeysBlocks.NOETIC_BOOKSHELF_BLOCK.get()).build(null)
	);

	public static <T extends BlockEntityType<?>> RegistrySupplier<T> blockEntity(String name, Supplier<T> blockEntity) {
		return blockEntities.register(new Identifier(Hexkeys.MOD_ID, name), blockEntity);
	}

	public static void register() {
		blockEntities.register();
	}
}