package com.russian.desperate.gamedev.gameserver.model.exceptions

import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.Player

class PlayerWasKilledException(val player: Player) : RuntimeException("$player was killed")