package lifegame.prio

import lifegame.PrioAlgo
import lifegame.container.Location
import lifegame.container.RoundState
import lifegame.prio.base.PrioAction
import lifegame.prio.base.PrioActionType
import java.lang.IllegalArgumentException

class PrioDistance (
        private val state: RoundState,
        private val algo: PrioAlgo
) {

    fun addDistanceFactor(possibleActions: List<PrioAction>) {
        possibleActions
                .filter { it.prioActionType != PrioActionType.WAIT }
                .forEach {
                    val distance = state.me.location.distanceTo(getActionLocation(it.prioActionType))
                    it.prioChange("dist", distance*-10)
                }
    }

    private fun getActionLocation(actionType: PrioActionType): Location {
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

}