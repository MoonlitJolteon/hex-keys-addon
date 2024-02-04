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
    override val argc = 0;
    val baseCost = MediaConstants.CRYSTAL_UNIT

    override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>>? {
        val player: ServerPlayerEntity = ctx.caster;
        return Triple(
            Spell(player),
            baseCost,
            listOf(ParticleSpray.burst(ctx.caster.pos, 1.0))
        )
    }

    private data class Spell(val player: ServerPlayerEntity) : RenderedSpell {
        override fun cast(ctx: CastingContext) {
            DimensionHelper.FlipDimension(player, player.server)
        }

    }

}