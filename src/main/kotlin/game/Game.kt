package mcom.game

import mcom.map.MapType
import mcom.map.TileMap
import mcom.map.square.SquareTileMap
import mcom.player.Player

class Game {
    var currentMap: TileMap? = null
        private set
    var players: ArrayList<Player> = ArrayList()
    var isCompatibilityMode = true
    var multipleMovement = false

    fun createNewMap(type: MapType, size: Int, generateType: String = "", setPlayers: Boolean = false) {
        when(type) {
            MapType.Square -> currentMap = SquareTileMap(size)
            else -> TODO()
        }
        if (generateType.isEmpty()) return
        generateMap(generateType)
    }

    fun createNewMap(type: MapType, x: Int, y: Int, generateType: String = "", setPlayers: Boolean = false) {
        when(type) {
            MapType.Square -> currentMap = SquareTileMap(x, y)
            else -> TODO()
        }
        if (generateType.isEmpty()) return
        generateMap(generateType)
    }

    fun generateMap(type: String) {
        currentMap!!.generate(type)
    }

    fun startGame() {
        for (player in players) {
            player.stars = 5
        }
    }
    fun addPlayers(vararg players: Player, immediateUpdate: Boolean = false) {
        this.players.addAll(players)
        if (immediateUpdate) updatePorts()
    }
    fun addPlayers(players: Collection<Player>, immediateUpdate: Boolean = false) {
        this.players.addAll(players)
        if (immediateUpdate) updatePorts()
    }
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