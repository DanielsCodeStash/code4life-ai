package lifegame.container

data class MoleculeStorage(
        val A: Int,
        val B: Int,
        val C: Int,
        val D: Int,
        val E: Int
) {
    fun getAvailableByType(type: String): Int {
        when(type) {
            "A" -> return A
            "B" -> return B
            "C" -> return C
            "D" -> return D
            "E" -> return E
        }
        throw IllegalArgumentException(type)
    }
}