package mcom.map

open class Tile {
    var features = ArrayList<TileFeature>()
    open lateinit var map: TileMap

    override fun toString(): String {
        return features.toString()
    }
}