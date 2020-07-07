package lifegame.prio

import lifegame.PrioAlgo
import lifegame.container.Carrier
import lifegame.container.RoundState
import lifegame.prio.base.PrioAction
import lifegame.prio.base.PrioActionSubType
import lifegame.util.getMoleculeTypes

class PrioNewSamples (
        private val state:RoundState,
        private val algo:PrioAlgo
) {
    fun prioritizeNewSamples(getNewSampleActions: List<PrioAction>) {
        val carriedSampleRanks = algo.state.samples
                .filter { it.carriedBy == Carrier.ME }
                .map { it.rank }
                .toList()

        getNewSampleActions
                .forEach { it.prio = 110 }

        getNewSampleActions
                .filter { !carriedSampleRanks.contains(it.prioActionSubType.toRankString().toInt()) }
                .forEach { it.prioChange("notCarriedRank", 10) }

        val totalExpertize = getMoleculeTypes()
                .map { algo.state.me.getExpertizeOfType(it) }
                .sum()

        getNewSampleActions
                .filter { it.prioActionSubType == PrioActionSubType.RANK_1 }
                .forEach { it.prioChange("expertizeMod", Math.max(10-totalExpertize, -10))}

        getNewSampleActions
                .filter { it.prioActionSubType == PrioActionSubType.RANK_3 }
                .forEach { it.prioChange("expertizeMod", Math.min(totalExpertize-10, 10))}


    }
}

