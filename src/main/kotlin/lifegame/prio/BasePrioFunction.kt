package lifegame.prio

import lifegame.PrioAlgo
import lifegame.container.ActionType
import lifegame.container.Carrier
import lifegame.container.Location
import lifegame.prio.base.PrioAction
import lifegame.prio.base.PrioActionSubType
import lifegame.prio.base.PrioActionType
import lifegame.util.getMoleculeTypes
import java.lang.IllegalArgumentException


fun prioritize(possibleActions: List<PrioAction>, algo: PrioAlgo): List<PrioAction> {

    setBasePrio(possibleActions, algo)


    PrioDistance(algo.state, algo).addDistanceFactor(possibleActions)

    PrioMolecules(algo.state, algo).prioritizeMolecules(possibleActions.filter { it.prioActionType == PrioActionType.GET_MOLECULE })

    PrioNewSamples(algo.state, algo).prioritizeNewSamples(possibleActions.filter { it.prioActionType == PrioActionType.GET_NEW_SAMPLE })


    return possibleActions
}

fun setBasePrio(possibleActions: List<PrioAction>, algo: PrioAlgo) {

    setBasePrioForActionTyp(possibleActions, PrioActionType.WAIT, 10)
    setBasePrioForActionTyp(possibleActions, PrioActionType.UPLOAD_SAMPLE, 20)
    setBasePrioForActionTyp(possibleActions, PrioActionType.DIAGNOSE_SAMPLE, 110)
    setBasePrioForActionTyp(possibleActions, PrioActionType.GET_DIAGNOSED_SAMPLE, 15)

}

fun setBasePrioForActionTyp(possibleActions: List<PrioAction>, actionType: PrioActionType, baseValue: Int) = possibleActions
        .filter {it.prioActionType == actionType}
        .forEach{it.prio = baseValue}










