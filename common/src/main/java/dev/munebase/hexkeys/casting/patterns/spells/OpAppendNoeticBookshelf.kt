package dev.munebase.hexkeys.casting.patterns.spells

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import dev.munebase.hexkeys.casting.patterns.mishaps.MishapNoeticBookshelfEmpty
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

object OpAppendNoeticBookshelf : SpellAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val value = ListIota(args.getList(1, argc))
        val target = NoeticBookshelfTargetResolver.resolve(args, 0, argc, env)

        val existing = target.shelf.getStoredIota(target.world)
        if (existing == null) {
            throw MishapNoeticBookshelfEmpty(target.pos)
        }
        val existingList = existing as ListIota

        val combined = mutableListOf<Iota>()
        combined.addAll(existingList.list.toList())
        combined.addAll(value.list.toList())

        val combinedList = ListIota(combined)
        val totalElements = NoeticBookshelfCostUtils.countTotalElements(value)
        val cost = totalElements * NoeticBookshelfCostUtils.APPEND_COST_PER_ELEMENT
        return SpellAction.Result(Spell(target.world.registryKey.value, target.pos, combinedList), cost, listOf())
    }

    private data class Spell(
        val dimension: Identifier,
        val pos: BlockPos,
        val value: ListIota
    ) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val world = NoeticBookshelfTargetResolver.resolveWorld(env.world, dimension) ?: return
            val shelf = world.getBlockEntity(pos) as? dev.munebase.hexkeys.blocks.BlockEntityNoeticBookshelf ?: return
            shelf.setStoredIota(value)
        }
    }
}
