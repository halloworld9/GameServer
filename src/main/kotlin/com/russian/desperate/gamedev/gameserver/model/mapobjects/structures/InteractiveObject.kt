package com.russian.desperate.gamedev.gameserver.model.mapobjects.structures

import com.example.demo.model.actions.Action
import com.example.demo.model.map_objects.units.Player

interface InteractiveObject : MapObject {

    fun interact(player: Player): Action

}