package mcom.map.square

import mcom.city.City
import mcom.data.builtins.Land
import mcom.data.builtins.Water
import mcom.map.MapType
import mcom.map.TileMap
import mcom.player.Player
import kotlin.properties.Delegates

class SquareTileMap : TileMap {
    override val ruleType: MapType = MapType.Square
    val tiles = ArrayList<SquareTile>()
    var wrapX: Boolean = false
    var wrapY: Boolean = false
    var sizeX by Delegates.notNull<Int>()
        private set
    var sizeY by Delegates.notNull<Int>()
        private set

    fun getTile(x: Int, y: Int): SquareTile {
        val place = y * sizeY + x
        return tiles[place]
    }

    constructor()

    constructor(xSize: Int, ySize: Int) {
        this.sizeX = xSize
        this.sizeY = ySize
        for (j in 0..<ySize)
            for (i in 0..<xSize) {
                tiles.add(SquareTile(this, i, j))
            }
        for (tile in tiles) tile.update()
    }
    constructor(size: Int): this(size, size)

    fun setWrapping(wrapX: Boolean = this.wrapX, wrapY: Boolean = this.wrapY) {
        this.wrapX = wrapX
        this.wrapY = wrapY
    }

    override fun generate(type: String) {
        if (type == "drylands") dryLand()
        if (type == "old") oldMap()
    }

    /**
     * Proposed suggestion of how to set up capital locations with a given map
     */
    fun defaultSetPlayers(players: List<Player>) {
        val validTiles = tiles.filter { it.isCityAllowed }
        if (validTiles.size < players.size) throw RuntimeException()
        val capitalMap: HashMap<Player, City> = HashMap()
        // start with random cities for variance
        for (player in players) {
            val city = City("", owningPlayer = player)
            city.level = 1
            city.isCapital = true
            city.originalCapital = true
            city.originalOwner = player
            player.capital = city
            player.cities.add(city)
            capitalMap[player] = city
            val tile = validTiles.random()
            tile.city = city
            city.tile = tile
        }

        // max distance from other capitals
        for (i in 0..<3) {
            for (player1 in players) {
                var largestDistance: SquareDistance? = null
                for (tile in validTiles) {
                    //if (tile.base.name != "land") continue
                    var smallestDistanceFromPlayer: SquareDistance? = null
                    for (player2 in players) {
                        if (player1 === player2) continue
                        val player2Cap = capitalMap[player2]!!
                        val distance = tile.getDistanceTo(player2Cap.tile as SquareTile)
                        if (smallestDistanceFromPlayer == null) {
                            smallestDistanceFromPlayer = distance
                            continue
                        }
                        if (distance < smallestDistanceFromPlayer) {
                            smallestDistanceFromPlayer = distance
                        }
                    }
                    if (smallestDistanceFromPlayer == null) continue

                    if (largestDistance == null) {
                        largestDistance = smallestDistanceFromPlayer
                        continue
                    }
                    if (smallestDistanceFromPlayer > largestDistance) {
                        largestDistance = smallestDistanceFromPlayer
                    }
                }
                if (largestDistance == null) throw RuntimeException()
                val capital = capitalMap[player1]!!
                val tile = capital.tile as SquareTile
                tile.city = null
                capital.tile = largestDistance.tileFrom
                largestDistance.tileFrom.city = capital
            }
        }
    }

    fun defaultSetVillages() {
        val capitals = tiles.filter { it.city?.isCapital == true }
        val distances = capitals.associateWith { 1 }.toMutableMap()
        if (capitals.isEmpty()) return // TODO
        var i = 0
        val tilesChecked = ArrayList<SquareTile>(capitals)
        while (tilesChecked.size < tiles.size) {
            val capital = capitals[i]
            val distance = distances[capital]!!
            if (distance > sizeX && distance > sizeY) throw RuntimeException("Capital is checking beyond the intended range (infinite loop?)")
            val tiles = capital.getOuterEdge(distance).shuffled().toMutableList()
            if (tiles.isEmpty()) {
                i++
                if (i == capitals.size) i = 0
                continue
            }
            var tile: SquareTile?
            do {
                tile = tiles.removeLastOrNull()
                if (tile == null) break
                if (tile in tilesChecked) {
                    tile = null
                    continue
                }
                tilesChecked.add(tile)
                if (!tile.isCityAllowed) {
                    tile = null
                }
            } while (tile == null)
            if (tile == null) {
                distances[capital] = distances[capital]!! + 1
                continue
            }
            tile.city = City("", tile)
            i++
            if (i == capitals.size) i = 0
        }
    }

    fun dryLand() {
        if (sizeY * sizeX != tiles.size) throw IllegalStateException("Map is not correct")
        for (tile in tiles) {
            tile.base = Land()
        }
    }

    fun oldMap() {
        if (sizeY <= 0) throw IllegalStateException("Height is not set")
        if (sizeX <= 0) throw IllegalStateException("Width is not set")
        if (sizeY * sizeX != tiles.size) throw IllegalStateException("Map is not correct")
        for (tile in tiles) {
            tile.base = Water()
        }
        run {
            val half = tiles.size / 2
            var i = 0
            while (i < half) {
                val landTile = tiles.random()
                if (landTile.base.name != "land") {
                    landTile.base = Land()
                    i++
                }
            }
        }
        run {
            val nextToLand = BooleanArray(tiles.size)
            for (j in 0..<3) {
                for ((i, tile) in tiles.withIndex()) {
                    val nearBy = tile.neighborsWithSelf as List<SquareTile>
                    val half = nearBy.size / 2
                    val landCount = nearBy.count { it.base.name == "land" }
                    if (half > landCount) {
                        nextToLand[i] = false
                    }
                    if (half < landCount) {
                        nextToLand[i] = true
                    }
                    if (half == landCount) {
                        if (nearBy.size % 2 == 0)
                            nextToLand[i] = tile.base.name == "land"
                        else nextToLand[i] = false
                    }
                }
                for (i in tiles.indices) {
                    if (nextToLand[i]){
                        tiles[i].base = Land()
                    }
                    else tiles[i].base = Water()
                }
            }
        }
    }
}