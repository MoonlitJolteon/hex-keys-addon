package dev.munebase.hexkeys.casting.patterns.spells.greater;

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import at.petrak.hexcasting.api.spell.mishaps.Mishap
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
    override val argc = 0;
    val baseCost = MediaConstants.SHARD_UNIT
    val costPerSlot = MediaConstants.DUST_UNIT * 0.25

    override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {

        val player = ctx.caster
        val kleinInventory = KleinStorageData.getServerState(ctx.world.server).getKleinInventory(player.uuid)
        val usedSlots = kleinInventory.stacks.filter { stack -> !stack.isEmpty }.size

        return Triple(
            Spell(player, kleinInventory),
            (baseCost + (costPerSlot * usedSlots)).toInt(),
            listOf(ParticleSpray.burst(ctx.caster.pos, 1.0))
        )
    }

    private data class Spell(val player: ServerPlayerEntity, val kleinInventory: KleinInventory): RenderedSpell {
        override fun cast(ctx: CastingContext) {
            if(ctx.source == CastingContext.CastSource.STAFF || ctx.source == CastingContext.CastSource.PACKAGED_HEX) {
                player.openHandledScreen(SimpleNamedScreenHandlerFactory( { syncId: Int, inventory: PlayerInventory, _: PlayerEntity ->
                    GenericContainerScreenHandler.createGeneric9x6(syncId, inventory, kleinInventory)
                }, Text.translatable("container.kleins_chest")))

            } else {
                throw MishapCasterAndCastingItemsOnly()
            }
        }

    }
}
