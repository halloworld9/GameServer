package com.russian.desperate.gamedev.gameserver.model.effects

interface StatusEffect {
    val effectType: EffectType
    val duration: Int
    val power: Int
}