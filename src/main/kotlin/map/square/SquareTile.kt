package mcom.map.square

import mcom.map.MapType
import mcom.map.Tile

class SquareTile(map: SquareTileMap, x: Int, y: Int): Tile() {
    val x = x
    val y = y
    override val ruleType = MapType.Square
    val location get() = (map as SquareTileMap).sizeY * y + x
    init {
        this.map = map
    }

    var nearBorder = false
        private set

    override val isCityAllowed: Boolean
        get() {
            if (base.name != "land") return false
            if (getNeighbors(2, true).any { it.city != null }) return false
            val map = map as SquareTileMap
            if (map.wrapX && map.wrapY) return true
            return !nearBorder
        }

    fun update() {
        neighbors = super.neighbors
        nearBorder = neighbors.size < 8 // for square tiles, it should have a full 8 tiles to be next to another Tile
        val withSelf = neighbors.toMutableList()
        withSelf.add(this)
        neighborsWithSelf = withSelf
    }

    override var neighbors: List<Tile> = ArrayList()
        private set

    override var neighborsWithSelf: List<Tile> = ArrayList()
        private set

    override fun getNeighbors(distance: Int, includeSelf: Boolean): List<SquareTile> {
        if (distance <= 0) throw IllegalArgumentException()
        val map = map as SquareTileMap
        val tiles = ArrayList<SquareTile>()
        if (includeSelf) tiles.add(this)
        for (i in 1..distance) {
            var xNegLocation = x - i
            while (map.wrapX && xNegLocation < 0) xNegLocation += map.sizeX
            var xPosLocation = x + i
            while (map.wrapX && xPosLocation >= map.sizeX) xPosLocation -= map.sizeX
            var yNegLocation = y - i
            while (map.wrapY && yNegLocation < 0) yNegLocation += map.sizeY
            var yPosLocation = y + i
            while (map.wrapY && yPosLocation >= map.sizeY) yPosLocation -= map.sizeY
            val negX = map.wrapX || xNegLocation >= 0
            val negY = map.wrapY || yNegLocation >= 0
            val posX = map.wrapX || xPosLocation < map.sizeX
            val posY = map.wrapY || yPosLocation < map.sizeY
            if (negX && negY) tiles.add(map.getTile(xNegLocation, yNegLocation))
            if (posX && negY) tiles.add(map.getTile(xPosLocation, yNegLocation))
            if (negX && posY) tiles.add(map.getTile(xNegLocation, yPosLocation))
            if (posX && posY) tiles.add(map.getTile(xPosLocation, yPosLocation))

            val startX = if (negX) 0 else 0 - xNegLocation
            val endX = if (posX) map.sizeX else map.sizeX - xNegLocation
            val startY = if (negY) 0 else 0 - yNegLocation
            val endY = if (posY) map.sizeY else map.sizeY - yNegLocation
            for (j in 1..<2 * i) {
                if (j in startY..<endY) {
                    if (negX) tiles.add(map.getTile(xNegLocation, yNegLocation + j))
                    if (posX) tiles.add(map.getTile(xPosLocation, yNegLocation + j))
                }
                if (j in startX..<endX) {
                    if (negY) tiles.add(map.getTile(xNegLocation + j, yNegLocation))
                    if (posY) tiles.add(map.getTile(xNegLocation + j, yPosLocation))
                }
            }
        }

        return tiles.distinct()
    }

    override fun getOuterEdge(distance: Int): List<SquareTile> {
        if (distance <= 0) throw IllegalArgumentException()
        val map = map as SquareTileMap
        val tiles = ArrayList<SquareTile>()
        var xNegLocation = x - distance
        while (map.wrapX && xNegLocation < 0) xNegLocation += map.sizeX
        var xPosLocation = x + distance
        while (map.wrapX && xPosLocation >= map.sizeX) xPosLocation -= map.sizeX
        var yNegLocation = y - distance
        while (map.wrapY && yNegLocation < 0) yNegLocation += map.sizeY
        var yPosLocation = y + distance
        while (map.wrapY && yPosLocation >= map.sizeY) yPosLocation -= map.sizeY
        val negX = map.wrapX || xNegLocation >= 0
        val negY = map.wrapY || yNegLocation >= 0
        val posX = map.wrapX || xPosLocation < map.sizeX
        val posY = map.wrapY || yPosLocation < map.sizeY
        if (negX && negY) tiles.add(map.getTile(xNegLocation, yNegLocation))
        if (posX && negY) tiles.add(map.getTile(xPosLocation, yNegLocation))
        if (negX && posY) tiles.add(map.getTile(xNegLocation, yPosLocation))
        if (posX && posY) tiles.add(map.getTile(xPosLocation, yPosLocation))

        val startX = if (negX) 0 else 0 - xNegLocation
        val endX = if (posX) map.sizeX else map.sizeX - xNegLocation
        val startY = if (negY) 0 else 0 - yNegLocation
        val endY = if (posY) map.sizeY else map.sizeY - yNegLocation
        for (j in 1..<2 * distance) {
            if (j in startY..<endY) {
                if (negX) tiles.add(map.getTile(xNegLocation, yNegLocation + j))
                if (posX) tiles.add(map.getTile(xPosLocation, yNegLocation + j))
            }
            if (j in startX..<endX) {
                if (negY) tiles.add(map.getTile(xNegLocation + j, yNegLocation))
                if (posY) tiles.add(map.getTile(xNegLocation + j, yPosLocation))
            }
        }

        return tiles.distinct()
    }

    fun getDistanceTo(tile: SquareTile): SquareDistance {
        return SquareDistance(this, tile)
    }
}