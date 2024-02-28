package dev.munebase.hexkeys.casting.patterns.operators

import at.petrak.hexcasting.api.spell.ConstMediaAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import dev.munebase.hexkeys.casting.iotas.MindscapeIota

object OpGetCasterMindscape : ConstMediaAction {
    override val argc = 0
    override val isGreat = true;
    override fun execute(args: List<Iota>, ctx: CastingContext): List<Iota> {
        ctx.assertEntityInRange(ctx.caster)
        return listOf(MindscapeIota(ctx.caster));
    }
}