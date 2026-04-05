package dev.munebase.hexkeys.casting.patterns.spells

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.munebase.hexkeys.blocks.BlockEntityNoeticBookshelf
import dev.munebase.hexkeys.casting.iotas.NoeticBookshelfIota
import dev.munebase.hexkeys.casting.patterns.mishaps.MishapNoeticBookshelfExpected
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object NoeticBookshelfTargetResolver {
    data class Target(
        val world: ServerWorld,
        val pos: BlockPos,
        val shelf: BlockEntityNoeticBookshelf,
        val usedReference: Boolean
    )

    fun resolve(args: List<Iota>, argIndex: Int, argc: Int, env: CastingEnvironment): Target {
        val arg = args[argIndex]
        if (arg is NoeticBookshelfIota) {
            val ref = arg.getRef()
            val worldKey = RegistryKey.of(RegistryKeys.WORLD, ref.dimension())
            val targetWorld = env.world.server?.getWorld(worldKey)
                ?: throw MishapNoeticBookshelfExpected(ref.pos())

            val pos = ref.pos()
            val shelf = targetWorld.getBlockEntity(pos) as? BlockEntityNoeticBookshelf
                ?: throw MishapNoeticBookshelfExpected(pos)
            return Target(targetWorld, pos, shelf, true)
        }

        val pos = args.getBlockPos(argIndex, argc)
        env.assertPosInRange(pos)
        val shelf = env.world.getBlockEntity(pos) as? BlockEntityNoeticBookshelf
            ?: throw MishapNoeticBookshelfExpected(pos)
        return Target(env.world, pos, shelf, false)
    }

    fun resolveWorld(world: ServerWorld, dimension: net.minecraft.util.Identifier): ServerWorld? {
        val worldKey = RegistryKey.of(RegistryKeys.WORLD, dimension)
        return world.server?.getWorld(worldKey)
    }
}
