package dev.munebase.hexkeys.casting.patterns.operators

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.munebase.hexkeys.casting.patterns.spells.NoeticBookshelfTargetResolver

object OpReadNoeticBookshelf : ConstMediaAction {
    override val argc = 1
    override val mediaCost = MediaConstants.DUST_UNIT / 4

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val target = NoeticBookshelfTargetResolver.resolve(args, 0, argc, env)
        val iota = target.shelf.getStoredIota(target.world)
        return listOf(iota ?: NullIota())
    }
}
