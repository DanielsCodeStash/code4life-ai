import java.util.*

fun main() {

    var playerHasSample = false
    var playerHasMoleculesNeeded = false


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
//        debug(ownPlayer.toString())
//        debug(enemyPlayer.toString())
//        sampleList.forEach { debug(it.toString()) }

        // prepare
        playerHasSample = playerHasSample(sampleList)
        playerHasMoleculesNeeded = playerHasMoleculesNeeded(ownPlayer, sampleList)

        // go
        if (!playerHasSample) {
            debug("GETTING SAMPLES!")
            getSample(ownPlayer, sampleList)
        } else if (!playerHasMoleculesNeeded) {
            debug("GETTING MOLECULES!")
            getMolecules(ownPlayer, sampleList)
        } else {
            debug("DUMPING IN LAB!")
            dumpInLab(ownPlayer, sampleList)
        }
    }
}

fun dumpInLab(ownPlayer: Player, sampleList: List<Sample>) {
    if (ownPlayer.location != Location.LABORATORY) {
        goto(Location.LABORATORY)
    } else if (ownPlayer.location == Location.LABORATORY) {
        println("CONNECT " + getPlayerCarriedSample(ownPlayer, sampleList).sampleId)
    }
}

fun getSample(ownPlayer: Player, samples: List<Sample>) {
    if (ownPlayer.location != Location.DIAGNOSIS) {
        goto(Location.DIAGNOSIS)
    } else if (ownPlayer.location == Location.DIAGNOSIS) {
        println("CONNECT " + getSampleWithMostHealth(samples).sampleId)
    }
}

fun getSampleWithMostHealth(samples: List<Sample>): Sample {
    return samples
            .filter { it.carriedBy == -1 }
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
    return samples.any { it.carriedBy == 0 }
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
    return sampleList.first { it.carriedBy == 0 }
}


