package dev.munebase.hexkeys;

import dev.munebase.dynamickeybinds.action.DynamicKeybindActionRegistry;
import dev.munebase.hexkeys.blocks.BlockNoeticBookshelf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

/**
 * Common client loading entrypoint.
 */
public class HexkeysClient {
    public static void init() {
        DynamicKeybindActionRegistry.register(BlockNoeticBookshelf.NOETIC_KEYBIND_ACTION_ID, (actionID, data) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) {
                return;
            }

            String dimension = data.getString("Dimension");
            int x = data.getInt("X");
            int y = data.getInt("Y");
            int z = data.getInt("Z");

            client.player.sendMessage(Text.literal("Noetic Bookshelf: " + dimension + " @ (" + x + ", " + y + ", " + z + ")"), false);
        });
    }
}
