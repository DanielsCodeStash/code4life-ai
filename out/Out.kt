import java.util.*
import java.util.*
import Location
import java.lang.RuntimeException
import java.lang.RuntimeException
enum class Carrier {ME, ENEMY, CLOUD}

fun readPlayer(input: Scanner): Player {
    val p = Player()

    p.location = Location.valueOf(input.next())
    p.eta = input.nextInt()
    p.scoreHealth = input.nextInt()
    p.storageA = input.nextInt()
    p.storageB = input.nextInt()
    p.storageC = input.nextInt()
    p.storageD = input.nextInt()
    p.storageE = input.nextInt()
    p.expertiseA = input.nextInt()
    p.expertiseB = input.nextInt()
    p.expertiseC = input.nextInt()
    p.expertiseD = input.nextInt()
    p.expertiseE= input.nextInt()

    return p
}

fun readProjects(input: Scanner) {
    val projectCount = input.nextInt()


    for (i in 0 until projectCount) {
        val a = input.nextInt()
        val b = input.nextInt()
        val c = input.nextInt()
        val d = input.nextInt()
        val e = input.nextInt()
    }
}

fun readMoleculeAvailability(input: Scanner) {
    val availableA = input.nextInt()
    val availableB = input.nextInt()
    val availableC = input.nextInt()
    val availableD = input.nextInt()
    val availableE = input.nextInt()
}

fun readSamples(input: Scanner): List<Sample> {
    val sampleCount = input.nextInt()

    val samples = mutableListOf<Sample>()
    for (i in 0 until sampleCount) {
        samples.add(readSample(input))
    }

    return samples
}

fun readSample(input: Scanner): Sample {
    val sample = Sample()

    sample.sampleId = input.nextInt()
    when(input.nextInt()) {
        -1 -> sample.carriedBy = Carrier.CLOUD
        0 -> sample.carriedBy = Carrier.ME
        1 -> sample.carriedBy = Carrier.ENEMY
    }
    sample.rank = input.nextInt()
    sample.expertiseGain = input.next()
    sample.health = input.nextInt()
    sample.costA = input.nextInt()
    sample.costB = input.nextInt()
    sample.costC = input.nextInt()
    sample.costD = input.nextInt()
    sample.costE = input.nextInt()

    return sample
}

enum class Location {
    LABORATORY, DIAGNOSIS, MOLECULES, START_POS, SAMPLES;

}

fun main() {

    val debug = true

    var playerHasSample = false
    var playerHasMoleculesNeeded = false
    var playerSampleDiagnosed = false
    var activeSampleId = -1

    val input = Scanner(System.`in`)

    readProjects(input)

    // game loop
    while (true) {

        // read
        val ownPlayer = readPlayer(input)
        val enemyPlayer = readPlayer(input)
        readMoleculeAvailability(input)
        val sampleList = readSamples(input)

        // debug
        if(debug) {
            debug(ownPlayer.toString())
            debug(enemyPlayer.toString())
            sampleList.forEach { debug(it.toString()) }
        }

        // prepare
        playerHasSample = playerHasSample(sampleList)
        playerHasMoleculesNeeded = playerHasMoleculesNeeded(ownPlayer, sampleList)

        // go
        if (!playerHasSample) {
            if(listOf("A", "B", "C", "D", "E").filter { ownPlayer.getStorageOfType(it) != 0 }.isNotEmpty()) {
                println("WHHHAAAT")
            }
            debug("GETTING SAMPLE!")
            getSample(ownPlayer, sampleList)
        } else if(!playerSampleDiagnosed) {
            debug("DIAGNOSING SAMPLE!")
            val diagnosed = diagnoseSample(ownPlayer, sampleList, playerSampleDiagnosed)
            if(diagnosed) {
                playerSampleDiagnosed = true
            }
        } else if (!playerHasMoleculesNeeded) {
            debug("GETTING MOLECULES!")
            getMolecules(ownPlayer, sampleList)
        } else {
            debug("DUMPING IN LAB!")
            val dumped = dumpInLab(ownPlayer, sampleList)
            if(dumped) {
                playerSampleDiagnosed = false
            }
        }
    }
}

fun diagnoseSample(ownPlayer: Player, sampleList: List<Sample>, playerSampleDiagnosed: Boolean): Boolean {
    if(ownPlayer.location != Location.DIAGNOSIS) {
        goto(Location.DIAGNOSIS)
    } else if (!playerSampleDiagnosed)  {
        println("CONNECT " + getPlayerCarriedSample(ownPlayer, sampleList).sampleId)
        return true
    }
    return false
}

fun dumpInLab(ownPlayer: Player, sampleList: List<Sample>): Boolean {
    if (ownPlayer.location != Location.LABORATORY) {
        goto(Location.LABORATORY)
    } else if (ownPlayer.location == Location.LABORATORY) {
        println("CONNECT " + getPlayerCarriedSample(ownPlayer, sampleList).sampleId)
        return true
    }
    return false
}

fun getSample(ownPlayer: Player, samples: List<Sample>) {
    if (ownPlayer.location != Location.SAMPLES) {
        goto(Location.SAMPLES)
    } else if (ownPlayer.location == Location.SAMPLES) {
        println("CONNECT 2")
    }
}

fun getSampleWithMostHealth(samples: List<Sample>): Sample {
    return samples
            .filter { it.carriedBy == Carrier.ME }
            .maxBy { it.health }!!
}

fun getMolecules(ownPlayer: Player, samples: List<Sample>) {
    if (ownPlayer.location != Location.MOLECULES) {
        goto(Location.MOLECULES)
    } else if (ownPlayer.location == Location.MOLECULES) {
        getMoleculesNeeded(ownPlayer, samples)
    }
}

fun playerHasSample(samples: List<Sample>): Boolean {
    return samples.any { it.carriedBy == Carrier.ME }
}

fun playerHasMoleculesNeeded(player: Player, samples: List<Sample>): Boolean {

    if (!playerHasSample(samples))
        return false

    return listOf("A", "B", "C", "D", "E")
            .filter { !playerHasMoleculesNeedOfType(player, samples, it) }
            .isEmpty()
}

fun getMoleculesNeeded(player: Player, samples: List<Sample>) {
    val missingSamples = listOf("A", "B", "C", "D", "E")
            .filter { !playerHasMoleculesNeedOfType(player, samples, it) }

    println("CONNECT " + missingSamples.first())
}

fun playerHasMoleculesNeedOfType(player: Player, samples: List<Sample>, type: String): Boolean {
    val sample = getPlayerCarriedSample(player, samples)
    val cost = sample.getCostOfType(type)
    val stored = player.getStorageOfType(type)
    val hasEnough = stored >= cost;

    //debug("hasEnough $hasEnough of type $type cost: $cost stored: $stored" )

    return hasEnough
}

fun getPlayerCarriedSample(player: Player, sampleList: List<Sample>): Sample {
    return sampleList.first { it.carriedBy == Carrier.ME }
}



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
fun debug(out: String) {
    System.err.println(out)
}


fun goto(location: Location) {
    println("GOTO $location")
}
