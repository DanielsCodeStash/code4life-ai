package lifegame.util

import lifegame.container.*
import java.lang.IllegalStateException

class SmartAlgo {

    // algo state
    private var playerHasSample = false
    private var playerHasMoleculesNeeded = false
    private var playerSampleDiagnosed = false


    // round state
    private lateinit var state: RoundState

    fun getTurnAction(state: RoundState): Action {

        this.state = state
        playerHasSample = playerHasSample()
        playerHasMoleculesNeeded = playerHasMoleculesNeeded()

        if(state.me.eta != 0) {
            return Action(ActionType.WAIT)
        }

        return if (!playerHasSample) {
            getSample()

        } else if(!playerSampleDiagnosed) {
            diagnoseSample()

        } else if (!playerHasMoleculesNeeded) {
            getMolecules()

        } else {
            dumpInLab()

        }
    }


    private fun diagnoseSample(): Action {
        debug("DIAGNOSING SAMPLE!")

        if(state.me.location != Location.DIAGNOSIS) {
            playerSampleDiagnosed = false
            return Action(ActionType.GOTO, Location.DIAGNOSIS)

        } else if (!playerSampleDiagnosed)  {
            playerSampleDiagnosed = true
            return Action(ActionType.CONNECT, getPlayerCarriedSample().sampleId)
        }

        throw IllegalStateException()
    }

    private fun dumpInLab(): Action {
        debug("DUMPING IN LAB!")

        if (state.me.location != Location.LABORATORY) {
            return Action(ActionType.GOTO, Location.LABORATORY)

        } else if (state.me.location == Location.LABORATORY) {
            playerSampleDiagnosed = false
            return Action(ActionType.CONNECT, getPlayerCarriedSample().sampleId)

        }
        throw IllegalStateException()

    }

    private fun getSample(): Action {
        debug("GETTING SAMPLE!")

        if (state.me.location != Location.SAMPLES) {
            return Action(ActionType.GOTO, Location.SAMPLES)
        } else if (state.me.location == Location.SAMPLES) {
            return Action(ActionType.CONNECT, "2")
        }

        throw IllegalStateException()
    }


    private fun getMolecules(): Action {
        debug("GETTING MOLECULES!")

        if (state.me.location != Location.MOLECULES) {
            return Action(ActionType.GOTO, Location.MOLECULES)
        } else if (state.me.location == Location.MOLECULES) {
            return getMoleculesNeeded()
        }

        throw IllegalStateException()
    }

    private fun playerHasSample(): Boolean {
        return state.samples.any { it.carriedBy == Carrier.ME }
    }

    private fun playerHasMoleculesNeeded(): Boolean {

        if (!playerHasSample())
            return false

        return listOf("A", "B", "C", "D", "E")
                .filter { !playerHasMoleculesNeedOfType(it) }
                .isEmpty()
    }

    private fun getMoleculesNeeded(): Action {
        val missingSamples = listOf("A", "B", "C", "D", "E")
                .filter { !playerHasMoleculesNeedOfType(it) }

        return Action(ActionType.CONNECT, missingSamples.first())
    }

    private fun playerHasMoleculesNeedOfType(type: String): Boolean {
        val sample = getPlayerCarriedSample()
        val cost = sample.getCostOfType(type)
        val stored = state.me.getStorageOfType(type)
        val hasEnough = stored >= cost

        return hasEnough
    }

    private fun getPlayerCarriedSample(): Sample {
        return state.samples.first { it.carriedBy == Carrier.ME }
    }


}