package lifegame.prio

import lifegame.container.RoundState
import lifegame.prio.base.PrioAction

class PrioDiagnosing(
        private val state: RoundState
) {

    fun prio(actions: List<PrioAction>) {
        if (carryingTwoUndiagnosedSamples()) {
            actions.forEach { it.prioChange("twoUndiagnosed", 5) }
        }
    }

    private fun carryingTwoUndiagnosedSamples(): Boolean {

        if (state.numSamplesICarry() == 3) {
            return state.myDiagnosedSamples().size == 1
        }
        return false
    }
}