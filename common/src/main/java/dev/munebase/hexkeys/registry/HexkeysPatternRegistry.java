package dev.munebase.hexkeys.registry;

import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import dev.munebase.hexkeys.casting.patterns.operators.OpGetCasterMindscape;
import dev.munebase.hexkeys.casting.patterns.spells.OpAccessMindscape;
import dev.munebase.hexkeys.casting.patterns.spells.OpVisitMindscape;
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
