package mcom.map


abstract class Distance(val tileFrom: Tile, val tileTo: Tile) {
    abstract val distance: Int
    abstract val ruleType: MapType

    operator fun compareTo(other: Distance): Int {
        if (this is Comparable<*> && other.javaClass == this.javaClass) throw NotImplementedError()
        if (distance > other.distance)
            return 1
        if (distance < other.distance)
            return -1
        return 0
    }
}