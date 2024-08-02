package mcom.city

import mcom.map.FeatureType
import mcom.map.Tile
import mcom.map.TileFeature
import mcom.map.square.SquareTile

class City(override var name: String): TileFeature(name, FeatureType.city) {
    var owner: String? = null // String is placeholder for player logic
    var originalOwner: String? = null
    var originalCapital: Boolean = false
    var isCapital: Boolean = false
    lateinit var tile: Tile
    val location: Int get()  {
        if (tile.ruleType == "square") {
            return (tile as SquareTile).location
        }
        return 0
    }
}