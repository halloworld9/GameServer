package com.russian.desperate.gamedev.gameserver.model.mapobjects.units

import com.example.demo.model.effects.StatusEffect
import com.example.demo.model.map_objects.structures.MapObject

interface Unit : MapObject {
    val attack: Int
    val defence: Int
    val health: Int
    val effects: List<StatusEffect>
}