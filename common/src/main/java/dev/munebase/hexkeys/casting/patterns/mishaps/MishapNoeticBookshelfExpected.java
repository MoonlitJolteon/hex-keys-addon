package dev.munebase.hexkeys.casting.patterns.mishaps;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MishapNoeticBookshelfExpected extends Mishap
{
	private final BlockPos pos;

	public MishapNoeticBookshelfExpected(BlockPos pos) {
		this.pos = pos;
	}

	@NotNull
	@Override
	public FrozenPigment accentColor(@NotNull CastingEnvironment ctx, @NotNull Mishap.Context errorCtx)
	{
		Intrinsics.checkNotNullParameter(ctx, "ctx");
		Intrinsics.checkNotNullParameter(errorCtx, "errorCtx");
		return this.dyeColor(DyeColor.PURPLE);
	}

	@NotNull
	@Override
	public Text errorMessage(@NotNull CastingEnvironment castingContext, @NotNull Mishap.Context context)
	{
		return this.error("noetic_bookshelf_expected");
	}

	@Override
	public void execute(@NotNull CastingEnvironment castingContext, @NotNull Mishap.Context context, @NotNull List<Iota> list)
	{

	}
}
