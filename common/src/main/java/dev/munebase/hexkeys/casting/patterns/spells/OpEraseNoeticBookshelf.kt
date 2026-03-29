package dev.munebase.hexkeys.casting.patterns.spells

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.munebase.hexkeys.blocks.BlockEntityNoeticBookshelf
import dev.munebase.hexkeys.casting.patterns.mishaps.MishapNoeticBookshelfExpected
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

object OpEraseNoeticBookshelf : SpellAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val pos = args.getBlockPos(0, argc)
        env.assertPosInRange(pos)

        val shelf = env.world.getBlockEntity(pos) as? BlockEntityNoeticBookshelf
            ?: throw MishapNoeticBookshelfExpected(pos)

        val stored = shelf.getStoredIota(env.world)
        val totalElements = NoeticBookshelfCostUtils.countTotalElements(stored)
        val eraseCost = totalElements * NoeticBookshelfCostUtils.ERASE_COST_PER_ELEMENT

        val caster = env.castingEntity as? ServerPlayerEntity
        return SpellAction.Result(Spell(pos, caster), eraseCost, listOf())
    }

    private data class Spell(
        val pos: BlockPos,
        val caster: ServerPlayerEntity?
    ) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val shelf = env.world.getBlockEntity(pos) as? BlockEntityNoeticBookshelf ?: return
            shelf.clearStoredIota()

            if (caster != null) {
                caster.damage(caster.damageSources.magic(), 1.0f)
            }
        }
    }
}
