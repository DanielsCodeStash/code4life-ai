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