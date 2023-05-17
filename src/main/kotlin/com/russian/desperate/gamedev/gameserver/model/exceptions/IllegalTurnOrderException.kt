package com.russian.desperate.gamedev.gameserver.model.exceptions

import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.Player

class IllegalTurnOrderException(player: Player) : IllegalArgumentException("Now $player can't make a move")