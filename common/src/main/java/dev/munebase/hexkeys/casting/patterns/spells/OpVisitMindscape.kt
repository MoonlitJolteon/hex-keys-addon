package dev.munebase.hexkeys.casting.patterns.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.spell.ParticleSpray
import at.petrak.hexcasting.api.spell.RenderedSpell
import at.petrak.hexcasting.api.spell.SpellAction
import at.petrak.hexcasting.api.spell.casting.CastingContext
import at.petrak.hexcasting.api.spell.iota.Iota
import dev.munebase.hexkeys.Hexkeys
import dev.munebase.hexkeys.casting.iotas.MindscapeIota
import dev.munebase.hexkeys.casting.patterns.mishaps.MishapMindscapeDoesntExist
import dev.munebase.hexkeys.casting.patterns.mishaps.MishapNotAMindscapeKey
import dev.munebase.hexkeys.utils.DimensionHelper
import dev.munebase.hexkeys.utils.DimensionHelper.NBTKeys
import dev.munebase.hexkeys.utils.PlayerHelper
import dev.munebase.hexkeys.worldData.MindscapeStatus
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity

class OpVisitMindscape : SpellAction {
    override val argc = 1
    val cost = MediaConstants.CRYSTAL_UNIT

    override fun execute(args: List<Iota>, ctx: CastingContext): Triple<RenderedSpell, Int, List<ParticleSpray>>? {
        val player: ServerPlayerEntity = ctx.caster
        val mindscapeOwner = args[0];
        val mindscapeList = MindscapeStatus.getServerState(ctx.world.server)
        val tag: NbtCompound = PlayerHelper.getPersistentTag(player, Hexkeys.IDENTIFIER.toString())
        if(mindscapeOwner.type != MindscapeIota.TYPE) {
            throw MishapNotAMindscapeKey()
        }
        if (mindscapeList.hasMindscape((mindscapeOwner as MindscapeIota).player.uuid)) {
            tag.putString("CURRENT_MINDSCAPE_OWNER_UUID", mindscapeOwner.player.uuidAsString)
        }
        else {
            throw MishapMindscapeDoesntExist()
        }

        return Triple(
            Spell(player, mindscapeOwner),
            cost,
            listOf(ParticleSpray.burst(ctx.caster.pos, 1.0))
        )
    }

    private data class Spell(val player: ServerPlayerEntity, val mindscapeOwner: MindscapeIota) : RenderedSpell {
        override fun cast(ctx: CastingContext) {
            if(DimensionHelper.isInMindscape(player)) {
                var mindNBT = PlayerHelper.getPersistentTag(player, Hexkeys.IDENTIFIER.toString())
                var mindscapePos = DimensionHelper.getMindscapePos(mindscapeOwner.player.uuid, mindNBT.getInt(NBTKeys.MINDSCAPE_VERSION_NUM))
                player.teleport(mindscapePos.x + 0.5, mindscapePos.y + 0.5, mindscapePos.z + 0.5)
            } else {
                DimensionHelper.flipDimension(player, mindscapeOwner.player.uuid, player.server)
            }
        }

    }

}