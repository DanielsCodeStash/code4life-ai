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
            // debug(roundState.me.toString())
            // debug(roundState.me.toString())
            // roundState.samples.forEach { debug(it.toString()) }
            debugSamples(roundState)

        }

        // get
        val action = algo.getTurnAction(roundState)

        // output
        if(action.verb != ActionType.NONE) {
            if(action.verb == ActionType.WAIT) {
                println(getTauntMessage(roundState))
            } else {
                println(action.verb.toString() + " " + action.thing)
            }
        }
    }
}

fun getTauntMessage(roundState: RoundState): String {

    val weAreWinning = roundState.me.scoreHealth >= roundState.enemy.scoreHealth

    return if(weAreWinning) {
        when ((0..7).random()) {
            1 -> "You lose nub"
            2 -> "LUUUUHUUSER"
            3 -> "you = suck"
            4 -> "DU GAMLA DU FRIA DU"
            5 -> "I'm Einstein"
            6 -> "MMmm goodass health"
            else -> "#winning taunt"
        }
    } else {
        when ((0..7).random()) {
            1 -> "ONLY LUCKY"
            2 -> "WATCH YOUR 6"
            3 -> "WEAKASS POSER"
            4 -> "I'LL GET YOU"
            5 -> "Heavy breathing"
            6 -> "LUCK!!!"
            else -> "Generic losing taunt"
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



