package mcom.map

open class TileFeature {
    constructor(name: String = ""): this(name, FeatureType.none)
    constructor(name: String = "", type: FeatureType) {
        this.type = type
        this.name = name
    }
    val type: FeatureType
    val name: String
    open var gridName: String = ""

    override fun toString(): String {
        return name
    }
}