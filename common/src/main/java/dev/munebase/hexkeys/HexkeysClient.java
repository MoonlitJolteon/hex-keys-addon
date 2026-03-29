package dev.munebase.hexkeys;

import dev.munebase.dynamickeybinds.action.DynamicKeybindActionRegistry;
import dev.munebase.hexkeys.blocks.BlockNoeticBookshelf;
import dev.munebase.hexkeys.network.NoeticShelfKeybindNetworking;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

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
            String keyId = data.getString("KeyID");

            if (!NetworkManager.canServerReceive(NoeticShelfKeybindNetworking.USE_NOETIC_SHELF)) {
                client.player.sendMessage(Text.literal("Noetic Bookshelf request channel unavailable on server"), false);
                return;
            }

            BlockPos pos = new BlockPos(x, y, z);
            NoeticShelfKeybindNetworking.sendUseRequestToServer(keyId, dimension, pos);
        });
    }
}
