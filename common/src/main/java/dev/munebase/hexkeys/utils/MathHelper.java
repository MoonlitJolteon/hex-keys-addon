package dev.munebase.hexkeys.utils;

import net.minecraft.util.math.BlockPos;

public class MathHelper
{
	public static double distanceBetweenBlockPos(BlockPos a, BlockPos b) {
		double x = Math.pow(a.getX() - b.getX(), 2);
		double y = Math.pow(a.getY() - b.getY(), 2);
		double z = Math.pow(a.getZ() - b.getZ(), 2);

		return Math.sqrt(x + y + z);
	}
}
