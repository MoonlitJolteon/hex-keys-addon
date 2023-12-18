package dev.munebase.hexkeys.casting.patterns.spells.greater;

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import dev.munebase.hexkeys.worldData.KleinStorageData
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.GenericContainerScreenHandler
import net.minecraft.screen.SimpleNamedScreenHandlerFactory
import net.minecraft.text.Text

class OpKleinChest : SpellAction
{
    override val argc = 0;
    val cost = 2 * MediaConstants.DUST_UNIT

    override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>> {
        return Triple(
            Spell(1),
            cost,
            listOf(ParticleSpray.burst(ctx.caster.pos, 1.0))
        )
    }

    private data class Spell(val iDunnoWhatToDoHere: Int): RenderedSpell {
        override fun cast(ctx: CastingContext) {
            if(ctx.source == CastingContext.CastSource.STAFF || ctx.source == CastingContext.CastSource.PACKAGED_HEX) {
                val player = ctx.caster
                val kleinInventory = KleinStorageData.getServerState(ctx.world.server).getKleinInventory(player.uuid)//(ctx.caster as IKleinInventory).kleinInventory
                player.openHandledScreen(SimpleNamedScreenHandlerFactory( { syncId: Int, inventory: PlayerInventory, _: PlayerEntity ->
                    GenericContainerScreenHandler.createGeneric9x6(syncId, inventory, kleinInventory)
                }, Text.translatable("container.kleins_chest")))

            } else {
                ctx.caster.sendMessage(Text.literal("Bad! Staff and Casting items Only!"))
            }
        }

    }
}
