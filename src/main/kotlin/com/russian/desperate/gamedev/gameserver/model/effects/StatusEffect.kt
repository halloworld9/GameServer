package com.russian.desperate.gamedev.gameserver.model.effects

import java.io.Serializable

interface StatusEffect : Serializable {
    val effectType: EffectType
    val duration: Int
    val power: Int
}