package lifegame.prio

import lifegame.PrioAlgo
import lifegame.container.Carrier
import lifegame.container.RoundState
import lifegame.prio.base.PrioAction
import lifegame.prio.base.PrioActionSubType
import lifegame.util.getMoleculeTypes
import kotlin.math.roundToInt

class PrioNewSamples(
        private val state: RoundState
) {
    fun prioritizeNewSamples(getNewSampleActions: List<PrioAction>) {

        val baseSamplePrio = 90
        val maxSamplePrio = 110
        val minSamplePrio = 80

        val breakpointForRank2 = 7
        val breakpointForRank3 = 10

        getNewSampleActions
                .forEach { it.prio = baseSamplePrio }

        val totalExpertize = getMoleculeTypes()
                .map { state.me.getExpertizeOfType(it) }
                .sum()

        getNewSampleActions
                .filter { it.prioActionSubType == PrioActionSubType.RANK_1 }
                .forEach { it.prioChange("expertizeChange", breakpointForRank2 - totalExpertize * 2) }

        getNewSampleActions
                .filter { it.prioActionSubType == PrioActionSubType.RANK_3 }
                .forEach { it.prioChange("expertizeChange", (-20 + (totalExpertize * 1.5)).roundToInt()) }


        val carriedSampleRanks = state.mySamples()
                .map { it.rank }
                .toList()

        getNewSampleActions
                .filter { !carriedSampleRanks.contains(it.prioActionSubType.toRankString().toInt()) }
                .forEach { it.prioChange("notCarriedRank", 7) }

        getNewSampleActions
                .forEach {
                    if (it.prio > maxSamplePrio) it.prio = maxSamplePrio
                    if (it.prio < minSamplePrio) it.prio = minSamplePrio
                }
    }
}

