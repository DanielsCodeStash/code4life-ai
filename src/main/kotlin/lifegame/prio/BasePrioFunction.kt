package lifegame.prio

import lifegame.container.RoundState
import lifegame.prio.base.PrioAction
import lifegame.prio.base.PrioActionType


class BasePrioFunction(private val state: RoundState) {

    fun prioritize(actions: List<PrioAction>): List<PrioAction> {

        // base prio
        setBasePrio(actions)

        // general prio functions
        PrioMolecules(state).prio(getActionsOfType(actions, PrioActionType.GET_MOLECULE))
        PrioNewSamples(state).prio(getActionsOfType(actions, PrioActionType.GET_NEW_SAMPLE))
        PrioProduceMedicine(state).prio(getActionsOfType(actions, PrioActionType.PRODUCE_MEDICINE))
        PrioDiagnosing(state).prio(getActionsOfType(actions, PrioActionType.DIAGNOSE_SAMPLE))

        // distance factor
        PrioDistance(state).addDistanceFactor(actions)

        return actions
    }

    private fun getActionsOfType(actions: List<PrioAction>, type: PrioActionType): List<PrioAction> = actions
            .filter { it.prioActionType == type }
            .toList()

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









