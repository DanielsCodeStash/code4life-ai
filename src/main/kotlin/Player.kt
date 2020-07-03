import Location
import java.lang.RuntimeException

data class Player(
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

    fun getStorageOfType(type: String): Int {
        when(type) {
            "A" -> return storageA
            "B" -> return storageB
            "C" -> return storageC
            "D" -> return storageD
            "E" -> return storageE
        }
        throw RuntimeException("dh")
    }
}

