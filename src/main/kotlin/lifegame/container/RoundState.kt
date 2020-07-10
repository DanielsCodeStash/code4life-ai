package lifegame.container

data class RoundState(
        val roundNum: Int,
        val me: Player,
        val enemy: Player,
        val samples: List<Sample>,
        val storage: MoleculeStorage
) {
    fun getMySamples() = samples
                .filter { it.carriedBy == Carrier.ME }
                .toList()

    fun getMyDiagnosedSamples() = samples
            .filter { it.carriedBy == Carrier.ME }
            .filter { it.costA == -1 }
            .toList()



}