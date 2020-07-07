package lifegame.prio

import lifegame.PrioAlgo
import lifegame.container.Carrier
import lifegame.container.RoundState
import lifegame.prio.base.PrioAction
import lifegame.prio.base.PrioActionSubType
import lifegame.prio.base.PrioActionType

class PrioMolecules (
        private val state: RoundState,
        private val algo: PrioAlgo
) {
    fun prioritizeMolecules(getMoleculeActions: List<PrioAction>) {

        addIfWeNeedMoleculesOfThatTypeForSamples(getMoleculeActions)

        addPrioIfWeAlreadyHaveMoleculesForTheSample(getMoleculeActions)
    }

    private fun addIfWeNeedMoleculesOfThatTypeForSamples(possibleActions: List<PrioAction>) {

        possibleActions
                .filter { numNeededForAllSamples(it.prioActionSubType) > numCarried(it.prioActionSubType) }
                .forEach { it.prioChange("typeNeeded",10) }

    }

    private fun addPrioIfWeAlreadyHaveMoleculesForTheSample(possibleActions: List<PrioAction>) {
        possibleActions
                .forEach { it.prioChange("existingForSample", getNumMoleculesNeededOfTypeForAllCarriedSamples(it.prioActionSubType) * 10) }
    }

    private fun numCarried(prioActionSubType: PrioActionSubType) =
            state.me.getStorageOfType(prioActionSubType.toTypeString())

    private fun numNeededForAllSamples(prioActionSubType: PrioActionSubType) =
            state.samples
                    .filter { it.carriedBy == Carrier.ME && algo.diagnosedSamples.contains(it.sampleId) }
                    .map { it.getCostOfType(prioActionSubType.toTypeString()) }
                    .sum()

    // for a given type, how many more molecules in total do we need to complete all samples?
    private fun getNumMoleculesNeededOfTypeForAllCarriedSamples(subType: PrioActionSubType) =
            state.samples
                    .filter { it.carriedBy == Carrier.ME && algo.diagnosedSamples.contains(it.sampleId) }
                    .map { it.getCostOfType(subType.toTypeString()) - state.me.getStorageOfType(subType.toTypeString()) }
                    .filter { it > 0 }
                    .sum()
}