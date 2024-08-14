package mcom.map.unit

import mcom.Current
import mcom.city.City
import mcom.map.Tile
import mcom.player.Player
import kotlin.math.roundToInt

class MapUnit {
    lateinit var tile: Tile
    lateinit var unitType: String
        internal set
    val abilities = ArrayList<String>()
    var maxMovement: Int = 0
    var currentMovement = 0
        private set
    var health: Int = 0
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
    var range: Int = 1
    val actionsTaken: List<String> = ArrayList()
    var cityCost: Int = 0
    var canAct = false

    fun resetMovement() {
        currentMovement = maxMovement
    }

    fun startTurn() {
        resetMovement()
        (actionsTaken as ArrayList).clear()
        canAct = true
    }

    fun end() {
        if ("heal" in actionsAvailable) {
            if (health < maxHealth) useAction("heal")
        }
        canAct = false
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

    fun attack(unit: MapUnit, retaliation: Boolean = true) {
        val attForce = attack * health.toDouble() / maxHealth
        val defForce = unit.defense * health.toDouble() / unit.maxHealth
        val retaliationForce = unit.retaliationAttk * unit.health.toDouble() / unit.maxHealth
        val totalAtt = attForce + defForce
        val attDmg = (attForce / totalAtt * 4.5).roundToInt()
        unit.dealDamage(attDmg)
        if (!unit.alive && range == 1) {
            move(unit.tile, true)
            return
        }
        if (retaliation) {
            val defDmg = (retaliationForce / totalAtt * 4.5).roundToInt()
            dealDamage(defDmg)
        }
    }

    fun canAttack(unit: MapUnit): Boolean {
        if (tile.getDistanceTo(unit.tile).distance > range) return false
        return true
    }

    fun useAction(action: String, vararg args: Any) {
        when(action) {
            "heal" -> if (tile.owner?.owningPlayer == owner) heal(4) else heal(2)
            "attack" -> {
                val unit = args.firstOrNull()
                if (unit !is MapUnit) return
                attack(unit)
            }
            "move" -> {
                val tile = args.firstOrNull()
                if (tile !is Tile) return
                move(tile, true)
            }
            "capture" -> {
                val tile = args.firstOrNull()
                if (tile is City) owner?.capture(tile, this)
                else if (tile is Tile && tile.city != null) owner?.capture(tile.city!!, this)
                else return
            }
        }
        if (action != "move") canAct = false
        (actionsTaken as ArrayList).add(action)
    }

    val actionsAvailable: List<String> get() {
        if (!canAct) return emptyList()
        if (actionsTaken.contains("move") && !abilities.contains("dash")) {
            if (Current.gameInfo?.multipleMovement == true && getMovableTiles().filter { it.key.unit == null }.isNotEmpty()) return listOf("move")
            return emptyList()
        }
        val available = ArrayList<String>()
        for (action in BUILTINACTIONS) {
            when(action) {
                "move" -> {
                    if (getMovableTiles().filter { it.key.unit != null }.isEmpty()) continue
                    if (Current.gameInfo?.multipleMovement != true && action in actionsTaken) continue
                }
                in actionsTaken ->  continue
                "heal" -> if (health >= maxHealth) continue
                "capture" -> if (tile.city == null) continue
                "interact" -> continue
            }
            available.add(action)
        }
        return available
    }

    fun move(tile: Tile, confirmMove: Boolean = false, teleport: Boolean = false): Boolean {
        val multiMove = Current.gameInfo?.multipleMovement == true
        var movementInfo: Int = 0
        var movableTiles: Map<Tile, Int> = emptyMap()
        if (confirmMove || (!teleport && multiMove)) {
            movableTiles = getMovableTiles()
            movementInfo = movableTiles[tile] ?: return false
            if (currentMovement < maxMovement && movementInfo > currentMovement) return false
        }
        this.tile.unit = null
        this.tile = tile
        tile.unit = this
        if (multiMove) {
            val newMovement = currentMovement - movementInfo
            if (movableTiles.none { it.value - movementInfo < newMovement })
                (actionsTaken as ArrayList).add("fullMove")
            if (!actionsTaken.contains("move"))
                (actionsTaken as ArrayList).add("move")
            currentMovement = newMovement
        }
        else (actionsTaken as ArrayList).add("move")
        return true
    }

    fun switch(other: MapUnit, confirmMove: Boolean = false): Boolean {
        if (confirmMove) {
            val movableTiles = getMovableTiles()
            if (other.tile !in movableTiles.keys) return false
            val otherMovableTiles = other.getMovableTiles()
            if (tile !in otherMovableTiles.keys) return false
        }
        val otherTile = other.tile
        tile.unit = other
        otherTile.unit = this
        other.tile = tile
        tile = otherTile
        return true
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
                if (movementUsed[tileNeighbor] == null || willUse < movementUsed[tileNeighbor]!!) {
                    movementUsed[tileNeighbor] = willUse
                    neighbors.addLast(tileNeighbor)
                }
            }
        }
        return movementUsed
    }

    companion object {
        val BUILTINACTIONS = listOf("attack", "heal", "wait", "capture", "interact", "move")
    }
}