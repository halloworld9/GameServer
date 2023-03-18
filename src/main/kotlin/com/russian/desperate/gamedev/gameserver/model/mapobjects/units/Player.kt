package com.russian.desperate.gamedev.gameserver.model.mapobjects.units

import com.russian.desperate.gamedev.gameserver.model.effects.StatusEffect
import com.russian.desperate.gamedev.gameserver.model.items.EmptyItem
import com.russian.desperate.gamedev.gameserver.model.items.Item

class Player : Unit {
    override var attack: Int
    override var defence: Int
    override var health: Int
    val playerClass: PlayerClass
    var level = 1
    var experience = 0
    val inventory = Array<Item>(10) { EmptyItem }
    override val effects: List<StatusEffect>

    constructor(attack: Int, defence: Int, health: Int, playerClass: PlayerClass) {
        this.attack = attack
        this.defence = defence
        this.health = health
        this.playerClass = playerClass
        this.effects = ArrayList()
    }


}