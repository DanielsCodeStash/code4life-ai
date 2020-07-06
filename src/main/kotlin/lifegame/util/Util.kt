package lifegame.util

import lifegame.container.Location

fun debug(out: String) {
    System.err.println(out)
}


fun goto(location: Location) {
    println("GOTO $location")
}