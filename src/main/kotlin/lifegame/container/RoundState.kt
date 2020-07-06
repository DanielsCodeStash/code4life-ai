package lifegame.container

data class RoundState(
        val me: Player,
        val enemy: Player,
        val samples: List<Sample>
)