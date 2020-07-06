package lifegame

class PrioAction(
        val prioActionType: PrioActionType,
        val prioActionSubType: PrioActionSubType,
        val sampleId: Int = -1
) {
    constructor(type: PrioActionType) : this(type, PrioActionSubType.NONE)
    constructor(type: PrioActionType, sampleId: Int) : this(type, PrioActionSubType.NONE, sampleId)

    var prio: Int = 100


    override fun toString(): String {
        return "PrioAction(prioActionType=$prioActionType, prioActionSubType=$prioActionSubType, sampleId=$sampleId, prio=$prio)"
    }


}