package lifegame.prio

import lifegame.PrioAlgo
import lifegame.container.Carrier
import lifegame.container.RoundState
import lifegame.container.Sample
import lifegame.prio.base.PrioAction
import lifegame.util.debug
import lifegame.util.getMoleculeTypes

class PrioProduceMedicine (
        private val state: RoundState,
        private val algo: PrioAlgo
) {

    data class OrderResult(val permutation: List<Int>, val numMedicines: Int, val numRemainingMolecules: Int)
    data class Storage(var A: Int, var B: Int, var C: Int, var D: Int, var E: Int) {
        fun getOfType(type: String): Int {
            return when(type) {
                "A" -> A
                "B" -> B
                "C" -> C
                "D" -> D
                "E" -> E
                else -> throw IllegalArgumentException(type)
            }
        }
        fun setForType(type: String, value: Int) {
            when(type) {
                "A" -> A = value
                "B" -> B = value
                "C" -> C = value
                "D" -> D = value
                "E" -> E = value
                else -> throw IllegalArgumentException(type)
            }

        }

    }

    private val analyzedResult = mutableListOf<OrderResult>()
    private val mySamples = state.samples
            .filter { it.carriedBy == Carrier.ME }
            .filter { algo.diagnosedSamples.contains(it.sampleId) }
            .toList()

    fun prioritizeProduceMedicine(produceActions: List<PrioAction>){



        val permutationList = produceActions
                .filter { algo.diagnosedSamples.contains(it.sampleId) }
                .map { it.sampleId }.toList()

        if(permutationList.size < 2)
            return


        generatePermutations(permutationList)
                .forEach { analyze(it) }

        analyzedResult
                .sortWith(compareByDescending<OrderResult>{it.numMedicines}.thenByDescending{it.numRemainingMolecules})

        val best = analyzedResult.first()

        var prioAdjust = 0

        best.permutation
                .map { getActionForSampleId(it, produceActions) }
                .forEach {
                    it.prioChange("orderLUL", prioAdjust--)
                }


    }

    private fun getActionForSampleId(sampleId: Int, produceActions: List<PrioAction>) = produceActions.first { it.sampleId == sampleId }

    private fun analyze(permutation: List<Int>) {
        val storage = Storage(state.me.storageA, state.me.storageB, state.me.storageC, state.me.storageD, state.me.storageE)
        val expertize = Storage(state.me.expertiseA, state.me.expertiseB, state.me.expertiseC, state.me.expertiseD, state.me.expertiseE)
        var numTurnedIn = 0


        permutation.forEach {sampleId ->
            val sample = getSampleById(sampleId)
            val didTurnIn = tryTurnIn(sample, storage, expertize)

            if(didTurnIn) numTurnedIn++
        }

        val numRemainingMolecules = getMoleculeTypes()
                .map { storage.getOfType(it) }
                .sum()

        analyzedResult.add(OrderResult(permutation.map { it }.toList(), numTurnedIn, numRemainingMolecules))

    }

    private fun getSampleById(id: Int) = state.samples
            .filter { it.sampleId == id }
            .first()

    private fun tryTurnIn(sample: Sample, storage: Storage, expertize: Storage): Boolean {
        val canNotTurnIn = getMoleculeTypes()
                .any { sample.getCostOfType(it) - expertize.getOfType(it) > storage.getOfType(it) }

        if(canNotTurnIn)
            return false

        getMoleculeTypes()
                .forEach {type ->
                    val cost = sample.getCostOfType(type) - expertize.getOfType(type)
                    if(cost > 0) {
                        storage.setForType(type, storage.getOfType(type) - cost)
                    }
                }

        expertize.setForType(sample.expertiseGain, expertize.getOfType(sample.expertiseGain)+1)

        return true
    }

    // don't look at this, I'll fix later
    private fun generatePermutations(original: List<Int>): List<List<Int>> {
        if (original.isEmpty())
            return emptyList()

        when (original.size) {
            1 -> {
                return listOf(original)
            }
            2 -> {
                return listOf(
                        listOf(original[0], original[1]),
                        listOf(original[1], original[0])
                )
            }
            3 -> {
                return listOf(
                        listOf(original[0], original[1], original[2]),
                        listOf(original[0], original[2], original[1]),

                        listOf(original[1], original[0], original[2]),
                        listOf(original[1], original[2], original[0]),

                        listOf(original[2], original[1], original[0]),
                        listOf(original[2], original[0], original[1])
                )
            }
            else -> throw java.lang.IllegalArgumentException("dontask")
        }
    }
}