package dev.munebase.hexkeys.casting.iotas;

import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.IotaType;
import at.petrak.hexcasting.api.utils.HexUtils;
import dev.munebase.hexkeys.registry.HexkeysIotaTypeRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MindscapeIota extends Iota {
	public MindscapeIota(@NotNull ServerPlayerEntity e) {
		super(HexkeysIotaTypeRegistry.MINDSCAPE, e);
	}

	public ServerPlayerEntity getPlayer() {
		return (ServerPlayerEntity) this.payload;
	}

	@Override
	public boolean toleratesOther(Iota that) {
		return typesMatch(this, that)
				&& that instanceof MindscapeIota dent
				&& this.getPlayer() == dent.getPlayer();
	}

	@Override
	public boolean isTruthy() {
		return true;
	}

	@Override
	public @NotNull
	NbtCompound serialize() {
		var out = new NbtCompound();
		out.putUuid("uuid", this.getPlayer().getUuid());
		out.putString("name", this.getPlayer().getEntityName());
		return out;
	}

	@Override
	public Text display() {
		return Text.literal(this.getPlayer().getEntityName() + "'s Library").getWithStyle(Style.EMPTY.withColor(Formatting.GOLD)).get(0);
	}

	public static IotaType<MindscapeIota> TYPE = new IotaType<>() {
		@Nullable
		@Override
		public MindscapeIota deserialize(NbtElement tag, ServerWorld world) throws IllegalArgumentException
		{
			var ctag = HexUtils.downcast(tag, NbtCompound.TYPE);
			UUID uuid = ctag.getUuid("uuid");
			if (uuid == null) {
				return null;
			}
			var entity = world.getEntity(uuid);
			if (entity == null || !(entity instanceof ServerPlayerEntity)) {
				return null;
			}
			return new MindscapeIota((ServerPlayerEntity) entity);
		}

		@Override
		public Text display(NbtElement tag)
		{
			if (!(tag instanceof NbtCompound ctag)) {
				return Text.translatable("hexcasting.spelldata.entity.whoknows");
			}
			if (!ctag.contains("name", NbtElement.STRING_TYPE)) {
				return Text.translatable("hexcasting.spelldata.entity.whoknows");
			}
			var name = ctag.getString("name");
			return Text.literal(name + "'s Library").getWithStyle(Style.EMPTY.withColor(Formatting.GOLD)).get(0);
		}

		@Override
		public int color() {
			return 0xff_55ffff;
		}
	};
}