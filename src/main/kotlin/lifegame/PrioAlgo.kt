package lifegame

import lifegame.container.Action
import lifegame.container.ActionType
import lifegame.container.RoundState

class PrioAlgo {

    // algo state



    // round state
    private lateinit var state: RoundState

    fun getTurnAction(state: RoundState): Action {

        return Action(ActionType.WAIT)
    }

}