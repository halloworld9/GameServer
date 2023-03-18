package com.russian.desperate.gamedev.gameserver.model.mapobjects.units

import com.russian.desperate.gamedev.gameserver.model.effects.StatusEffect
import com.russian.desperate.gamedev.gameserver.model.mapobjects.structures.MapObject


interface Unit : MapObject {
    val attack: Int
    val defence: Int
    val health: Int
    val effects: List<StatusEffect>
}