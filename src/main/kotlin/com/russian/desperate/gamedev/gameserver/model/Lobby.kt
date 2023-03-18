package com.russian.desperate.gamedev.gameserver.model


import com.russian.desperate.gamedev.gameserver.model.dto.User
import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.PlayerClass
import java.io.Serializable

class Lobby : Serializable {
    private val users = HashMap<User, PlayerClass>()

    fun addUser(user: User): Boolean {
        if (users.size < MAX_PLAYERS) {
            users[user] = PlayerClass.RANDOM
            return true
        }
        return false
    }

    fun removeUser(user: User): Boolean {
        val a = users.remove(user)
        return a != null
    }

    fun getUsers(): ArrayList<String> {
        val users = ArrayList<String>()
        for (i in this.users.values)
            users.add(i.toString())
        return users
    }

    fun getUsersAndClasses(): ArrayList<Pair<String, PlayerClass>> {
        val pairs = ArrayList<Pair<String, PlayerClass>>()
        for (i in users.entries)
            pairs.add(Pair(i.key.name, i.value))

        return pairs
    }

}

private const val MAX_PLAYERS = 4