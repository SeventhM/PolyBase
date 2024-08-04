package mcom

import mcom.game.Game
import mcom.map.square.SquareTile
import mcom.map.square.SquareTileMap
import mcom.map.unit.MapUnit
import mcom.player.Player

fun main() {
    Current.gameInfo = Game()
    val game = Current.gameInfo!!

    for (i in 0..<4) {
        game.addPlayer(Player())
    }
    game.updatePorts()
    val map = SquareTileMap(18)
    map.dryLand()
    map.defaultSetPlayers(game.players)
    val unit = MapUnit()
    unit.maxMovement = 3
    unit.resetMovement()
    val tile = map.tiles.random()
    unit.tile = tile
    tile.unit = unit
    val moveables = unit.getMovableTiles()
    for (y in 0..<map.height) {
        for (x in 0..<map.width) {
            val tile = map.getTile(x, y)
            print(tile)
            print("$x , $y")
            print(" | ")
        }
        println()
    }
    val name = "Kotlin"
    println("Hello, $name!")

    for (i in 1..5) {
        println("i = $i")
    }
    println("Unit Location:  x:${(unit.tile as SquareTile).x} , y:${(unit.tile as SquareTile).y}")
    for ((tile, amount) in moveables) {
        val tile = tile as SquareTile
        println("x: ${tile.x}, y: ${tile.y}, movement: $amount")
    }
}