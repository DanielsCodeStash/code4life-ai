package lifegame.prio

import lifegame.PrioAlgo
import lifegame.container.Carrier
import lifegame.container.RoundState
import lifegame.prio.base.PrioAction
import lifegame.prio.base.PrioActionSubType
import lifegame.prio.base.PrioActionType

class PrioMolecules(
        private val state: RoundState
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
                .filter { state.storage.getAvailableByType(it.prioActionSubType.toTypeString()) < 2 }
                .forEach { it.prioChange("lowInStock", 1) }
    }

    private fun addPrioOfEnemyNeedsItTo(possibleActions: List<PrioAction>) {
        possibleActions
                .filter { enemyNeedMoleculeOfType(possibleActions, it.prioActionSubType) }
                .forEach { it.prioChange("enemyNeed", 1) }
    }

    private fun enemyNeedMoleculeOfType(possibleActions: List<PrioAction>, subType: PrioActionSubType): Boolean {

        val type = subType.toTypeString()

        return state.enemyDiagnosedSamples()
                .any { it.getCostOfType(type) - state.enemy.getExpertizeOfType(type) > state.enemy.getStorageOfType(type) }
    }

    private fun addIfWeNeedMoleculesOfThatTypeForSamples(possibleActions: List<PrioAction>) {

        possibleActions
                .filter { weNeedTypeForSamples(it) }
                .forEach { it.prioChange("typeNeeded", 30) }

    }

    private fun weNeedTypeForSamples(action: PrioAction) =
            numNeededForAllSamples(action.prioActionSubType) - state.me.getExpertizeOfType(action.prioActionSubType.toTypeString()) > numCarried(action.prioActionSubType)

    private fun addPrioIfWeAlreadyHaveMoleculesForTheSample(possibleActions: List<PrioAction>) {
        possibleActions
                .forEach { it.prioChange("existingForSample", getNumMoleculesNeededOfTypeForAllCarriedSamples(it.prioActionSubType) * 1) }
    }

    private fun numCarried(prioActionSubType: PrioActionSubType) =
            state.me.getStorageOfType(prioActionSubType.toTypeString())

    private fun numNeededForAllSamples(prioActionSubType: PrioActionSubType) =
            state.myDiagnosedSamples()
                    .map { it.getCostOfType(prioActionSubType.toTypeString()) }
                    .sum()

    // for a given type, how many more molecules in total do we need to complete all samples?
    private fun getNumMoleculesNeededOfTypeForAllCarriedSamples(subType: PrioActionSubType) =
            state.myDiagnosedSamples()
                    .map { it.getCostOfType(subType.toTypeString()) - state.me.getStorageOfType(subType.toTypeString()) }
                    .filter { it > 0 }
                    .sum()
}