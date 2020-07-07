package lifegame.prio

import lifegame.PrioAlgo
import lifegame.container.RoundState
import lifegame.container.Sample
import java.lang.IllegalArgumentException

class SubPrioDiagnosed (
        private val state: RoundState,
        private val algo: PrioAlgo
) {

    fun prioritizeGivenSamples(samples: List<Sample>): List<Pair<Int, Sample>> {

        if(samples.filter { !algo.diagnosedSamples.contains(it.sampleId) }.any()) {
            throw IllegalArgumentException("Only takes diagnosed samples")
        }

        // how many for the sample we already have
        // how many we can still carry
        // how many molecules that we need exists (and if we'll be able to get them if enemy is fetching)







        return emptyList()
    }
}