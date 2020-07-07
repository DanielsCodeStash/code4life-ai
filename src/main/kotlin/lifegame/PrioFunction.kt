package lifegame

import lifegame.container.Carrier


fun prioritize(possibleActions: List<PrioAction>, algo: PrioAlgo): List<PrioAction> {

    prioritizeMolecules(possibleActions, algo)

    possibleActions
            .filter { it.prioActionType == PrioActionType.GET_NEW_SAMPLE }
            .forEach { it.prio += 10 }

    possibleActions
            .filter { it.prioActionType == PrioActionType.DIAGNOSE_SAMPLE }
            .forEach { it.prio += 10 }

    return possibleActions
}

fun prioritizeMolecules(possibleActions: List<PrioAction>, algo: PrioAlgo) {


    addIfWeNeedMoleculesOfThatTypeForSamples(possibleActions, algo)

    addPrioIfWeAlreadyHaveMoleculesForTheSample(possibleActions, algo)
}


fun addIfWeNeedMoleculesOfThatTypeForSamples(possibleActions: List<PrioAction>, algo: PrioAlgo) {

    possibleActions
            .filter { it.prioActionType == PrioActionType.GET_MOLECULE }
            .filter { numNeededForAllSamples(algo, it.prioActionSubType) > numCarried(algo, it.prioActionSubType) }
            .forEach { it.prio += 10 }

}

fun numCarried(algo: PrioAlgo, prioActionSubType: PrioActionSubType) =
        algo.state.me.getStorageOfType(prioActionSubType.toTypeString())

fun numNeededForAllSamples(algo: PrioAlgo, prioActionSubType: PrioActionSubType) =
        algo.state.samples
                .filter { it.carriedBy == Carrier.ME && algo.diagnosedSamples.contains(it.sampleId) }
                .map { it.getCostOfType(prioActionSubType.toTypeString()) }
                .sum()


fun addPrioIfWeAlreadyHaveMoleculesForTheSample(possibleActions: List<PrioAction>, algo: PrioAlgo) {
    possibleActions
            .filter { it.prioActionType == PrioActionType.GET_MOLECULE }
            .forEach { it.prio += getNumMoleculesNeededOfTypeForAllCarriedSamples(algo, it.prioActionSubType) * 10 }
}

// for a given type, how many more molecules in total do we need to complete all samples?
fun getNumMoleculesNeededOfTypeForAllCarriedSamples(algo: PrioAlgo, subType: PrioActionSubType) =
        algo.state.samples
            .filter { it.carriedBy == Carrier.ME && algo.diagnosedSamples.contains(it.sampleId) }
            .map { it.getCostOfType(subType.toTypeString()) - algo.state.me.getStorageOfType(subType.toTypeString()) }
            .filter { it > 0 }
            .sum()




