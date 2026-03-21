package dev.munebase.hexkeys.casting.patterns.spells.greater;

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.munebase.hexkeys.casting.patterns.mishaps.MishapCasterAndCastingItemsOnly
import dev.munebase.hexkeys.inventories.KleinInventory
import dev.munebase.hexkeys.worldData.KleinStorageData
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

class OpKleinChest : SpellAction
{
    override val argc = 0
    val baseCost = MediaConstants.SHARD_UNIT
    val costPerSlot = MediaConstants.DUST_UNIT * 0.25

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val player = env.castingEntity as? ServerPlayerEntity ?: return SpellAction.Result(Spell(null, null), 0L, listOf())
        val kleinInventory = KleinStorageData.getServerState(env.world.server).getKleinInventory(player.uuid)
        var usedSlots = 0
        for (i in 0 until kleinInventory.size()) {
            if(!kleinInventory.getStack(i).isEmpty) usedSlots += 1
        }
        return SpellAction.Result(
            Spell(player, kleinInventory),
            (baseCost + (costPerSlot * usedSlots)).toLong(),
            listOf(ParticleSpray.burst(player.pos, 1.0))
        )
    }

    private data class Spell(val player: ServerPlayerEntity?, val kleinInventory: KleinInventory?): RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val castPlayer = player ?: return
            val inv = kleinInventory ?: return
            if (env.castingEntity is ServerPlayerEntity) {
                castPlayer.openHandledScreen(SimpleNamedScreenHandlerFactory( { syncId: Int, inventory: PlayerInventory, _: PlayerEntity ->
                    GenericContainerScreenHandler.createGeneric9x6(syncId, inventory, inv)
                }, Text.translatable("container.kleins_chest")))

            } else {
                throw MishapCasterAndCastingItemsOnly()
            }
        }

    }
}
