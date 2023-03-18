package com.russian.desperate.gamedev.gameserver.model.items

import com.russian.desperate.gamedev.gameserver.model.effects.EffectType
import java.io.Serializable

interface Item : Serializable {
    val effectType: EffectType
    val power: Int
}