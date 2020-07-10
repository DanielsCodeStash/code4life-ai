package lifegame.container

import java.lang.IllegalArgumentException

data class RoundState(
        val roundNum: Int,
        val me: Player,
        val enemy: Player,
        val samples: List<Sample>,
        val storage: MoleculeStorage
) {
    fun mySamples() = samples
            .filter { it.carriedBy == Carrier.ME }
            .toList()

    fun myDiagnosedSamples() = samples
            .filter { it.carriedBy == Carrier.ME }
            .filter { sampleIsDiagnosed(it) }
            .toList()

    fun myUndiagnosedSamples() = samples
            .filter { it.carriedBy == Carrier.ME }
            .filter { !sampleIsDiagnosed(it) }
            .toList()

    fun numSamplesICarry() = samples
            .filter { it.carriedBy == Carrier.ME }
            .size

    fun enemySamples() = samples
            .filter { it.carriedBy == Carrier.ENEMY }
            .toList()

    fun enemyDiagnosedSamples() = samples
            .filter { it.carriedBy == Carrier.ENEMY }
            .filter { sampleIsDiagnosed(it) }
            .toList()

    fun sampleIsDiagnosed(sample: Sample) = sample.costA != -1

    fun getSampleById(sampleId: Int) = samples
            .filter { sampleId == it.sampleId }
            .ifEmpty { throw IllegalArgumentException("No sample for $sampleId") }
            .first()

    fun sampleIsDiagnosed(sampleId: Int) = sampleIsDiagnosed(getSampleById(sampleId))


}