package mcom.map.square

import mcom.map.MapType
import mcom.map.Tile

class SquareTile(map: SquareTileMap, x: Int, y: Int): Tile() {
    val x = x
    val y = y
    override val ruleType = MapType.Square
    val location get() = (map as SquareTileMap).height * y + x
    init {
        this.map = map
    }

    override fun getNeighbors(distance: Int, includeSelf: Boolean): List<SquareTile> {
        if (distance <= 0) throw IllegalArgumentException()
        val map = map as SquareTileMap
        val tiles = ArrayList<SquareTile>()
        if (includeSelf) tiles.add(this)
        for (i in 1..distance) {
            val xNegLocation = x - i
            val xPosLocation = x + i
            val yNegLocation = y - i
            val yPosLocation = y + i
            val negX = map.wrapX || xNegLocation >= 0
            val negY = map.wrapY || yNegLocation >= 0
            val posX = map.wrapX || xPosLocation < map.width
            val posY = map.wrapY || yPosLocation < map.height
            if (negX && negY) tiles.add(map.getTile(xNegLocation, yNegLocation))
            if (posX && negY) tiles.add(map.getTile(xPosLocation, yNegLocation))
            if (negX && posY) tiles.add(map.getTile(xNegLocation, yPosLocation))
            if (posX && posY) tiles.add(map.getTile(xPosLocation, yPosLocation))

            for (j in 1..<2 * i) {
                if (negX) tiles.add(map.getTile(xNegLocation, yNegLocation + j))
                if (posX) tiles.add(map.getTile(xPosLocation, yNegLocation + j))
                if (negY) tiles.add(map.getTile(xNegLocation + j, yNegLocation))
                if (posY) tiles.add(map.getTile(xNegLocation + j, yPosLocation))
            }
        }

        return tiles
    }

    fun getDistanceTo(tile: SquareTile): SquareDistance {
        return SquareDistance(this, tile)
    }
}