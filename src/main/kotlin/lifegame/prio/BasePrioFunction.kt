package lifegame.prio

import lifegame.PrioAlgo
import lifegame.container.Carrier
import lifegame.prio.base.PrioAction
import lifegame.prio.base.PrioActionType


fun prioritize(possibleActions: List<PrioAction>, algo: PrioAlgo): List<PrioAction> {

    setBasePrio(possibleActions, algo)

    PrioMolecules(algo.state, algo).prioritizeMolecules(possibleActions.filter { it.prioActionType == PrioActionType.GET_MOLECULE })

    PrioNewSamples(algo.state, algo).prioritizeNewSamples(possibleActions.filter { it.prioActionType == PrioActionType.GET_NEW_SAMPLE })

    PrioProduceMedicine(algo.state, algo).prioritizeProduceMedicine(possibleActions.filter { it.prioActionType == PrioActionType.PRODUCE_MEDICINE })

    if(carryingTwoUndiagnosedSamples(algo)) {
        possibleActions
                .filter { it.prioActionType == PrioActionType.DIAGNOSE_SAMPLE }
                .forEach { it.prioChange("twoUndiagnosed", 5) }
    }


    PrioDistance(algo.state, algo).addDistanceFactor(possibleActions)

    return possibleActions
}

fun carryingTwoUndiagnosedSamples(algo: PrioAlgo): Boolean {

    if(algo.numCarriedSamples == 3) {
        return algo.state.samples
                .filter { it.carriedBy == Carrier.ME }
                .filter { algo.diagnosedSamples.contains(it.sampleId) }
                .size == 1
    }
    return false
}

fun setBasePrio(possibleActions: List<PrioAction>, algo: PrioAlgo) {

    setBasePrioForActionTyp(possibleActions, PrioActionType.WAIT, -100)
    setBasePrioForActionTyp(possibleActions, PrioActionType.UPLOAD_SAMPLE, 20)
    setBasePrioForActionTyp(possibleActions, PrioActionType.DIAGNOSE_SAMPLE, 110)
    setBasePrioForActionTyp(possibleActions, PrioActionType.GET_DIAGNOSED_SAMPLE, 15)
    setBasePrioForActionTyp(possibleActions, PrioActionType.PRODUCE_MEDICINE, 120)
    setBasePrioForActionTyp(possibleActions, PrioActionType.GET_MOLECULE, 70)

}

fun setBasePrioForActionTyp(possibleActions: List<PrioAction>, actionType: PrioActionType, baseValue: Int) = possibleActions
        .filter {it.prioActionType == actionType}
        .forEach{it.prio = baseValue}










