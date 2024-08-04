package mcom.player

class Player {
    var name: String = ""
    var playerPort: Int = -1
    val ownerText get() = "player_${name}port${playerPort}"
}