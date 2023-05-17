package com.russian.desperate.gamedev.gameserver.model.items

import com.russian.desperate.gamedev.gameserver.model.effects.EffectType

interface Item {
    val effectType: EffectType
    val power: Int
}