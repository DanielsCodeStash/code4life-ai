package lifegame.container

import java.lang.IllegalArgumentException

enum class Location {
    START_POS, LABORATORY, DIAGNOSIS, MOLECULES, SAMPLES;

    fun distanceTo(location: Location): Int {

        if(this == location) return 0

        when {
            this == START_POS -> {
                return 2
            }
            this == SAMPLES -> {
                return 3
            }
            this == DIAGNOSIS -> {
                return when(location) {
                    SAMPLES -> 3
                    MOLECULES -> 3
                    LABORATORY -> 4
                    else -> throw IllegalArgumentException()
                }
            }
            this == MOLECULES -> {
                return 3
            }
            this == LABORATORY -> {
                return when(location) {
                    SAMPLES -> 3
                    DIAGNOSIS -> 4
                    MOLECULES -> 3
                    else -> throw IllegalArgumentException()
                }
            }
            else -> throw IllegalArgumentException()
        }
    }
}