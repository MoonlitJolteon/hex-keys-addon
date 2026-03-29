package dev.munebase.hexkeys.casting.patterns.spells

import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicBookshelf
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getList
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.mishaps.MishapOthersName
import dev.munebase.hexkeys.blocks.BlockNoeticBookshelf
import dev.munebase.hexkeys.blocks.BlockEntityNoeticBookshelf
import dev.munebase.hexkeys.blocks.HexkeysBlocks
import dev.munebase.hexkeys.casting.patterns.mishaps.MishapBookshelfNotEmpty
import dev.munebase.hexkeys.casting.patterns.mishaps.MishapNoeticCompatibleBookshelfExpected
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

object OpWriteNoeticBookshelf : SpellAction {
    override val argc = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val pos = args.getBlockPos(0, argc)
        val value = ListIota(args.getList(1, argc))

        env.assertPosInRange(pos)

        val blockState = env.world.getBlockState(pos)
        val blockEntity = env.world.getBlockEntity(pos)
        when {
            blockEntity is BlockEntityNoeticBookshelf -> {
                if (blockEntity.hasStoredIota()) {
                    throw MishapBookshelfNotEmpty(pos)
                }
            }
            blockState.block is BlockAkashicBookshelf -> {
                val akashicShelf = blockEntity as? BlockEntityAkashicBookshelf
                    ?: throw MishapNoeticCompatibleBookshelfExpected(pos)
                if (!isAkashicShelfEmpty(blockState, akashicShelf)) {
                    throw MishapBookshelfNotEmpty(pos)
                }
            }
            else -> throw MishapNoeticCompatibleBookshelfExpected(pos)
        }

        val casterPlayer = env.castingEntity as? ServerPlayerEntity
        val trueName = MishapOthersName.Companion.getTrueNameFromDatum(value, casterPlayer as PlayerEntity?)
        if (trueName != null) {
            throw MishapOthersName(trueName)
        }

        val totalElements = NoeticBookshelfCostUtils.countTotalElements(value)
        val cost = NoeticBookshelfCostUtils.BASE_WRITE_COST +
            (totalElements * NoeticBookshelfCostUtils.WRITE_COST_PER_ELEMENT)
        return SpellAction.Result(Spell(pos, value), cost, listOf())
    }

    private data class Spell(
        val pos: BlockPos,
        val value: Iota
    ) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val world = env.world
            var shelf = world.getBlockEntity(pos) as? BlockEntityNoeticBookshelf
            if (shelf == null) {
                val state = world.getBlockState(pos)
                if (state.block is BlockAkashicBookshelf) {
                    val akashicShelf = world.getBlockEntity(pos) as? BlockEntityAkashicBookshelf ?: return
                    if (!isAkashicShelfEmpty(state, akashicShelf)) {
                        return
                    }

                    var noeticState = HexkeysBlocks.NOETIC_BOOKSHELF_BLOCK.get().defaultState
                        .with(BlockNoeticBookshelf.HAS_BOOKS, false)
                    if (state.contains(BlockAkashicBookshelf.FACING)) {
                        noeticState = noeticState.with(BlockNoeticBookshelf.FACING, state.get(BlockAkashicBookshelf.FACING))
                    }
                    world.setBlockState(pos, noeticState, 3)
                    // Register the keybind on the server (and let DynamicKeybinds sync to client)
                    BlockNoeticBookshelf.initializeNoeticKeybind(world, pos)
                    shelf = world.getBlockEntity(pos) as? BlockEntityNoeticBookshelf
                }
            }

            shelf ?: return
            shelf.setStoredIota(value)
        }
    }

    private fun isAkashicShelfEmpty(state: net.minecraft.block.BlockState, shelf: BlockEntityAkashicBookshelf): Boolean {
        if (state.contains(BlockAkashicBookshelf.HAS_BOOKS) && state.get(BlockAkashicBookshelf.HAS_BOOKS)) {
            return false
        }
        return shelf.pattern == null && shelf.iotaTag == null
    }
}
