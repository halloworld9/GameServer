package com.russian.desperate.gamedev.gameserver.model


import com.russian.desperate.gamedev.gameserver.model.dto.User
import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.PlayerClass


class Lobby {
    private val users = HashMap<User, PlayerClass>()

    fun addUser(user: User): Boolean {
        if (users.size < MAX_PLAYERS) {
            users[user] = PlayerClass.RANDOM
            return true
        }
        return false
    }

    fun setPlayerClass(user: User, playerClass: PlayerClass): Boolean {
        return if (users.containsKey(user)) {
            users[user] = playerClass
            true
        } else
            false
    }

    fun removeUser(user: User): Boolean {
        val a = users.remove(user)
        return a != null
    }

    fun getUsernames(): ArrayList<String> {
        val users = ArrayList<String>()
        for (i in this.users.keys)
            users.add(i.toString())
        return users
    }

    fun getUsersAndClasses(): ArrayList<Pair<User, PlayerClass>> {
        val pairs = ArrayList<Pair<User, PlayerClass>>()
        for (i in users.entries)
            pairs.add(Pair(i.key, i.value))

        return pairs
    }

    fun containsUser(user: User): Boolean {
        return users.containsKey(user)
    }
}

private const val MAX_PLAYERS = 4
