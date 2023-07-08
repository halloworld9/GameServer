package com.russian.desperate.gamedev.gameserver.model

import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.Unit

class Battle(private val firstUnit: Unit, private val secondUnit: Unit) {
    private var order = true
    private fun attack() {
        if (order) {
            secondUnit.health -= firstUnit.attack - secondUnit.defence
        } else {
            firstUnit.health -= secondUnit.attack - firstUnit.defence
        }
        order = !order
    }


    /**
     * @return unit to delete from cell
     */
    fun startBattle(): Unit {
        while (firstUnit.health > 0 && secondUnit.health > 0) {
            attack()
        }
        return if (firstUnit.health < 0) firstUnit else secondUnit
    }
}