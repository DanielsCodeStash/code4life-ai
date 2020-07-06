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
//            debug(roundState.me.toString())
//            debug(roundState.me.toString())
            roundState.samples.forEach { debug(it.toString()) }
        }

        // get
        val action = algo.getTurnAction(roundState)

        // output
        if(action.verb != ActionType.NONE) {

            println(action.verb.toString() + " " + action.thing)
        }
    }
}

fun readRoundState(input: Scanner): RoundState {
    val me = readPlayer(input)
    val enemy = readPlayer(input)
    val moleculeStorage = readMoleculeStorage(input)
    val sampleList = readSamples(input)
    return RoundState(me, enemy, sampleList, moleculeStorage)
}



