package mcom.map.square

import mcom.map.TileMap
import kotlin.properties.Delegates

class SquareTileMap : TileMap {
    val tiles = ArrayList<SquareTile>()
    var wrapLeft: Boolean = false
    var wrapUp: Boolean = false
    var wrapDown: Boolean = false
    var wrapRight: Boolean = false
    var width by Delegates.notNull<Int>()
        private set
    var height by Delegates.notNull<Int>()
        private set

    fun getTile(x: Int, y: Int): SquareTile {
        val place = y * height + x
        return tiles[place]
    }

    constructor()

    constructor(width: Int,  height: Int) {
        this.width = width
        this.height = height
        for (i in 0..<height)
            for (j in 0..<width) {
                tiles.add(SquareTile(this, i, j))
            }
    }
    constructor(size: Int): this(size, size)

    fun setWrapping(left: Boolean = wrapLeft, right: Boolean = wrapRight, up: Boolean = wrapUp, down: Boolean = wrapDown) {
        this.wrapLeft = left
        this.wrapRight = right
        this.wrapUp = up
        this.wrapDown = down
    }
}