package mcom.map

abstract class TileMap {
    open fun generate(type: String) {
        TODO("Not yet implemented")
    }

    abstract val ruleType: MapType
}