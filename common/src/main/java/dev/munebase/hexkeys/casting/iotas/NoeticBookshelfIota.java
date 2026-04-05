package dev.munebase.hexkeys.casting.iotas;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.utils.HexUtils;
import dev.munebase.hexkeys.registry.HexkeysIotaTypeRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NoeticBookshelfIota extends Iota {
    public static final String PREFIX = "Noetic Bookshelf ";

    public record Ref(Identifier dimension, BlockPos pos, @Nullable String keybindId, String displayCode) {
    }

    public NoeticBookshelfIota(@NotNull Ref ref) {
        super(HexkeysIotaTypeRegistry.NOETIC_BOOKSHELF, ref);
    }

    public Ref getRef() {
        return (Ref) this.payload;
    }

    @Override
    public boolean toleratesOther(Iota that) {
        return typesMatch(this, that)
            && that instanceof NoeticBookshelfIota other
            && this.getRef().dimension().equals(other.getRef().dimension())
            && this.getRef().pos().equals(other.getRef().pos());
    }

    @Override
    public boolean isTruthy() {
        return true;
    }

    @Override
    public @NotNull NbtCompound serialize() {
        Ref ref = this.getRef();
        NbtCompound out = new NbtCompound();
        out.putString("dimension", ref.dimension().toString());
        out.putLong("pos", ref.pos().asLong());
        out.putString("display_code", ref.displayCode());
        if (ref.keybindId() != null && !ref.keybindId().isBlank()) {
            out.putString("keybind_id", ref.keybindId());
        }
        return out;
    }

    @Override
    public Text display() {
        return formatDisplay(this.getRef().displayCode());
    }

    private static Text formatDisplay(String code) {
        return Text.literal(PREFIX + code)
            .getWithStyle(Style.EMPTY.withColor(Formatting.GOLD)).get(0);
    }

    public static final IotaType<NoeticBookshelfIota> TYPE = new IotaType<>() {
        @Nullable
        @Override
        public NoeticBookshelfIota deserialize(NbtElement tag, ServerWorld world) throws IllegalArgumentException {
            NbtCompound ctag = HexUtils.downcast(tag, NbtCompound.TYPE);
            if (!ctag.contains("dimension", NbtElement.STRING_TYPE) || !ctag.contains("pos", NbtElement.LONG_TYPE)) {
                return null;
            }

            Identifier dimension;
            try {
                dimension = new Identifier(ctag.getString("dimension"));
            } catch (IllegalArgumentException ignored) {
                return null;
            }

            String displayCode = ctag.contains("display_code", NbtElement.STRING_TYPE)
                ? ctag.getString("display_code")
                : "??????";
            String keybindId = ctag.contains("keybind_id", NbtElement.STRING_TYPE)
                ? ctag.getString("keybind_id")
                : null;

            Ref ref = new Ref(dimension, BlockPos.fromLong(ctag.getLong("pos")), keybindId, displayCode);
            return new NoeticBookshelfIota(ref);
        }

        @Override
        public Text display(NbtElement tag) {
            if (!(tag instanceof NbtCompound ctag)) {
                return formatDisplay("??????");
            }

            String code = ctag.contains("display_code", NbtElement.STRING_TYPE)
                ? ctag.getString("display_code")
                : "??????";
            return formatDisplay(code);
        }

        @Override
        public int color() {
            return 0xff_ffaa00;
        }
    };
}
