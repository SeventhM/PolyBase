package mcom.map.square

import mcom.city.City
import mcom.data.builtins.Land
import mcom.data.builtins.Water
import mcom.map.TileMap
import mcom.player.Player
import kotlin.properties.Delegates

class SquareTileMap : TileMap {
    val tiles = ArrayList<SquareTile>()
    var wrapX: Boolean = false
    var wrapY: Boolean = false
    var width by Delegates.notNull<Int>()
        private set
    var height by Delegates.notNull<Int>()
        private set

    fun getTile(x: Int, y: Int): SquareTile {
        val place = y * height + x
        return tiles[place]
    }

    constructor()

    constructor(width: Int,  height: Int) {
        this.width = width
        this.height = height
        for (j in 0..<height)
            for (i in 0..<width) {
                tiles.add(SquareTile(this, i, j))
            }
    }
    constructor(size: Int): this(size, size)

    fun setWrapping(wrapX: Boolean = this.wrapX, wrapY: Boolean = this.wrapY) {
        this.wrapX = wrapX
        this.wrapY = wrapY
    }

    /**
     * Proposed suggestion of how to set up capital locations with a given map
     */
    fun defaultSetPlayers(players: List<Player>) {
        val capitalMap: HashMap<Player, City> = HashMap()
        // start with random cities for variance
        for (player in players) {
            val city = City("")
            city.isCapital = true
            city.originalCapital = true
            city.originalOwner = "player_${player.name}port${player.playerPort}"
            city.owner = "player_${player.name}port${player.playerPort}"
            capitalMap[player] = city
            var tile = tiles.random()
            while (tile.city != null && tile.base.name != "land") tile = tiles.random()
            tile.city = city
            city.tile = tile
        }

        // max distance from other capitals
        for (i in 0..<3) {
            for (player1 in players) {
                var largestDistance: SquareDistance? = null
                for (tile in tiles) {
                    if (tile.base.name != "land") continue
                    var smallestDistanceFromPlayer: SquareDistance? = null
                    for (player2 in players) {
                        if (player1 === player2) continue
                        val player2Cap = capitalMap[player2]!!
                        val distance = tile.getDistanceTo(player2Cap.tile as SquareTile)
                        if (smallestDistanceFromPlayer == null) {
                            smallestDistanceFromPlayer = distance
                            continue
                        }
                        if (distance.distance < smallestDistanceFromPlayer.distance) {
                            smallestDistanceFromPlayer = distance
                            continue
                        }
                        if (distance.distance == smallestDistanceFromPlayer.distance &&
                            distance.secondaryDistance < smallestDistanceFromPlayer.secondaryDistance
                        ) {
                            smallestDistanceFromPlayer = distance
                            continue
                        }
                    }
                    if (smallestDistanceFromPlayer == null) continue

                    if (largestDistance == null) {
                        largestDistance = smallestDistanceFromPlayer
                        continue
                    }
                    if (smallestDistanceFromPlayer.distance > largestDistance.distance) {
                        largestDistance = smallestDistanceFromPlayer
                        continue
                    }
                    if (smallestDistanceFromPlayer.distance == largestDistance.distance &&
                        smallestDistanceFromPlayer.secondaryDistance > largestDistance.secondaryDistance
                    ) {
                        largestDistance = smallestDistanceFromPlayer
                        continue
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

    fun dryLand() {
        if (height * width != tiles.size) throw IllegalStateException("Map is not correct")
        for (tile in tiles) {
            tile.base = Land()
        }
    }

    fun oldMap() {
        if (height <= 0) throw IllegalStateException("Height is not set")
        if (width <= 0) throw IllegalStateException("Width is not set")
        if (height * width != tiles.size) throw IllegalStateException("Map is not correct")
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
                for (i in tiles.indices) {
                    val tile = tiles[i]
                    val nearBy = tile.neighborsWithSelf as MutableList<SquareTile>
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