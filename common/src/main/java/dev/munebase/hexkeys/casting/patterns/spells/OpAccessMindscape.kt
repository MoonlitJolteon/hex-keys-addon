package dev.munebase.hexkeys.casting.patterns.spells

import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.munebase.hexkeys.Hexkeys
import dev.munebase.hexkeys.utils.DimensionHelper
import dev.munebase.hexkeys.utils.PlayerHelper
import dev.munebase.hexkeys.worldData.MindscapeStatus
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.network.ServerPlayerEntity

class OpAccessMindscape : SpellAction {
    override val argc = 0
    val creationCost = MediaConstants.CRYSTAL_UNIT * 64 // 1 stack
    val transportCost = MediaConstants.CRYSTAL_UNIT
    val returnCost = 0L

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val player = env.castingEntity as? ServerPlayerEntity ?: return SpellAction.Result(Spell(null), 0L, listOf())
        var cost = 0L
        val mindscapeList = MindscapeStatus.getServerState(env.world.server)
        val tag: NbtCompound = PlayerHelper.getPersistentTag(player, Hexkeys.IDENTIFIER.toString())
        if (DimensionHelper.isInMindscape(player))
            cost = returnCost
        else if (mindscapeList.hasMindscape(player.uuid)) {
            cost = transportCost
            tag.putString("CURRENT_MINDSCAPE_OWNER_UUID", player.uuidAsString)
        }
        else {
            cost = creationCost
            tag.putString("CURRENT_MINDSCAPE_OWNER_UUID", player.uuidAsString)
        }

        return SpellAction.Result(
            Spell(player),
            cost,
            listOf(ParticleSpray.burst(player.pos, 1.0))
        )
    }

    private data class Spell(val player: ServerPlayerEntity?) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            if (player != null) {
                DimensionHelper.flipDimension(player, player.server)
            }
        }

    }

}