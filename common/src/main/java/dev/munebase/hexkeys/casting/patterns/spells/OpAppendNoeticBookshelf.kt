package dev.munebase.hexkeys.casting.patterns.spells

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import dev.munebase.hexkeys.blocks.BlockEntityNoeticBookshelf
import dev.munebase.hexkeys.casting.patterns.mishaps.MishapNoeticBookshelfEmpty
import dev.munebase.hexkeys.casting.patterns.mishaps.MishapNoeticBookshelfExpected
import net.minecraft.util.math.BlockPos

object OpAppendNoeticBookshelf : SpellAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val pos = args.getBlockPos(0, argc)
        val value = ListIota(args.getList(1, argc))

        env.assertPosInRange(pos)

        val shelf = env.world.getBlockEntity(pos) as? BlockEntityNoeticBookshelf
            ?: throw MishapNoeticBookshelfExpected(pos)

        val existing = shelf.getStoredIota(env.world)
        if (existing == null) {
            throw MishapNoeticBookshelfEmpty(pos)
        }
        val existingList = existing as ListIota

        val combined = mutableListOf<Iota>()
        combined.addAll(existingList.list.toList())
        combined.addAll(value.list.toList())

        val combinedList = ListIota(combined)
        val totalElements = NoeticBookshelfCostUtils.countTotalElements(value)
        val cost = totalElements * NoeticBookshelfCostUtils.APPEND_COST_PER_ELEMENT
        return SpellAction.Result(Spell(pos, combinedList), cost, listOf())
    }

    private data class Spell(
        val pos: BlockPos,
        val value: ListIota
    ) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val shelf = env.world.getBlockEntity(pos) as? BlockEntityNoeticBookshelf ?: return
            shelf.setStoredIota(value)
        }
    }
}
