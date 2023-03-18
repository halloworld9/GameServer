package com.russian.desperate.gamedev.gameserver.model.mapobjects.units

import java.util.Random

enum class PlayerClass {
    WARRIOR,
    MAGE,
    ARCHER,
    ASSASSIN,
    RANDOM;

    companion object {
        private val values = PlayerClass.values()

        fun getRandomClass(): PlayerClass {
            return values[Random().nextInt(4)]
        }
    }
}