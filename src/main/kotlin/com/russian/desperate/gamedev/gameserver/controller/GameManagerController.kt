package com.russian.desperate.gamedev.gameserver.controller

import com.russian.desperate.gamedev.gameserver.model.Coordinates
import com.russian.desperate.gamedev.gameserver.model.GameManager
import com.russian.desperate.gamedev.gameserver.model.dto.GameDTO
import com.russian.desperate.gamedev.gameserver.model.dto.User
import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.Unit
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.*


@RestController
class GameManagerController {
    final val gameManager = GameManager()
    final val user = User("Vitaliy")
    init {
        gameManager.addUser(user)
        gameManager.startGame()
    }
    @GetMapping("/api/game")
    fun getGame(response: ServerHttpResponse): GameDTO {
        response.headers.add("Access-Control-Allow-Origin", "*")
        response.headers.add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept")
        return GameDTO(gameManager.game.gameField, gameManager.game.turn)
    }

    @GetMapping("/api/moves")
    fun getMoves(response: ServerHttpResponse): Array<Array<Int>> {
        response.headers.add("Access-Control-Allow-Origin", "*")
        response.headers.add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept")
        return gameManager.getPlayerMoves(user)
    }

    @GetMapping("/api/move")
    fun getPath(response: ServerHttpResponse, x: Int, y: Int): Pair<List<Coordinates>, Unit?> {
        response.headers.add("Access-Control-Allow-Origin", "*")
        response.headers.add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept")
        val path = gameManager.movePlayer(user, x, y)
        var unit: Unit? = null
        if (path.isNotEmpty())
            unit = gameManager.tryStartBattle(user)
        return Pair(path, unit)
    }

    @GetMapping("/api/pair")
    fun getPair(response: ServerHttpResponse): Pair<Int, Int> {
        response.headers.add("Access-Control-Allow-Origin", "*")
        response.headers.add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept")
        return Pair(1, 2)
    }


    class Num(val n: Int)
}
