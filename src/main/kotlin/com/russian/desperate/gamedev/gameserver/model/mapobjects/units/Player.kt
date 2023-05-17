package com.russian.desperate.gamedev.gameserver.model.mapobjects.units

import com.russian.desperate.gamedev.gameserver.model.effects.StatusEffect
import com.russian.desperate.gamedev.gameserver.model.items.EmptyItem
import com.russian.desperate.gamedev.gameserver.model.items.Item

class Player (
    override var attack: Int,
    override var defence: Int,
    override var health: Int,
    val playerClass: PlayerClass,
    var energy: Int = 5, //random num
    val id: Int
) : Unit {
    override val isPassable = true
    var level = 1
    var experience = 0
    val inventory = Array<Item>(10) { EmptyItem }
    override val effects: List<StatusEffect>

    init {
        this.effects = ArrayList()
    }


}