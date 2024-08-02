package mcom.data.builtins

import mcom.map.FeatureType
import mcom.map.TileFeature

class Water: TileFeature("water", FeatureType.base) {
    override var gridName = "W"
}

class Land: TileFeature("land", FeatureType.base) {
    override var gridName = "L"
}

class Forest: TileFeature("forest", FeatureType.terrain) {
    override var gridName = "F"
}

class Mountain: TileFeature("mountain", FeatureType.terrain) {
    override var gridName = "M"
}

class Blank: TileFeature("none", FeatureType.base) {
    override var gridName = ""
}
