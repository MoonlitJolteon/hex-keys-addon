package dev.munebase.hexkeys.items;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.munebase.hexkeys.Hexkeys;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.Supplier;

public class HexkeysItems
{
	public static DeferredRegister<Item> items = DeferredRegister.create(Hexkeys.MOD_ID, Registry.ITEM_KEY);

	public static <T extends Item> RegistrySupplier<T> item(String name, Supplier<T> item) {
		return items.register(new Identifier(Hexkeys.MOD_ID, name), item);
	}

	public static Item.Settings defaultSettings(){
		return new Item.Settings();
//		return new Item.Settings().group(HEXKEYS_GROUP);
	}

//	public static final ItemGroup HEXKEYS_GROUP = CreativeTabRegistry.create(
//			new Identifier(Hexkeys.MOD_ID, "general"),
//			() -> PINK_TINTED_GLASSES.get().getDefaultStack());

	public static void register(){
		items.register();
	}
}
