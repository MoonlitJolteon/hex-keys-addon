package dev.munebase.hexkeys.registry;

import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.IotaType;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import dev.munebase.hexkeys.casting.iotas.MindscapeIota;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

import static dev.munebase.hexkeys.Hexkeys.id;

public class HexkeysIotaTypeRegistry {
    public static Map<Identifier, IotaType<?>> TYPES = new HashMap<>();

    public static final IotaType<MindscapeIota> MINDSCAPE = register("mindscape", MindscapeIota.TYPE);

    public static void init() {
        for (Map.Entry<Identifier, IotaType<?>> entry : TYPES.entrySet()) {
            Registry.register(HexIotaTypes.REGISTRY, entry.getKey(), entry.getValue());
        }
    }

    private static <U extends Iota, T extends IotaType<U>> T register(String name, T type) {
        IotaType<?> old = TYPES.put(id(name), type);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return type;
    }
}
