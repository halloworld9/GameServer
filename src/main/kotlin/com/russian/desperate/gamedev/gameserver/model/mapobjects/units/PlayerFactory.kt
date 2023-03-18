package com.russian.desperate.gamedev.gameserver.model.mapobjects.units

import java.util.*

class PlayerFactory {
    fun createPlayer(playerClass: PlayerClass): Player {
        return when (playerClass) {
            PlayerClass.WARRIOR -> Player(5, 5, 10, playerClass)
            PlayerClass.MAGE -> Player(7, 3, 10, playerClass)
            PlayerClass.ARCHER -> Player(7, 3, 10, playerClass)
            PlayerClass.ASSASSIN -> Player(9, 1, 10, playerClass)
            PlayerClass.RANDOM -> Player(0, 0, 0, playerClass)
        }
    }

    fun createPlayer(gameClass: String): Player {
        return createPlayer(PlayerClass.valueOf(gameClass.uppercase(Locale.getDefault())))
    }
}