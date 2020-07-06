package lifegame

import lifegame.container.Location
import lifegame.container.Player
import lifegame.container.Sample
import java.util.*

fun readPlayer(input: Scanner): Player {

    return Player.Builder()
            .location(Location.valueOf(input.next()))
            .eta(input.nextInt())
            .scoreHealth(input.nextInt())
            .storageA(input.nextInt())
            .storageB(input.nextInt())
            .storageC(input.nextInt())
            .storageD(input.nextInt())
            .storageE(input.nextInt())
            .expertiseA(input.nextInt())
            .expertiseB(input.nextInt())
            .expertiseC(input.nextInt())
            .expertiseD(input.nextInt())
            .expertiseE(input.nextInt())
            .build()
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

    return Sample.Builder()
            .sampleId(input.nextInt())
            .carriedBy(input.nextInt())
            .rank(input.nextInt())
            .expertiseGain(input.next())
            .health(input.nextInt())
            .costA(input.nextInt())
            .costB(input.nextInt())
            .costC(input.nextInt())
            .costD(input.nextInt())
            .costC(input.nextInt())
            .build()
}