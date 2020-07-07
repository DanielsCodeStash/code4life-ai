package lifegame

class PrioAction(
        val prioActionType: PrioActionType,
        val prioActionSubType: PrioActionSubType,
        val sampleId: Int = -1,
        private val prioFactors: MutableList<PrioFactor> = mutableListOf()
) {
    constructor(type: PrioActionType) : this(type, PrioActionSubType.NONE)
    constructor(type: PrioActionType, sampleId: Int) : this(type, PrioActionSubType.NONE, sampleId)

    var prio: Int = 100

    fun prioChange(description: String, effect: Int) {
        if(effect == 0)
            return

        prioFactors.add(PrioFactor(description, effect))
        prio += effect
    }

    override fun toString(): String {
        val subType = if(prioActionSubType != PrioActionSubType.NONE) " - $prioActionSubType" else ""

        return "$prio -> $prioActionType $subType " +
                prioFactors
                .joinToString (", ") {it.description + ": " + it.factor}

    }
}