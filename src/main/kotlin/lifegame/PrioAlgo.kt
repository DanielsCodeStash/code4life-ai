package lifegame

import lifegame.container.*
import lifegame.util.debug
import lifegame.util.getMoleculeTypes

class PrioAlgo {

    // algo state
    private val diagnosedSamples: MutableSet<Int> = mutableSetOf()
    private var numCarriedSamples = 0
    private var numCarriedMolecules = 0

    // round state
    internal lateinit var state: RoundState

    fun getTurnAction(state: RoundState): Action {

        this.state = state
        numCarriedMolecules = getMoleculeTypes()
                .map { state.me.getStorageOfType(it) }
                .sum()

        val possibleActions = getPossibleActions()

        val prioritizedActions = prioritize(possibleActions, this).sortedByDescending { it.prio }

        val action = prioritizedActions.first()

        prioritizedActions.forEach { debug(it.toString()) }

        val actualAction = getActualAction(action)

        debug("Doing $actualAction")

        return actualAction
    }

    private fun getActualAction(prioAction: PrioAction): Action {
        return when (prioAction.prioActionType) {
            PrioActionType.GET_NEW_SAMPLE -> gotoOrAct(Location.SAMPLES,
                    Action(ActionType.CONNECT, prioAction.prioActionSubType.toRankString()),
                    { numCarriedSamples += 1 }
            )

            PrioActionType.DIAGNOSE_SAMPLE -> gotoOrAct(Location.DIAGNOSIS,
                    Action(ActionType.CONNECT, prioAction.sampleId),
                    {
                        numCarriedSamples -= 1
                        diagnosedSamples.add(prioAction.sampleId)
                    }

            )

            PrioActionType.GET_MOLECULE -> gotoOrAct(Location.MOLECULES,
                    Action(ActionType.CONNECT, prioAction.prioActionSubType.toString())
            )

            PrioActionType.PRODUCE_MEDICINE -> gotoOrAct(Location.LABORATORY,
                    Action(ActionType.CONNECT, prioAction.sampleId),
                    { numCarriedSamples -= 1 }
            )

            PrioActionType.GET_DIAGNOSED_SAMPLE -> gotoOrAct(Location.DIAGNOSIS,
                    Action(ActionType.CONNECT, prioAction.sampleId),
                    {numCarriedSamples += 1}
            )

            PrioActionType.WAIT -> Action(ActionType.WAIT)
            PrioActionType.UPLOAD_SAMPLE -> TODO()

        }
    }

    private fun gotoOrAct(location: Location, action: Action, onAction: () -> Unit): Action {
        return if (state.me.location != location || state.me.eta != 0) {
            Action(ActionType.GOTO, location)
        } else {
            onAction()
            action
        }
    }

    private fun gotoOrAct(location: Location, action: Action): Action {
        return if (state.me.location != location || state.me.eta != 0) {
            Action(ActionType.GOTO, location)
        } else {
            action
        }
    }

    private fun getPossibleActions(): List<PrioAction> {

        val getMolecules = if (numCarriedMolecules < 10) {

            getMoleculeTypes()
                    .filter { state.storage.getAvailableByType(it) > 0 }
                    .map { PrioAction(PrioActionType.GET_MOLECULE, PrioActionSubType.fromMoleculeType(it)) }
                    .toList()
        } else {
            emptyList()
        }

        val getSamples = if (numCarriedSamples < 3) {
            listOf(PrioActionSubType.RANK_1, PrioActionSubType.RANK_2, PrioActionSubType.RANK_3)
                    .map { PrioAction(PrioActionType.GET_NEW_SAMPLE, it) }
                    .toList()
        } else {
            emptyList()
        }

        val diagnoseSample = state.samples
                .filter { it.carriedBy == Carrier.ME }
                .filter { !diagnosedSamples.contains(it.sampleId) }
                .map { PrioAction(PrioActionType.DIAGNOSE_SAMPLE, it.sampleId) }
                .toList()

        val pickupDiagnosed = state.samples
                .filter { it.carriedBy == Carrier.CLOUD }
                .map { PrioAction(PrioActionType.GET_DIAGNOSED_SAMPLE, it.sampleId) }
                .toList()

        val produceMedicine = state.samples
                .filter { it.carriedBy == Carrier.ME }
                .filter { diagnosedSamples.contains(it.sampleId) }
                .map { PrioAction(PrioActionType.PRODUCE_MEDICINE, it.sampleId) }
                .toList()

        val others = listOf(PrioAction(PrioActionType.WAIT))

        return listOf(getMolecules, getSamples, diagnoseSample, pickupDiagnosed, produceMedicine, others).flatten()
    }

}