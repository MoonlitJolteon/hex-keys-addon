package dev.munebase.hexkeys.casting.patterns.operators

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.munebase.hexkeys.blocks.BlockEntityNoeticBookshelf
import dev.munebase.hexkeys.casting.iotas.NoeticBookshelfIota
import dev.munebase.hexkeys.casting.patterns.mishaps.MishapNoeticBookshelfExpected

object OpGetNoeticBookshelfRef : ConstMediaAction {
    override val argc = 1
    override val mediaCost = MediaConstants.DUST_UNIT / 4

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val pos = args.getBlockPos(0, argc)
        env.assertPosInRange(pos)

        val shelf = env.world.getBlockEntity(pos) as? BlockEntityNoeticBookshelf
            ?: throw MishapNoeticBookshelfExpected(pos)

        val displayCode = shelf.displayCode ?: "??????"
        val iota = NoeticBookshelfIota(
            NoeticBookshelfIota.Ref(
                env.world.registryKey.value,
                pos.toImmutable(),
                shelf.keybindId,
                displayCode
            )
        )

        return listOf(iota)
    }
}
