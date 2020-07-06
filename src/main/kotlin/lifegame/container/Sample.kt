package lifegame.container

import java.lang.RuntimeException

data class Sample private constructor(

        val sampleId: Int,
        val carriedBy: Carrier,
        val rank: Int,
        val expertiseGain: String,
        val health: Int,
        val costA: Int,
        val costB: Int,
        val costC: Int,
        val costD: Int,
        val costE: Int
) {

    data class Builder(
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
        fun sampleId(sampleId: Int) = apply { this.sampleId = sampleId }
        fun carriedBy(carriedBy: Int) = apply()
        {
            when(carriedBy) {
                -1 -> this.carriedBy = Carrier.CLOUD
                0 -> this.carriedBy =  Carrier.ME
                1 -> this.carriedBy = Carrier.ENEMY
            }
        }
        fun rank(rank: Int) = apply { this.rank = rank }
        fun expertiseGain(expertiseGain: String) = apply { this.expertiseGain = expertiseGain }
        fun health(health: Int) = apply { this.health = health }
        fun costA(costA: Int) = apply { this.costA = costA }
        fun costB(costB: Int) = apply { this.costB = costB }
        fun costC(costC: Int) = apply { this.costC = costC }
        fun costD(costD: Int) = apply { this.costD = costD }
        fun costE(costE: Int) = apply { this.costE = costE }
        fun build() = Sample(sampleId, carriedBy, rank, expertiseGain, health, costA, costB, costC, costD, costE)
    }
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