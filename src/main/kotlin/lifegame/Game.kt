package lifegame

import lifegame.container.*
import lifegame.util.*
import java.util.*

fun main() {

    val printDebug = true
    val input = Scanner(System.`in`)
    val algo = PrioAlgo()
    var roundNum = 0

    readProjects(input)

    // game loop
    while (true) {
        roundNum++

        // read
        val roundState = readRoundState(input)

        // debug
        if(printDebug) {
//          debug(roundState.me.toString())
//          debug(roundState.me.toString())
            //roundState.samples.forEach { debug(it.toString()) }
            debugSamples(roundState)

        }

        // get
        val action = algo.getTurnAction(roundState)

        // output
        if(action.verb != ActionType.NONE) {

            println(action.verb.toString() + " " + action.thing)
        }
    }
}

fun debugSamples(roundState: RoundState) {

    val totalMoleculesCarried = getMoleculeTypes()
            .map { roundState.me.getStorageOfType(it) }
            .sum()

    var out: String = "    A B C D E = $totalMoleculesCarried\n"

    val storage = getMoleculeTypes()
            .map { roundState.me.getStorageOfType(it) }
            .joinToString(" ")

    val expertise = getMoleculeTypes()
            .map {roundState.me.getExpertizeOfType(it)}
            .joinToString(" ")

    out += "e   $expertise\n"
    out += "s   $storage\n"
    out += "-------------\n"

    roundState.samples
            .filter { it.carriedBy == Carrier.ME }
            .filter { it.costA != -1 }
            .forEach {
                sample ->
                out += if(sample.sampleId < 10) " " else ""
                out += "${sample.sampleId}: "
                out += getMoleculeTypes()
                    .map {sample.getCostOfType(it)}
                    .joinToString ( " " )
                out += "\n"
            }

    debug(out)
}

fun readRoundState(input: Scanner): RoundState {
    val me = readPlayer(input)
    val enemy = readPlayer(input)
    val moleculeStorage = readMoleculeStorage(input)
    val sampleList = readSamples(input)
    return RoundState(me, enemy, sampleList, moleculeStorage)
}



