package lifegame

import lifegame.container.*
import lifegame.util.*
import java.util.*

fun main() {

    val printDebug = true
    val input = Scanner(System.`in`)
    val smartAlgo = SmartAlgo()

    readProjects(input)

    // game loop
    while (true) {

        // read
        val roundState = readRoundState(input)

        // debug
        if(printDebug) {
            debug(roundState.me.toString())
            debug(roundState.me.toString())
            roundState.samples.forEach { debug(it.toString()) }
        }

        // get
        val action = smartAlgo.getTurnAction(roundState)

        // output
        println(action.verb.toString() + " " + action.thing)
    }
}

fun readRoundState(input: Scanner): RoundState {
    val me = readPlayer(input)
    val enemy = readPlayer(input)
    readMoleculeAvailability(input)
    val sampleList = readSamples(input)
    return RoundState(me, enemy, sampleList)
}



