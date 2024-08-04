package mcom.game

import mcom.map.TileMap
import mcom.player.Player

class Game {
    var currentMap: TileMap? = null
    var players: ArrayList<Player> = ArrayList()
    var isCompatibilityMode = true

    fun addPlayer(player: Player, immediateUpdate: Boolean = false) {
        players.add(player)
        if (immediateUpdate) player.playerPort = players.size
    }

    fun removePlayer(player: Player) {
        val removed = players.remove(player)
        if (removed) updatePorts()
    }

    fun updatePorts() {
        for ((i, player) in players.withIndex()) {
            player.playerPort = i + 1
        }
    }
}