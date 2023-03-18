package com.russian.desperate.gamedev.gameserver.model

import com.russian.desperate.gamedev.gameserver.model.mapobjects.structures.EmptyObject
import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.PlayerClass
import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.Player
import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.PlayerFactory
import java.io.Serializable


class Game(usersAndClasses: ArrayList<Pair<String, PlayerClass>>) : Serializable {
    private val gameField: Array<Array<Cell>> = Array(20) { Array(20) { Cell(EmptyObject()) } }
    private val players: HashMap<String, Player> = HashMap()
    private val playersCoordinates: HashMap<String, Pair<Int, Int>> = HashMap()
    private val playerFactory = PlayerFactory();

    init {
        for (i in usersAndClasses)
            players[i.first] = playerFactory.createPlayer(i.second)
    }



}
