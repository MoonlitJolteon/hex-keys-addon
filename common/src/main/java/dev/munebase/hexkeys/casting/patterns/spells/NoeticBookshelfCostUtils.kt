package dev.munebase.hexkeys.casting.patterns.spells

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.misc.MediaConstants

object NoeticBookshelfCostUtils {
    val BASE_WRITE_COST = 8 * MediaConstants.CRYSTAL_UNIT
    val APPEND_COST_PER_ELEMENT = MediaConstants.DUST_UNIT / 8
    val WRITE_COST_PER_ELEMENT = MediaConstants.DUST_UNIT / 8
    val ERASE_COST_PER_ELEMENT = MediaConstants.DUST_UNIT / 2

    fun countTotalElements(iota: Iota?): Int {
        if (iota == null) {
            return 0
        }
        if (iota !is ListIota) {
            return 1
        }

        var total = 0
        for (entry in iota.list) {
            total += countTotalElements(entry)
        }
        return total
    }
}
