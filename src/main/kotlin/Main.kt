package mcom

import mcom.game.Game
import mcom.map.square.SquareTileMap
import mcom.player.Player

fun main() {
    val game = Game()

    for (i in 0..<4) {
        game.addPlayer(Player())
    }
    game.updatePorts()
    val map = SquareTileMap(18)
    map.dryLand()
    map.defaultSetPlayers(game.players)
    for (y in 0..<map.height) {
        for (x in 0..<map.width) {
            val tile = map.getTile(x, y)
            print(tile)
            print(" | ")
        }
        println()
    }
    val name = "Kotlin"
    println("Hello, $name!")

    for (i in 1..5) {
        println("i = $i")
    }
}