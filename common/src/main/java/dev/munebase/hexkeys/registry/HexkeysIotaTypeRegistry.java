package dev.munebase.hexkeys.registry;

import at.petrak.hexcasting.api.casting.iota.IotaType;
import dev.munebase.hexkeys.casting.iotas.MindscapeIota;
import dev.munebase.hexkeys.casting.iotas.NoeticBookshelfIota;

public class HexkeysIotaTypeRegistry {
    public static final IotaType<MindscapeIota> MINDSCAPE = MindscapeIota.TYPE;
    public static final IotaType<NoeticBookshelfIota> NOETIC_BOOKSHELF = NoeticBookshelfIota.TYPE;

    public static void init() {
    }
}
