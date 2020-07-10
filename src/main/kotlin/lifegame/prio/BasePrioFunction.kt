package lifegame.prio

import lifegame.container.RoundState
import lifegame.prio.base.PrioAction
import lifegame.prio.base.PrioActionType


class BasePrioFunction(private val state: RoundState) {

    fun prioritize(possibleActions: List<PrioAction>): List<PrioAction> {

        setBasePrio(possibleActions)

        PrioMolecules(state).prioritizeMolecules(getActionsOfType(possibleActions, PrioActionType.GET_MOLECULE))

        PrioNewSamples(state).prioritizeNewSamples(getActionsOfType(possibleActions, PrioActionType.GET_NEW_SAMPLE))

        PrioProduceMedicine(state).prioritizeProduceMedicine(getActionsOfType(possibleActions, PrioActionType.PRODUCE_MEDICINE))

        if (carryingTwoUndiagnosedSamples()) {
            possibleActions
                    .filter { it.prioActionType == PrioActionType.DIAGNOSE_SAMPLE }
                    .forEach { it.prioChange("twoUndiagnosed", 5) }
        }

        PrioDistance(state).addDistanceFactor(possibleActions)

        return possibleActions
    }

    private fun getActionsOfType(actions: List<PrioAction>, type: PrioActionType): List<PrioAction> = actions
            .filter { it.prioActionType == type }
            .toList()

    private fun carryingTwoUndiagnosedSamples(): Boolean {

        if (state.numSamplesICarry() == 3) {
            return state.myDiagnosedSamples().size == 1
        }
        return false
    }

    private fun setBasePrio(possibleActions: List<PrioAction>) {

        setBasePrioForActionTyp(possibleActions, PrioActionType.WAIT, -100)
        setBasePrioForActionTyp(possibleActions, PrioActionType.UPLOAD_SAMPLE, 20)
        setBasePrioForActionTyp(possibleActions, PrioActionType.DIAGNOSE_SAMPLE, 110)
        setBasePrioForActionTyp(possibleActions, PrioActionType.GET_DIAGNOSED_SAMPLE, 15)
        setBasePrioForActionTyp(possibleActions, PrioActionType.PRODUCE_MEDICINE, 120)
        setBasePrioForActionTyp(possibleActions, PrioActionType.GET_MOLECULE, 70)

    }

    private fun setBasePrioForActionTyp(possibleActions: List<PrioAction>, actionType: PrioActionType, baseValue: Int) = possibleActions
            .filter { it.prioActionType == actionType }
            .forEach { it.prio = baseValue }

}









