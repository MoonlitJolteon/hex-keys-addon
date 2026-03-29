package dev.munebase.hexkeys.casting.environment;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironmentComponent;
import dev.munebase.hexkeys.blocks.BlockEntityNoeticBookshelf;
import dev.munebase.hexkeys.blocks.HexkeysBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class NoeticBookshelfAmbitComponent implements CastingEnvironmentComponent.IsVecInRange {
    private static final NoeticBookshelfAmbitKey KEY = new NoeticBookshelfAmbitKey();
    private final CastingEnvironment env;

    public NoeticBookshelfAmbitComponent(CastingEnvironment env) {
        this.env = env;
    }

    @Override
    public Key<?> getKey() {
        return KEY;
    }

    @Override
    public boolean onIsVecInRange(Vec3d vec, boolean current) {
        if (current) {
            return true;
        }

        BlockPos pos = BlockPos.ofFloored(vec);
        var blockEntity = env.getWorld().getBlockEntity(pos);
        if (blockEntity instanceof BlockEntityNoeticBookshelf) {
            return true;
        }

        return env.getWorld().getBlockState(pos).isOf(HexkeysBlocks.NOETIC_BOOKSHELF_BLOCK.get());
    }

    public static class NoeticBookshelfAmbitKey implements CastingEnvironmentComponent.Key<CastingEnvironmentComponent.IsVecInRange> {
    }
}
