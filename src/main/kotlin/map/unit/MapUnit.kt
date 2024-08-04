package mcom.map.unit

import mcom.Current
import mcom.map.Tile

class MapUnit {
    lateinit var tile: Tile
    var maxMovement: Int = 0
    var currentMovement = 0
        private set
    var attack: Double = 0.0
    var defense: Double = 0.0
    var retaliationAttk: Double = 0.0
        get() {
            // TODO shouldn't get gameInfo this way, but I'm being lazy
            return if (Current.gameInfo?.isCompatibilityMode == true) defense
            else field
        }

    fun resetMovement() {
        currentMovement = maxMovement
    }

    /**
     * BFS implementation to find the nearest tile
     */
    fun getMovableTiles(): Map<Tile, Int> {
        val movementUsed = HashMap<Tile, Int>()
        val neighbors = ArrayDeque(tile.neighbors)
        for (tile in neighbors) movementUsed[tile] = 1
        while (neighbors.isNotEmpty()) {
            val current = neighbors.removeFirst()
            if (current == tile) continue
            val used = movementUsed[current]!!
            if (used >= currentMovement) continue
            val willUse = used + 1
            val tileNeighbors = current.neighbors
            for (tileNeighbor in tileNeighbors) {
                if (movementUsed[tileNeighbor] == null || used < movementUsed[tileNeighbor]!!) {
                    movementUsed[tileNeighbor] = willUse
                    neighbors.addLast(tileNeighbor)
                }
            }
        }
        return movementUsed
    }
}