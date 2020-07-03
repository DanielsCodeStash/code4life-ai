import java.util.*

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


