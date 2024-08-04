package mcom.map

import mcom.city.City
import mcom.data.builtins.Blank
import mcom.map.unit.MapUnit


abstract class Tile {

    abstract val ruleType: MapType
    private var features = ArrayList<TileFeature>()
    var base: TileFeature = Blank()
        set(feature) {
            if (feature.type != FeatureType.Base) throw IllegalArgumentException()
            field = feature
        }
    var city: City? = null
    val type: String = "" // String is a placeholder
    var unit: MapUnit? = null // TODO multiple units on a tile?
    open lateinit var map: TileMap
    val allFeatures: List<TileFeature> get() {
        val list = ArrayList(features)
        list.add(base)
        if (city != null) list.add(city)
        return list
    }

    abstract fun getNeighbors(distance: Int = 1, includeSelf: Boolean = false): List<Tile>

    open val neighbors get() = getNeighbors()

    open val neighborsWithSelf get() = getNeighbors(includeSelf = true)

    override fun toString(): String {
        return allFeatures.toString()
    }
}