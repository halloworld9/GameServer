package com.russian.desperate.gamedev.gameserver.model

import com.russian.desperate.gamedev.gameserver.model.dto.User
import com.russian.desperate.gamedev.gameserver.model.exceptions.NoSuchUserException
import com.russian.desperate.gamedev.gameserver.model.exceptions.PlayerWasKilledException
import com.russian.desperate.gamedev.gameserver.model.exceptions.UserLoosedException
import com.russian.desperate.gamedev.gameserver.model.mapobjects.structures.InteractiveObject
import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.Player
import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.PlayerClass
import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.PlayerFactory

class GameManager {
    val lobby = Lobby()
    lateinit var game: Game
        private set
    private val userPlayerMap = HashMap<User, Player>()

    fun startGame() {
        if (!this::game.isInitialized) {
            for ((id, i) in lobby.getUsersAndClasses().withIndex()) {
                val user = i.first
                val player = PlayerFactory.createPlayer(i.second, id)
                userPlayerMap[user] = player
            }
            game = Game(userPlayerMap.values)
        }
    }

    fun movePlayer(user: User, x: Int, y: Int): Boolean {
        val player = userPlayerMap[user] ?: throw NoSuchUserException(user)

        try {
            game.movePlayer(player, x, y)
        } catch (exception: PlayerWasKilledException) {
            for (entry in userPlayerMap.entries) {
                if (entry.value == exception.player) {
                    userPlayerMap.remove(entry.key)
                    throw UserLoosedException(entry.key)
                }
            }
        }
        return true
    }

    fun getInteractiveObjects(user: User): Set<InteractiveObject> {
        val player = userPlayerMap[user] ?: throw NoSuchUserException(user)
        return game.getInteractiveObjects(player)
    }

    fun activateInteractiveObject(user: User, interactiveObject: InteractiveObject): Action {
        val player = userPlayerMap[user] ?: throw NoSuchUserException(user)
        return game.activateInteractiveObject(player, interactiveObject)
    }

    fun getPath(user: User, x: Int, y: Int): ArrayList<Pair<Int, Int>> {
        val player = userPlayerMap[user] ?: throw NoSuchUserException(user)
        return game.getPlayersPath(player, x, y)
    }

    fun getPlayerMoves(user: User): Array<Array<Int>> {
        val player = userPlayerMap[user] ?: throw NoSuchUserException(user)
        return game.getPlayerMoves(player)
    }

    fun startTurn(user: User): Player {
        val player = userPlayerMap[user] ?: throw NoSuchUserException(user)
        return game.startTurn(player)
    }

    fun endTurn(user: User) {
        val player = userPlayerMap[user] ?: throw NoSuchUserException(user)
        game.endTurn(player)
    }

    fun addUser(user: User): Boolean {
        return lobby.addUser(user)
    }

    fun setPlayerClass(user: User, playerClass: PlayerClass): Boolean {
        return lobby.setPlayerClass(user, playerClass)
    }

    fun removeUser(user: User): Boolean {
        return lobby.removeUser(user)
    }

    fun getPlayers(): ArrayList<String> {
        return lobby.getUsernames()
    }

    fun containsUser(user: User): Boolean {
        return lobby.containsUser(user)
    }

    override fun toString(): String {
        return "GameManager(lobby=$lobby, game=$game)"
    }

}