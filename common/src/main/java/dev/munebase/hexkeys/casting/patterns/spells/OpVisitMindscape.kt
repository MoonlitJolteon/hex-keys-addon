package dev.munebase.hexkeys.casting.patterns.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
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

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val player = env.castingEntity as? ServerPlayerEntity ?: return SpellAction.Result(Spell(null, null), 0L, listOf())
        val mindscapeOwner = args[0]
        val mindscapeList = MindscapeStatus.getServerState(env.world.server)
        val tag: NbtCompound = PlayerHelper.getPersistentTag(player, Hexkeys.IDENTIFIER.toString())
        if(mindscapeOwner.type != MindscapeIota.TYPE) {
            throw MishapNotAMindscapeKey()
        }
        val owner = mindscapeOwner as MindscapeIota
        if (mindscapeList.hasMindscape(owner.player.uuid)) {
            tag.putString("CURRENT_MINDSCAPE_OWNER_UUID", owner.player.uuidAsString)
        }
        else {
            throw MishapMindscapeDoesntExist()
        }

        return SpellAction.Result(
            Spell(player, owner),
            cost,
            listOf(ParticleSpray.burst(player.pos, 1.0))
        )
    }

    private data class Spell(val player: ServerPlayerEntity?, val mindscapeOwner: MindscapeIota?) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val castPlayer = player ?: return
            val owner = mindscapeOwner ?: return
            if(DimensionHelper.isInMindscape(castPlayer)) {
                val mindNBT = PlayerHelper.getPersistentTag(castPlayer, Hexkeys.IDENTIFIER.toString())
                val mindscapePos = DimensionHelper.getMindscapePos(owner.player.uuid, mindNBT.getInt(NBTKeys.MINDSCAPE_VERSION_NUM))
                castPlayer.teleport(mindscapePos.x + 0.5, mindscapePos.y + 0.5, mindscapePos.z + 0.5)
            } else {
                DimensionHelper.flipDimension(castPlayer, owner.player.uuid, castPlayer.server)
            }
        }

    }

}