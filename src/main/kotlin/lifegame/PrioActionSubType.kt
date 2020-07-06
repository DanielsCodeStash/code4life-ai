package lifegame

enum class PrioActionSubType {

    A, B, C, D, E, RANK_1, RANK_2, RANK_3, NONE;

    companion object {
        fun fromMoleculeType(type: String): PrioActionSubType {
            when (type) {
                "A" -> return A
                "B" -> return B
                "C" -> return C
                "D" -> return D
                "E" -> return E
            }
            throw IllegalArgumentException(type)
        }
    }

    fun toRankString(): String {
        return when(this){
            RANK_1 -> "1"
            RANK_2 -> "2"
            RANK_3 -> "3"
            else -> throw IllegalArgumentException(this.toString())
        }
    }
}
