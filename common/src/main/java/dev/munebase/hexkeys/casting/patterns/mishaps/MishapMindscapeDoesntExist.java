package dev.munebase.hexkeys.casting.patterns.mishaps;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.mishaps.Mishap;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MishapMindscapeDoesntExist extends Mishap
{
	@NotNull
	@Override
	public FrozenColorizer accentColor(@NotNull CastingContext ctx, @NotNull Mishap.Context errorCtx)
	{
		Intrinsics.checkNotNullParameter(ctx, "ctx");
		Intrinsics.checkNotNullParameter(errorCtx, "errorCtx");
		return this.dyeColor(DyeColor.BROWN);
	}

	@NotNull
	@Override
	public Text errorMessage(@NotNull CastingContext castingContext, @NotNull Mishap.Context context)
	{
		Text errorText;
		errorText = this.error("mindscape_doesnt_exist");
		return errorText;
	}

	@Override
	public void execute(@NotNull CastingContext castingContext, @NotNull Mishap.Context context, @NotNull List<Iota> list)
	{

	}
}
