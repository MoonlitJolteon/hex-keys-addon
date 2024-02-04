package dev.munebase.hexkeys.utils;

import dev.munebase.hexkeys.Hexkeys;
import net.minecraft.util.Identifier;

import java.util.Locale;

public class ResourceLocHelper
{
	public static Identifier prefix(String path)
	{
		return new Identifier(Hexkeys.MOD_ID, path.toLowerCase(Locale.ROOT));
	}
}
