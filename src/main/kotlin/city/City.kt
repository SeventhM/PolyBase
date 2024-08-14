package mcom.city

import mcom.Current
import mcom.data.builtins.UnitBuiltins
import mcom.map.FeatureType
import mcom.map.MapType
import mcom.map.Tile
import mcom.map.TileFeature
import mcom.map.square.SquareTile
import mcom.player.Player

class City(override var name: String, tile: Tile? = null, var owningPlayer: Player? = null): TileFeature(name, FeatureType.City) {
    var owner: String? = owningPlayer?.ownerText // String is placeholder for player logic
    val originalOwnerText: String? get() = originalOwner?.ownerText
    var originalOwner: Player? = null
    var originalCapital: Boolean = false
    var isCapital: Boolean = false
    var level = 0
    var population = 0
        private set(value) {
            val difference = value - field
            field = value
            totalPopulation += difference
        }
    private var totalPopulation = 0
    val neededPopulation get() = level + 1 - population
    val baseSPT get() =
        if (population < 0) level + population
        else level
    lateinit var tile: Tile
    init {
        if (tile != null) this.tile = tile
        owningPlayer?.cities?.add(this)
    }
    val buildableUnits get() = UnitBuiltins.baseUnits.filter {
        it.value.cityCost < (owningPlayer?.stars ?: return@filter false)
    }
    val location: Int get()  {
        if (tile.ruleType == MapType.Square) {
            return (tile as SquareTile).location
        }
        return 0
    }
    fun gainPopulation(amount: Int, slow: Boolean = false) {
        if (slow) {
            if (amount >= 0) for (i in 0..<amount) gainPopulation(1)
            else for (i in 0..<-amount) gainPopulation(-1)
            return
        }
        population += amount
        while (population > level) {
            level++
            population -= level
        }
        while (Current.gameInfo?.isCompatibilityMode == false && population < 0 && level > 0) {
            population += level
            level--
        }
    }

    fun createUnit(unit: String): Boolean {
        val unit = UnitBuiltins.getUnit(unit) ?: return false
        unit.owner = owningPlayer
        tile.unit = unit
        unit.tile = tile
        unit.health = unit.maxHealth
        owningPlayer!!.units.add(unit)
        owningPlayer!!.stars -= unit.cityCost
        return true
    }
}