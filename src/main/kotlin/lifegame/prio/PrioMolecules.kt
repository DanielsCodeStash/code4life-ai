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

        addPrioOfEnemyNeedsItTo(getMoleculeActions)

        addPrioIfLowOnType(getMoleculeActions)
    }

    private fun addPrioIfLowOnType(possibleActions: List<PrioAction>) {
        possibleActions
                .filter { weNeedTypeForSamples(it) }
                .filter { state.storage.getAvailableByType(it.prioActionSubType.toTypeString()) < 2}
                .forEach { it.prioChange("lowInStock", 1) }
    }

    private fun enemySamples() = state.samples
            .filter { it.carriedBy == Carrier.ENEMY }
            .toList()

    private fun addPrioOfEnemyNeedsItTo(possibleActions: List<PrioAction>) {
        possibleActions
                .filter {enemyNeedMoleculeOfType(possibleActions, it.prioActionSubType)}
                .forEach { it.prioChange("enemyNeed", 1) }
    }

    private fun enemyNeedMoleculeOfType(possibleActions: List<PrioAction>, type: PrioActionSubType): Boolean {
        return state.samples
                .filter { it.carriedBy == Carrier.ENEMY }
                .any{it.getCostOfType(type.toTypeString()) > 0}
    }

    private fun addIfWeNeedMoleculesOfThatTypeForSamples(possibleActions: List<PrioAction>) {

        possibleActions
                .filter { weNeedTypeForSamples(it) }
                .forEach { it.prioChange("typeNeeded",10) }

    }

    private fun weNeedTypeForSamples(action: PrioAction) =
            numNeededForAllSamples(action.prioActionSubType) > numCarried(action.prioActionSubType)

    private fun addPrioIfWeAlreadyHaveMoleculesForTheSample(possibleActions: List<PrioAction>) {
        possibleActions
                .forEach { it.prioChange("existingForSample", getNumMoleculesNeededOfTypeForAllCarriedSamples(it.prioActionSubType) * 1) }
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