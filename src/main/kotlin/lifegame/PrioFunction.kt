package lifegame


fun prioritize(possibleActions: List<PrioAction>, algo: PrioAlgo): List<PrioAction> {

    possibleActions
            .filter { it.prioActionType == PrioActionType.GET_NEW_SAMPLE }
            .forEach { it.prio = 200 }

    return possibleActions
}