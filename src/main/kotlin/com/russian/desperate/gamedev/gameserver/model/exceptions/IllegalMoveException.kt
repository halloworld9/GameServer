package com.russian.desperate.gamedev.gameserver.model.exceptions

import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.Player

class IllegalMoveException(player: Player, x: Int, y: Int) : IllegalArgumentException("Player $player can't move to $x:$y")