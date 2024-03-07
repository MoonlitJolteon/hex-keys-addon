package dev.munebase.hexkeys.registry;

import at.petrak.hexcasting.api.PatternRegistry;
import at.petrak.hexcasting.api.spell.Action;
import at.petrak.hexcasting.api.spell.math.HexDir;
import at.petrak.hexcasting.api.spell.math.HexPattern;
import dev.munebase.hexkeys.casting.patterns.operators.OpGetCasterMindscape;
import dev.munebase.hexkeys.casting.patterns.spells.OpAccessMindscape;
import dev.munebase.hexkeys.casting.patterns.spells.OpVisitMindscape;
import dev.munebase.hexkeys.casting.patterns.spells.greater.OpKleinChest;
import kotlin.Triple;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

import static dev.munebase.hexkeys.Hexkeys.id;

public class HexkeysPatternRegistry {
    public static List<Triple<HexPattern, Identifier, Action>> PATTERNS = new ArrayList<>();
    public static List<Triple<HexPattern, Identifier, Action>> PER_WORLD_PATTERNS = new ArrayList<>();
    // IMPORTANT: be careful to keep the registration calls looking like this, or you'll have to edit the hexdoc pattern regex.
    public static HexPattern LIBRARY = register(HexPattern.fromAngles("qqqqqewwddada", HexDir.NORTH_WEST), "library", new OpAccessMindscape());
    public static HexPattern VISIT_LIBRARY = register(HexPattern.fromAngles("waqqqqqewwddada", HexDir.EAST), "visit_library", new OpVisitMindscape());
    public static HexPattern CREATE_LIBRARY_KEY = registerPerWorld(HexPattern.fromAngles("ewdwewdeewdwewdewewdwe", HexDir.WEST), "create_library_key", OpGetCasterMindscape.INSTANCE);
    public static HexPattern KLEINS_CHEST = registerPerWorld(HexPattern.fromAngles("eawaeawqawwawqq", HexDir.SOUTH_EAST), "kleins_chest", new OpKleinChest());
    public static void init() {
        try {
            for (Triple<HexPattern, Identifier, Action> patternTriple : PATTERNS) {
                PatternRegistry.mapPattern(patternTriple.getFirst(), patternTriple.getSecond(), patternTriple.getThird());
            }
            for (Triple<HexPattern, Identifier, Action> patternTriple : PER_WORLD_PATTERNS) {
                PatternRegistry.mapPattern(patternTriple.getFirst(), patternTriple.getSecond(), patternTriple.getThird(), true);
            }
        } catch (PatternRegistry.RegisterPatternException e) {
            e.printStackTrace();
        }
    }

    private static HexPattern register(HexPattern pattern, String name, Action action) {
        Triple<HexPattern, Identifier, Action> triple = new Triple<>(pattern, id(name), action);
        PATTERNS.add(triple);
        return pattern;
    }

    private static HexPattern registerPerWorld(HexPattern pattern, String name, Action action) {
        Triple<HexPattern, Identifier, Action> triple = new Triple<>(pattern, id(name), action);
        PER_WORLD_PATTERNS.add(triple);
        return pattern;
    }
}
