package lifegame

import lifegame.container.*
import lifegame.prio.BasePrioFunction
import lifegame.prio.base.PrioAction
import lifegame.prio.base.PrioActionSubType
import lifegame.prio.base.PrioActionType
import lifegame.util.getMoleculeTypes

class PrioAlgo {

    private lateinit var state: RoundState

    fun getTurnAction(state: RoundState): Action {

        this.state = state

        if (state.me.eta != 0) return Action(ActionType.WAIT)

        val possibleActions = getAllPossibleActions()

        val nextAction = BasePrioFunction(state)
                .prioritize(possibleActions)
                .maxBy { it.prio }!!

        return getActualAction(nextAction)
    }

    private fun getActualAction(prioAction: PrioAction): Action {
        return when (prioAction.prioActionType) {

            PrioActionType.GET_NEW_SAMPLE -> gotoOrAct(Location.SAMPLES,
                    Action(ActionType.CONNECT, prioAction.prioActionSubType.toRankString())
            )

            PrioActionType.DIAGNOSE_SAMPLE -> gotoOrAct(Location.DIAGNOSIS,
                    Action(ActionType.CONNECT, prioAction.sampleId)
            )
            PrioActionType.GET_MOLECULE -> gotoOrAct(Location.MOLECULES,
                    Action(ActionType.CONNECT, prioAction.prioActionSubType.toString())
            )

            PrioActionType.PRODUCE_MEDICINE -> gotoOrAct(Location.LABORATORY,
                    Action(ActionType.CONNECT, prioAction.sampleId)
            )

            PrioActionType.GET_DIAGNOSED_SAMPLE -> gotoOrAct(Location.DIAGNOSIS,
                    Action(ActionType.CONNECT, prioAction.sampleId)
            )

            PrioActionType.UPLOAD_SAMPLE -> gotoOrAct(Location.DIAGNOSIS,
                    Action(ActionType.CONNECT, prioAction.sampleId)
            )

            PrioActionType.WAIT -> Action(ActionType.WAIT)
        }
    }

    private fun gotoOrAct(location: Location, action: Action): Action {
        return if (state.me.location != location || state.me.eta != 0) {
            Action(ActionType.GOTO, location)
        } else {
            action
        }
    }

    private fun getAllPossibleActions(): List<PrioAction> {

        val numCarriedMolecules = getMoleculeTypes()
                .map { state.me.getStorageOfType(it) }
                .sum()

        val getMolecules = if (numCarriedMolecules < 10) {

            getMoleculeTypes()
                    .filter { state.storage.getAvailableByType(it) > 0 }
                    .map { PrioAction(PrioActionType.GET_MOLECULE, PrioActionSubType.fromMoleculeType(it)) }
                    .toList()
        } else {
            emptyList()
        }

        val getSamples = if (state.numSamplesICarry() < 3) {
            listOf(PrioActionSubType.RANK_1, PrioActionSubType.RANK_2, PrioActionSubType.RANK_3)
                    .map { PrioAction(PrioActionType.GET_NEW_SAMPLE, it) }
                    .toList()
        } else {
            emptyList()
        }

        val diagnoseSample = state.myUndiagnosedSamples()
                .map { PrioAction(PrioActionType.DIAGNOSE_SAMPLE, it.sampleId) }
                .toList()

        val storeSample = state.myDiagnosedSamples()
                .map { PrioAction(PrioActionType.UPLOAD_SAMPLE, it.sampleId) }
                .toList()

        val pickupDiagnosed = if (state.numSamplesICarry() < 3) {
            state.samples
                    .filter { it.carriedBy == Carrier.CLOUD }
                    .map { PrioAction(PrioActionType.GET_DIAGNOSED_SAMPLE, it.sampleId) }
                    .toList()
        } else {
            emptyList()
        }

        val produceMedicine = state.myDiagnosedSamples()
                .filter { hasEnoughMoleculesForSample(it) }
                .map { PrioAction(PrioActionType.PRODUCE_MEDICINE, it.sampleId) }
                .toList()

        val others = listOf(PrioAction(PrioActionType.WAIT))

        return listOf(getMolecules, getSamples, diagnoseSample, produceMedicine, storeSample, pickupDiagnosed, others).flatten()
    }

    private fun hasEnoughMoleculesForSample(sample: Sample) =
            getMoleculeTypes().none { state.me.getStorageOfType(it) < (sample.getCostOfType(it) - state.me.getExpertizeOfType(it)) }


}