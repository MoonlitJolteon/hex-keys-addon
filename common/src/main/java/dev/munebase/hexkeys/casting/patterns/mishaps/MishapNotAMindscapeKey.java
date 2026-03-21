package dev.munebase.hexkeys.casting.patterns.mishaps;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MishapNotAMindscapeKey extends Mishap
{
	@NotNull
	@Override
	public FrozenPigment accentColor(@NotNull CastingEnvironment ctx, @NotNull Mishap.Context errorCtx)
	{
		Intrinsics.checkNotNullParameter(ctx, "ctx");
		Intrinsics.checkNotNullParameter(errorCtx, "errorCtx");
		return this.dyeColor(DyeColor.BROWN);
	}

	@NotNull
	@Override
	public Text errorMessage(@NotNull CastingEnvironment castingContext, @NotNull Mishap.Context context)
	{
		Text errorText;
		errorText = this.error("not_a_mindscape_key");
		return errorText;
	}

	@Override
	public void execute(@NotNull CastingEnvironment castingContext, @NotNull Mishap.Context context, @NotNull List<Iota> list)
	{

	}
}
