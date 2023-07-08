package com.russian.desperate.gamedev.gameserver.model.dto

import com.russian.desperate.gamedev.gameserver.model.Cell

data class GameDTO(
    val field: Array<Array<Cell>>,
    val turn: Int
    ) {

}