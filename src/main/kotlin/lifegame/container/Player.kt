package lifegame.container

import java.lang.RuntimeException

data class Player private constructor(
        val location: Location,
        val eta: Int,
        val scoreHealth: Int,
        val storageA: Int,
        val storageB: Int,
        val storageC: Int,
        val storageD: Int,
        val storageE: Int,
        val expertiseA: Int,
        val expertiseB: Int,
        val expertiseC: Int,
        val expertiseD: Int,
        val expertiseE: Int
) {

    data class Builder(
            var location: Location = Location.START_POS,
            var eta: Int = -1,
            var scoreHealth: Int = -1,
            var storageA: Int = -1,
            var storageB: Int = -1,
            var storageC: Int = -1,
            var storageD: Int = -1,
            var storageE: Int = -1,
            var expertiseA: Int = -1,
            var expertiseB: Int = -1,
            var expertiseC: Int = -1,
            var expertiseD: Int = -1,
            var expertiseE: Int = -1
    ) {
        fun location(location: Location) = apply { this.location = location }
        fun eta(eta: Int) = apply { this.eta = eta }
        fun scoreHealth(scoreHealth: Int) = apply { this.scoreHealth = scoreHealth }
        fun storageA(storageA: Int) = apply { this.storageA = storageA }
        fun storageB(storageB: Int) = apply { this.storageB = storageB }
        fun storageC(storageC: Int) = apply { this.storageC = storageC }
        fun storageD(storageD: Int) = apply { this.storageD = storageD }
        fun storageE(storageE: Int) = apply { this.storageE = storageE }
        fun expertiseA(expertiseA: Int) = apply { this.expertiseA = expertiseA }
        fun expertiseB(expertiseB: Int) = apply { this.expertiseB = expertiseB }
        fun expertiseC(expertiseC: Int) = apply { this.expertiseC = expertiseC }
        fun expertiseD(expertiseD: Int) = apply { this.expertiseD = expertiseD }
        fun expertiseE(expertiseE: Int) = apply { this.expertiseE = expertiseE }
        fun build() = Player(location, eta, scoreHealth, storageA, storageB, storageC, storageD, storageE, expertiseA, expertiseB, expertiseC, expertiseD, expertiseE)
    }

    fun getStorageOfType(type: String): Int {
        when (type) {
            "A" -> return storageA
            "B" -> return storageB
            "C" -> return storageC
            "D" -> return storageD
            "E" -> return storageE
        }
        throw RuntimeException("dh")
    }

    fun getExpertizeOfType(type: String): Int {
        when (type) {
            "A" -> return expertiseA
            "B" -> return expertiseB
            "C" -> return expertiseC
            "D" -> return expertiseD
            "E" -> return expertiseE
        }
        throw RuntimeException("dh")
    }
}

