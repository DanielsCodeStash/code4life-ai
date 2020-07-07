package lifegame

import lifegame.container.Carrier
import lifegame.container.Location
import lifegame.util.getMoleculeTypes
import java.lang.IllegalArgumentException


fun prioritize(possibleActions: List<PrioAction>, algo: PrioAlgo): List<PrioAction> {

    addDistanceFactor(possibleActions, algo)
    prioritizeMolecules(possibleActions, algo)

    prioNewSamples(possibleActions, algo)

    possibleActions
            .filter { it.prioActionType == PrioActionType.WAIT }
            .forEach { it.prio = 10 }

    possibleActions
            .filter { it.prioActionType == PrioActionType.DIAGNOSE_SAMPLE }
            .forEach { it.prio += 10 }

    possibleActions
            .filter { it.prioActionType == PrioActionType.UPLOAD_SAMPLE }
            .forEach { it.prio = 20 }

    possibleActions
            .filter { it.prioActionType == PrioActionType.GET_DIAGNOSED_SAMPLE }
            .forEach { it.prio = 15 }


    return possibleActions
}

fun prioNewSamples(possibleActions: List<PrioAction>, algo: PrioAlgo) {

    val carriedSampleRanks = algo.state.samples
            .filter { it.carriedBy == Carrier.ME }
            .map { it.rank }
            .toList()

    possibleActions
            .filter { it.prioActionType == PrioActionType.GET_NEW_SAMPLE }
            .forEach { it.prio = 110 }

    possibleActions
            .filter { it.prioActionType == PrioActionType.GET_NEW_SAMPLE }
            .filter { !carriedSampleRanks.contains(it.prioActionSubType.toRankString().toInt()) }
            .forEach { it.prioChange("notCarriedRank", 10) }

    val totalExpertize = getMoleculeTypes()
            .map { algo.state.me.getExpertizeOfType(it) }
            .sum()

    possibleActions
            .filter { it.prioActionType == PrioActionType.GET_NEW_SAMPLE }
            .filter { it.prioActionSubType == PrioActionSubType.RANK_1 }
            .forEach { it.prioChange("expertizeMod", Math.max(10-totalExpertize, -10))}

    possibleActions
            .filter { it.prioActionType == PrioActionType.GET_NEW_SAMPLE }
            .filter { it.prioActionSubType == PrioActionSubType.RANK_3 }
            .forEach { it.prioChange("expertizeMod", Math.min(totalExpertize-10, 10))}


}

fun addDistanceFactor(possibleActions: List<PrioAction>, algo: PrioAlgo) {
    possibleActions
            .filter { it.prioActionType != PrioActionType.WAIT }
            .forEach {
                val distance = algo.state.me.location.distanceTo(getActionLocation(it.prioActionType))
                it.prioChange("dist", distance*-10)
    }
}

fun getActionLocation(actionType: PrioActionType): Location {
    return when(actionType) {
        PrioActionType.GET_NEW_SAMPLE -> Location.SAMPLES
        PrioActionType.DIAGNOSE_SAMPLE -> Location.DIAGNOSIS
        PrioActionType.UPLOAD_SAMPLE -> Location.DIAGNOSIS
        PrioActionType.GET_DIAGNOSED_SAMPLE -> Location.DIAGNOSIS
        PrioActionType.GET_MOLECULE -> Location.MOLECULES
        PrioActionType.PRODUCE_MEDICINE -> Location.LABORATORY
        else -> throw IllegalArgumentException()
    }
}


fun prioritizeMolecules(possibleActions: List<PrioAction>, algo: PrioAlgo) {


    addIfWeNeedMoleculesOfThatTypeForSamples(possibleActions, algo)

    addPrioIfWeAlreadyHaveMoleculesForTheSample(possibleActions, algo)
}


fun addIfWeNeedMoleculesOfThatTypeForSamples(possibleActions: List<PrioAction>, algo: PrioAlgo) {

    possibleActions
            .filter { it.prioActionType == PrioActionType.GET_MOLECULE }
            .filter { numNeededForAllSamples(algo, it.prioActionSubType) > numCarried(algo, it.prioActionSubType) }
            .forEach { it.prioChange("typeNeeded",10) }

}

fun addPrioIfWeAlreadyHaveMoleculesForTheSample(possibleActions: List<PrioAction>, algo: PrioAlgo) {
    possibleActions
            .filter { it.prioActionType == PrioActionType.GET_MOLECULE }
            .forEach { it.prioChange("existingForSample", getNumMoleculesNeededOfTypeForAllCarriedSamples(algo, it.prioActionSubType) * 10) }
}

fun numCarried(algo: PrioAlgo, prioActionSubType: PrioActionSubType) =
        algo.state.me.getStorageOfType(prioActionSubType.toTypeString())

fun numNeededForAllSamples(algo: PrioAlgo, prioActionSubType: PrioActionSubType) =
        algo.state.samples
                .filter { it.carriedBy == Carrier.ME && algo.diagnosedSamples.contains(it.sampleId) }
                .map { it.getCostOfType(prioActionSubType.toTypeString()) }
                .sum()

// for a given type, how many more molecules in total do we need to complete all samples?
fun getNumMoleculesNeededOfTypeForAllCarriedSamples(algo: PrioAlgo, subType: PrioActionSubType) =
        algo.state.samples
            .filter { it.carriedBy == Carrier.ME && algo.diagnosedSamples.contains(it.sampleId) }
            .map { it.getCostOfType(subType.toTypeString()) - algo.state.me.getStorageOfType(subType.toTypeString()) }
            .filter { it > 0 }
            .sum()




