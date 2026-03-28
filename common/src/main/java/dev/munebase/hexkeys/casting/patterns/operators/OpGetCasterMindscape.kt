package dev.munebase.hexkeys.casting.patterns.operators

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.munebase.hexkeys.casting.iotas.MindscapeIota
import net.minecraft.server.network.ServerPlayerEntity

object OpGetCasterMindscape : ConstMediaAction {
    override val argc = 0
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val caster = env.castingEntity as? ServerPlayerEntity ?: return emptyList()
        env.assertEntityInRange(caster)
        return listOf(MindscapeIota(caster))
    }
}