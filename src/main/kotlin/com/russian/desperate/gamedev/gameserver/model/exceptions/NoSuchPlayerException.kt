package com.russian.desperate.gamedev.gameserver.model.exceptions

import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.Player

class NoSuchPlayerException(player: Player) : IllegalArgumentException("There is no player $player in game")