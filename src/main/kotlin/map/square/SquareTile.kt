package mcom.map.square

import mcom.map.Tile

class SquareTile(map: SquareTileMap, x: Int, y: Int): Tile() {
    val x = x
    val y = y
    val location get() = (map as SquareTileMap).height * y + x
    init {
        this.map = map
    }
}