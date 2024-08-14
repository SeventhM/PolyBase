package mcom

import mcom.game.Game

object Current {
    var gameInfo: Game? = null
    val mapRuleType = gameInfo?.currentMap?.ruleType
}