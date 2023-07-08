package com.russian.desperate.gamedev.gameserver.controller

import com.russian.desperate.gamedev.gameserver.model.GameManager
import com.russian.desperate.gamedev.gameserver.model.dto.GameDTO
import com.russian.desperate.gamedev.gameserver.model.dto.User
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.*


@RestController
class GameManagerController {
    final val gameManager = GameManager()
    init {
        gameManager.addUser(User("Vitaliy"))
        gameManager.addUser(User("Stas"))
        gameManager.startGame()
    }
    @GetMapping("/api/game")
    fun shit(response: ServerHttpResponse): GameDTO {
        response.headers.add("Access-Control-Allow-Origin", "*")
        response.headers.add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept")
        return GameDTO(gameManager.game.gameField, gameManager.game.turn)
    }

    class Num(val n: Int)
}
