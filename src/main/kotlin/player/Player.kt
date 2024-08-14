package mcom.player

import mcom.city.City
import mcom.map.unit.MapUnit

class Player {
    var name: String = ""
    var playerPort: Int = -1
    val ownerText get() = "player_${name}port${playerPort}"
    var capital: City? = null
    var stars = 0
    val abilities = ArrayList<String>()
    var cities = ArrayList<City>()
    var units = ArrayList<MapUnit>()

    fun gainSPT() {
        for (city in cities) {
            stars += city.baseSPT
            if (city.isCapital) stars += 1
        }
    }

    fun capture(city: City, unit: MapUnit? = null) {
        cities.add(city)
        city.owningPlayer = this
        if (city.tile.owner == null) city.tile.owner = city
        if (city.level == 0) city.level = 1
    }
}