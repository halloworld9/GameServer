package com.russian.desperate.gamedev.gameserver.model

class GameFieldFactory {

    companion object {
        fun createField(playerCount: Int): Array<Array<Cell>> {
            val field = Array(20) { Array(20) { Cell() } }
            return field
        }

        fun getStartPosition(playerCount: Int): ArrayList<Pair<Int, Int>> {
            val positions = ArrayList<Pair<Int, Int>>(playerCount)
            for (i in 0 until playerCount)
                positions.add(Pair(0, 0))

            return positions
        }
    }
}