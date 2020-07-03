import java.lang.RuntimeException

public enum class Location {
    LABORATORY, DIAGNOSIS, MOLECULES, START_POS;

    fun fromString(str: String): Location {
        when(str) {
            "LABORATORY" -> return LABORATORY
            "DIAGNOSIS" -> return DIAGNOSIS
            "MOLECULES" -> return MOLECULES
            "START_POS" -> return START_POS
        }
        throw RuntimeException()
    }
}