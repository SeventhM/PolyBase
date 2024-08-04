package mcom.city

import mcom.map.FeatureType
import mcom.map.MapType
import mcom.map.Tile
import mcom.map.TileFeature
import mcom.map.square.SquareTile
import mcom.player.Player

class City(override var name: String, var owningPlayer: Player? = null): TileFeature(name, FeatureType.City) {
    var owner: String? = owningPlayer?.ownerText // String is placeholder for player logic
    var originalOwner: String? = null
    var originalCapital: Boolean = false
    var isCapital: Boolean = false
    lateinit var tile: Tile
    val location: Int get()  {
        if (tile.ruleType == MapType.Square) {
            return (tile as SquareTile).location
        }
        return 0
    }
}