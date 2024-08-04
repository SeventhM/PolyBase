package mcom.map.square

import mcom.map.MapType
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SquareDistance(tileFrom: SquareTile, tileTo: SquareTile) : Comparable<SquareDistance> {
    val tileTo = tileTo
    val tileFrom = tileFrom
    val distance: Int
    val secondaryDistance: Int
    val diagonalDiff: Int
    val absDiagonalDiff: Int
    val manDistance: Int
    val type = MapType.Square
    init {
        if (tileTo.map !== tileFrom.map) throw IllegalStateException("Maps are somehow different")
        if (tileFrom == tileTo) {
            distance = 0
            diagonalDiff = 0
            absDiagonalDiff = 0
            secondaryDistance = 0
            manDistance = 0
        } else {
            val map = tileFrom.map as SquareTileMap
            var xDistance1 = tileFrom.x - tileTo.x
            var xDistance2 = tileTo.x - tileFrom.x

            if (xDistance1 < 0) {
                val placeHolder = xDistance2
                xDistance2 = xDistance1
                xDistance1 = placeHolder
            }
            if (map.wrapX && xDistance2 != 0) {
                xDistance2 += map.width
            }
            val xDistance = if (map.wrapX) min(xDistance1, xDistance2) else xDistance1

            var yDistance1 = tileFrom.y - tileTo.y
            var yDistance2 = tileTo.y - tileFrom.y

            if (yDistance1 < 0) {
                val placeHolder = yDistance2
                yDistance2 = yDistance1
                yDistance1 = placeHolder
            }
            if (map.wrapY && yDistance2 != 0) {
                yDistance2 += map.height
            }
            val yDistance = if (map.wrapY) min(yDistance1, yDistance2) else yDistance1
            manDistance = xDistance + yDistance
            distance = min(xDistance, yDistance)
            secondaryDistance = max(xDistance, yDistance)
            diagonalDiff = xDistance - yDistance
            absDiagonalDiff = abs(diagonalDiff)
        }
    }

    override fun compareTo(other: SquareDistance): Int {
        if (distance > other.distance)
            return 1
        if (distance < other.distance)
            return -1
        if (secondaryDistance > other.secondaryDistance)
            return 1
        if (secondaryDistance < other.secondaryDistance)
            return -1
        return 0
    }
}