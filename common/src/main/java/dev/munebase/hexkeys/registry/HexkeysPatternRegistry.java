package dev.munebase.hexkeys.registry;

import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import dev.munebase.hexkeys.casting.patterns.operators.OpGetCasterMindscape;
import dev.munebase.hexkeys.casting.patterns.operators.OpGetNoeticBookshelfRef;
import dev.munebase.hexkeys.casting.patterns.operators.OpReadNoeticBookshelf;
import dev.munebase.hexkeys.casting.patterns.spells.OpAccessMindscape;
import dev.munebase.hexkeys.casting.patterns.spells.OpConcatNoeticBookshelf;
import dev.munebase.hexkeys.casting.patterns.spells.OpEraseNoeticBookshelf;
import dev.munebase.hexkeys.casting.patterns.spells.OpVisitMindscape;
import dev.munebase.hexkeys.casting.patterns.spells.OpWriteNoeticBookshelf;
import dev.munebase.hexkeys.casting.patterns.spells.greater.OpKleinChest;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static dev.munebase.hexkeys.Hexkeys.id;

public class HexkeysPatternRegistry {
    public record PatternRegistration(HexPattern pattern, Identifier id, Action action) {}

    public static List<PatternRegistration> PATTERNS = new ArrayList<>();
    public static List<PatternRegistration> PER_WORLD_PATTERNS = new ArrayList<>();
    // IMPORTANT: be careful to keep the registration calls looking like this, or you'll have to edit the hexdoc pattern regex.
    public static HexPattern LIBRARY = register(HexPattern.fromAngles("qqqqqewwddada", HexDir.NORTH_WEST), "library", new OpAccessMindscape());
    public static HexPattern VISIT_LIBRARY = register(HexPattern.fromAngles("waqqqqqewwddada", HexDir.EAST), "visit_library", new OpVisitMindscape());
    public static HexPattern READ_NOETIC_BOOKSHELF = register(HexPattern.fromAngles("qqqwqqqqqwedewqwede", HexDir.WEST), "read_noetic_bookshelf", OpReadNoeticBookshelf.INSTANCE);
    public static HexPattern GET_NOETIC_BOOKSHELF_REF = register(HexPattern.fromAngles("qwdedwqeedeeqwdedwqeede", HexDir.EAST), "get_noetic_bookshelf_ref", OpGetNoeticBookshelfRef.INSTANCE);
    public static HexPattern CONCAT_NOETIC_BOOKSHELF = register(HexPattern.fromAngles("eeeweeeeewqaqwewqaq", HexDir.EAST), "concat_noetic_bookshelf", OpConcatNoeticBookshelf.INSTANCE);
    public static HexPattern ERASE_NOETIC_BOOKSHELF = register(HexPattern.fromAngles("qqqeedeeqqqeede", HexDir.EAST), "erase_noetic_bookshelf", OpEraseNoeticBookshelf.INSTANCE);

    public static HexPattern WRITE_NOETIC_BOOKSHELF = registerPerWorld(HexPattern.fromAngles("wqwawqwqwqwqwqawewdwewa", HexDir.EAST), "write_noetic_bookshelf", OpWriteNoeticBookshelf.INSTANCE);
    public static HexPattern CREATE_LIBRARY_KEY = registerPerWorld(HexPattern.fromAngles("ewdwewdeewdwewdewewdwe", HexDir.WEST), "create_library_key", OpGetCasterMindscape.INSTANCE);
    public static HexPattern KLEINS_CHEST = registerPerWorld(HexPattern.fromAngles("eawaeawqawwawqq", HexDir.SOUTH_EAST), "kleins_chest", new OpKleinChest());
    public static void init() {
    }

    private static HexPattern register(HexPattern pattern, String name, Action action) {
        PATTERNS.add(new PatternRegistration(pattern, id(name), action));
        return pattern;
    }

    private static HexPattern registerPerWorld(HexPattern pattern, String name, Action action) {
        PER_WORLD_PATTERNS.add(new PatternRegistration(pattern, id(name), action));
        return pattern;
    }
}
