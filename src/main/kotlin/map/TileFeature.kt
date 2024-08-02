package mcom.map

open class TileFeature(open val name: String = "", val type: FeatureType) {
    constructor(name: String = ""): this(name, FeatureType.none)

    open var gridName: String = ""

    override fun toString(): String {
        return name
    }
}