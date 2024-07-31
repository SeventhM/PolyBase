package mcom

import mcom.data.builtins.Water
import mcom.map.square.SquareTileMap

fun main() {
    val map = SquareTileMap(11)
    for (tile in map.tiles) {
        tile.features.add(Water())
    }
    for (x in 0..<map.height) {
        for (y in 0..<map.width) {
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