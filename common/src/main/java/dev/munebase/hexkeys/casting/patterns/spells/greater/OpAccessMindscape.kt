package dev.munebase.hexkeys.casting.patterns.spells.greater

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import dev.munebase.hexkeys.inventories.KleinInventory
import dev.munebase.hexkeys.utils.DimensionHelper
import net.minecraft.server.network.ServerPlayerEntity

class OpAccessMindscape : SpellAction {
    override val argc = 0
    val creationCost = MediaConstants.CRYSTAL_UNIT * (64 * 5) // 5 stacks
    val transportCost = MediaConstants.CRYSTAL_UNIT
    val returnCost = 0

    override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>>? {
        val player: ServerPlayerEntity = ctx.caster
        var cost = 0
        if (DimensionHelper.isInMindscape(player))
            cost = returnCost
        else if (DimensionHelper.mindscapeExists(player.uuid, player.server))
            cost = transportCost
        else
            cost = creationCost

        return Triple(
            Spell(player),
            cost,
            listOf(ParticleSpray.burst(ctx.caster.pos, 1.0))
        )
    }

    private data class Spell(val player: ServerPlayerEntity) : RenderedSpell {
        override fun cast(ctx: CastingContext) {
            DimensionHelper.FlipDimension(player, player.server)
        }

    }

}