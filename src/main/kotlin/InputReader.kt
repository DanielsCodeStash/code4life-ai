import java.util.*

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