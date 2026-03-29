package dev.munebase.hexkeys.network;

import dev.architectury.networking.NetworkManager;
import dev.munebase.hexkeys.Hexkeys;
import dev.munebase.hexkeys.blocks.BlockEntityNoeticBookshelf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public final class NoeticShelfKeybindNetworking {
    public static final Identifier USE_NOETIC_SHELF = Hexkeys.id("use_noetic_shelf");

    private static boolean registered = false;

    private NoeticShelfKeybindNetworking() {
    }

    public static void registerCommon() {
        if (registered) {
            return;
        }

        NetworkManager.registerReceiver(NetworkManager.c2s(), USE_NOETIC_SHELF, (buf, context) -> {
            String keybindId = buf.readString();
            String dimensionId = buf.readString();
            BlockPos pos = buf.readBlockPos();

            context.queue(() -> handleUseRequest(context, keybindId, dimensionId, pos));
        });

        registered = true;
    }

    public static void sendUseRequestToServer(String keybindId, String dimensionId, BlockPos pos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(keybindId == null ? "" : keybindId);
        buf.writeString(dimensionId == null ? "" : dimensionId);
        buf.writeBlockPos(pos);
        NetworkManager.sendToServer(USE_NOETIC_SHELF, buf);
    }

    private static void handleUseRequest(NetworkManager.PacketContext context, String keybindId, String dimensionId, BlockPos pos) {
        if (!(context.getPlayer() instanceof ServerPlayerEntity player)) {
            return;
        }

        Identifier dim;
        try {
            dim = new Identifier(dimensionId);
        } catch (IllegalArgumentException ignored) {
            player.sendMessage(Text.literal("Noetic Bookshelf: invalid dimension id from keybind data"), false);
            return;
        }

        RegistryKey<net.minecraft.world.World> worldKey = RegistryKey.of(RegistryKeys.WORLD, dim);
        ServerWorld shelfWorld = player.getServer() == null ? null : player.getServer().getWorld(worldKey);
        if (shelfWorld == null) {
            player.sendMessage(Text.literal("Noetic Bookshelf: target dimension is unavailable"), false);
            return;
        }

        shelfWorld.getChunk(pos);
        var be = shelfWorld.getBlockEntity(pos);
        if (!(be instanceof BlockEntityNoeticBookshelf shelf)) {
            player.sendMessage(Text.literal(String.format("Noetic Bookshelf missing at %s in %s", pos.toShortString(), dimensionId)), false);
            return;
        }

        String shelfKeyId = shelf.getKeybindId();
        String displayCode = shelf.getDisplayCode();
        boolean hasStoredIota = shelf.hasStoredIota();
        String msg = String.format(
            "Noetic Bookshelf in %s @ %s — keyId=%s, code=%s, hasStoredIota=%s",
            dimensionId,
            pos.toShortString(),
            shelfKeyId == null ? "<none>" : shelfKeyId,
            displayCode == null ? "<none>" : displayCode,
            hasStoredIota ? "yes" : "no"
        );

        if (keybindId != null && !keybindId.isBlank() && shelfKeyId != null && !shelfKeyId.isBlank() && !keybindId.equals(shelfKeyId)) {
            msg += " [warning: keybind id mismatch]";
        }

        player.sendMessage(Text.literal(msg), false);
    }
}
