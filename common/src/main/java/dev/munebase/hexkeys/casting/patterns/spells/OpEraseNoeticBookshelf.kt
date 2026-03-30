package dev.munebase.hexkeys.casting.patterns.spells

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

object OpEraseNoeticBookshelf : SpellAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val target = NoeticBookshelfTargetResolver.resolve(args, 0, argc, env)

        val stored = target.shelf.getStoredIota(target.world)
        val totalElements = NoeticBookshelfCostUtils.countTotalElements(stored)
        val eraseCost = totalElements * NoeticBookshelfCostUtils.ERASE_COST_PER_ELEMENT

        val caster = env.castingEntity as? ServerPlayerEntity
        return SpellAction.Result(Spell(target.world.registryKey.value, target.pos, caster), eraseCost, listOf())
    }

    private data class Spell(
        val dimension: Identifier,
        val pos: BlockPos,
        val caster: ServerPlayerEntity?
    ) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val world = NoeticBookshelfTargetResolver.resolveWorld(env.world, dimension) ?: return
            val shelf = world.getBlockEntity(pos) as? dev.munebase.hexkeys.blocks.BlockEntityNoeticBookshelf ?: return
            shelf.clearStoredIota()

            if (caster != null) {
                caster.damage(caster.damageSources.magic(), 1.0f)
            }
        }
    }
}
