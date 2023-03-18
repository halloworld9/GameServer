package com.russian.desperate.gamedev.gameserver.model.items

import com.russian.desperate.gamedev.gameserver.model.effects.EffectType


object EmptyItem : Item {
    override val effectType = EffectType.NOTHING
    override val power = 0
}