package mcom.data.builtins

import mcom.map.unit.MapUnit

object UnitBuiltins {
    val unitTypes = listOf("Warrior", "Rider", "Swordsman", "Defender", "Archer")
    val baseUnits = unitTypes.filter { getUnit(it) != null }.associateWith { getUnit(it)!! }

    fun getUnit(unit: String): MapUnit? {
        when(unit) {
            "Warrior" -> return warrior
            "Rider" -> return rider
            "Swordsman" -> return swordsman
            "Defender" -> return defender
            "Archer" -> return archer
            else -> return null
        }
    }
}

val warrior get() = MapUnit().apply {
    unitType = "Warrior"
    cityCost = 2
    attack = 2.0
    defense = 2.0
    retaliationAttk = 2.0
    maxMovement = 1
    maxHealth = 10
    abilities.add("dash")
    abilities.add("fortify")
}

val rider get() = MapUnit().apply {
    unitType = "Rider"
    cityCost = 3
    attack = 2.0
    defense = 1.0
    retaliationAttk = 1.0
    maxMovement = 2
    maxHealth = 10
    abilities.add("dash")
    abilities.add("fortify")
    abilities.add("escape")
}

val swordsman get() = MapUnit().apply {
    unitType = "Swordsman"
    cityCost = 5
    attack = 3.0
    defense = 3.0
    retaliationAttk = 3.0
    maxMovement = 1
    maxHealth = 10
    abilities.add("dash")
    abilities.add("fortify")
}

val defender get() = MapUnit().apply {
    unitType = "Defender"
    cityCost = 3
    attack = 1.0
    defense = 3.0
    retaliationAttk = 3.0
    maxMovement = 1
    maxHealth = 10
    abilities.add("fortify")
}

val archer get() = MapUnit().apply {
    unitType = "Archer"
    range = 2
    cityCost = 3
    attack = 2.0
    defense = 1.0
    retaliationAttk = 1.0
    maxMovement = 1
    maxHealth = 10
    abilities.add("dash")
    abilities.add("fortify")
}