package mcom

import mcom.city.City
import mcom.data.builtins.UnitBuiltins
import mcom.game.Game
import mcom.map.MapType
import mcom.map.square.SquareTile
import mcom.map.square.SquareTileMap
import mcom.map.unit.MapUnit
import mcom.player.Player

fun main() {
    Current.gameInfo = Game()
    val game = Current.gameInfo!!

    for (i in 0..<4) {
        game.addPlayer(Player())
    }
    game.updatePorts()
    game.createNewMap(MapType.Square, 18, "drylands")
    val map = game.currentMap as SquareTileMap
    map.defaultSetPlayers(game.players)
    map.defaultSetVillages()
    game.players.forEach { it.stars = 5 }

    var player = 0
    var turn = 0
    while (true) {
        val curPlayer = game.players[player]
        println("stars: ${curPlayer.stars}")
        for ((index, city) in curPlayer.cities.withIndex()) {
            println("city: $index, location: ${city.tile.locationInfo}")
        }
        for ((index, unit) in curPlayer.units.withIndex()) {
            println("unit: $index, location: ${unit.tile.locationInfo}")
        }
        var test: String?
        do {
            test = readlnOrNull()
        } while (test == null)
        if (test == "end") break
        if (test == "turnE") {
            curPlayer.units.forEach { it.end() }
            player++
            if (player >= game.players.size) {
                turn++
                player = 0
            }
            val newPlayer = game.players[player]
            if (turn != 0) newPlayer.gainSPT()
            for (unit in newPlayer.units)
                unit.startTurn()
            continue
        }
        if (test.startsWith("city")) {
            val numberSt = test.removePrefix("city ")
            val number = numberSt.toInt()
            val city = curPlayer.cities[number]
            parseCity(city)
        }
        if (test.startsWith("unit")) {
            val numberSt = test.removePrefix("unit ")
            val number = numberSt.toInt()
            val unit = curPlayer.units[number]
            parseUnit(unit)
        }
    }
    val test = readlnOrNull()
    print(test)
    val unit = MapUnit()
    unit.maxMovement = 3
    unit.resetMovement()
    val tile = map.tiles.random()
    unit.tile = tile
    tile.unit = unit
    val moveables = unit.getMovableTiles()
    for (y in 0..<map.sizeY) {
        for (x in 0..<map.sizeX) {
            val tile = map.getTile(x, y)
            print(tile)
            //print("$x , $y")
            print(" | ")
        }
        println()
    }
    val name = "Kotlin"
    println("Hello, $name!")

    for (i in 1..5) {
        println("i = $i")
    }
    println("Unit Location:  x:${(unit.tile as SquareTile).x} , y:${(unit.tile as SquareTile).y}")
    for ((tile, amount) in moveables) {
        val tile = tile as SquareTile
        println("x: ${tile.x}, y: ${tile.y}, movement: $amount")
    }
}

fun parseCity(city: City) {
    if (city.tile.unit != null) return
    for (unit in city.buildableUnits) {
        println("${unit.key}, cost: ${unit.value.cityCost}")
    }
    var test: String?
    do {
        test = readlnOrNull()
        if (UnitBuiltins.baseUnits[test] != null) continue
    } while (test == null)
    city.createUnit(test)
}

fun parseUnit(unit: MapUnit) {
    if (!unit.actionsAvailable.contains("move")) return
    val tiles = unit.getMovableTiles().filter { it.key.unit == null }.toList()
    if (tiles.isEmpty()) return
    for ((index, tile) in tiles.withIndex())
        println("$index: ${tile.first.locationInfo}")
    var test: String?
    do {
        test = readlnOrNull()
    } while (test == null)
    val number = test.toInt()
    unit.move(tiles[number].first)
}