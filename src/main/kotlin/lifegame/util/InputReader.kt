package lifegame.util

import lifegame.container.*
import java.util.*

fun readRoundState(input: Scanner, projects: List<Project>, roundNum: Int): RoundState {
    val me = readPlayer(input)
    val enemy = readPlayer(input)
    val moleculeStorage = readMoleculeStorage(input)
    val sampleList = readSamples(input)
    return RoundState(roundNum, me, enemy, sampleList, moleculeStorage, projects)
}

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

fun readProjects(input: Scanner): List<Project> {
    val projects = mutableListOf<Project>()
    val projectCount = input.nextInt()

    for (i in 0 until projectCount) {
        val project = Project.Builder()
                .a(input.nextInt())
                .b(input.nextInt())
                .c(input.nextInt())
                .d(input.nextInt())
                .e(input.nextInt())
                .build()

        projects.add(project)
    }

    return projects
}

fun readMoleculeStorage(input: Scanner): MoleculeStorage {
    return MoleculeStorage(input.nextInt(), input.nextInt(), input.nextInt(), input.nextInt(), input.nextInt())
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
            .costE(input.nextInt())
            .build()
}