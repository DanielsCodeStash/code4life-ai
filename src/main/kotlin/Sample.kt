import java.lang.RuntimeException

data class Sample(

        var sampleId: Int = -1,
        var carriedBy: Carrier = Carrier.CLOUD,
        var rank: Int = -1,
        var expertiseGain: String = "",
        var health: Int = -1,
        var costA: Int = -1,
        var costB: Int = -1,
        var costC: Int = -1,
        var costD: Int = -1,
        var costE: Int = -1


) {
    fun getCostOfType(type: String): Int {
        when(type) {
            "A" -> return costA
            "B" -> return costB
            "C" -> return costC
            "D" -> return costD
            "E" -> return costE
        }
        throw RuntimeException("dh")
    }
}