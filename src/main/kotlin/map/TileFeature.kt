package mcom.map

open class TileFeature(open val name: String = "", val type: FeatureType = FeatureType.None) {

    open var gridName: String = ""

    override fun toString(): String {
        return name
    }
}