package mcom.map.unit

import mcom.Current
import mcom.map.Tile
import mcom.player.Player
import kotlin.math.roundToInt

class MapUnit {
    lateinit var tile: Tile
    val abilities = ArrayList<String>()
    var maxMovement: Int = 0
    var currentMovement = 0
        private set
    var health: Int = 0
        private set
    var alive = true
        private set
    var owner: Player? = null
    var maxHealth: Int = 0
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

    fun dealDamage(amount: Int) {
        health -= amount
        if (health <= 0)
            alive = false
    }

    fun heal(amount: Int) {
        health += amount
        if (health > maxHealth) health = maxHealth
    }

    fun attack(unit: MapUnit) {
        val attForce = attack * health.toDouble() / maxHealth
        val defForce = unit.defense * health.toDouble() / unit.maxHealth
        val retaliationForce = unit.retaliationAttk * unit.health.toDouble() / unit.maxHealth
        val totalAtt = attForce + defForce
        val attDmg = (attForce / totalAtt * 4.5).roundToInt()
        unit.dealDamage(attDmg)
        if (unit.alive) return
        val defDmg = (retaliationForce / totalAtt * 4.5).roundToInt()
        dealDamage(defDmg)
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