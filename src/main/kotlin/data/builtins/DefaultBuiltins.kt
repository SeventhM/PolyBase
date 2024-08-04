package mcom.data.builtins

import mcom.map.FeatureType
import mcom.map.TileFeature

class Water: TileFeature("water", FeatureType.Base) {
    override var gridName = "W"
}

class Land: TileFeature("land", FeatureType.Base) {
    override var gridName = "L"
}

class Forest: TileFeature("forest", FeatureType.Terrain) {
    override var gridName = "F"
}

class Mountain: TileFeature("mountain", FeatureType.Terrain) {
    override var gridName = "M"
}

class Blank: TileFeature("none", FeatureType.Base) {
    override var gridName = ""
}
