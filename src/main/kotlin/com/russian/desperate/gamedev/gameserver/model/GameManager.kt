package com.russian.desperate.gamedev.gameserver.model

import com.russian.desperate.gamedev.gameserver.model.dto.User

class GameManager {
    private val lobby = Lobby()
    private var game: Game? = null

    fun addUser(user: User): Boolean {
        return lobby.addUser(user)
    }

    fun removeUser(user: User): Boolean {
        return lobby.removeUser(user)
    }

    fun startGame() {
        if (game == null) {
            game = Game(lobby.getUsersAndClasses())
        }
    }

}