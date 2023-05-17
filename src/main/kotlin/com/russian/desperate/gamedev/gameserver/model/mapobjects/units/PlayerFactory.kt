package com.russian.desperate.gamedev.gameserver.model.mapobjects.units

import java.util.*


class PlayerFactory {
    companion object {
        fun createPlayer(playerClass: PlayerClass, id: Int): Player {
            return when (playerClass) {
                PlayerClass.WARRIOR -> Player(5, 5, 10, playerClass, id=id)
                PlayerClass.MAGE -> Player(7, 3, 10, playerClass, id=id)
                PlayerClass.ARCHER -> Player(7, 3, 10, playerClass, id=id)
                PlayerClass.ASSASSIN -> Player(9, 1, 10, playerClass, id=id)
                PlayerClass.RANDOM -> Player(0, 0, 0, playerClass, id=id)
            }
        }

        fun createPlayer(gameClass: String, id: Int): Player {
            return createPlayer(PlayerClass.valueOf(gameClass.uppercase(Locale.getDefault())), id)
        }
    }
}