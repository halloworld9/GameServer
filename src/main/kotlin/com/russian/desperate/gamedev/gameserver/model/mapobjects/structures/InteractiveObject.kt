package com.russian.desperate.gamedev.gameserver.model.mapobjects.structures

import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.Player


interface InteractiveObject : MapObject {

    fun interact(player: Player)
    fun getInteractDescription(): String

}