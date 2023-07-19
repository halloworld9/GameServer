package com.russian.desperate.gamedev.gameserver.model

import com.russian.desperate.gamedev.gameserver.model.exceptions.*
import com.russian.desperate.gamedev.gameserver.model.mapobjects.structures.InteractiveObject
import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.Player
import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.Unit
import java.util.*
import kotlin.collections.ArrayList

class Game(playerClasses: Collection<Player>) {

    private val playerMoves = HashMap<Player, Array<Array<Int>>>()
    val gameField: Array<Array<Cell>> = GameFieldFactory.createField(playerClasses.size)
    private val playerCoordinates: HashMap<Player, Coordinates> = HashMap()
    var turn = 0
        private set
    private val playerOrder = ArrayList<Player>(playerClasses.size)

    init {
        val startPositions = GameFieldFactory.getStartPosition(playerClasses.size)
        playerClasses.forEachIndexed { i, e ->
            val currentPosition = startPositions[i]
            playerOrder.add(e)
            playerCoordinates[e] = currentPosition
            gameField[currentPosition.x][currentPosition.y].addMapObject(e)
        }

        initPlayerMoves()
        updatePlayersMoveMatrix(playerOrder[0])
    }

    private fun initPlayerMoves() {
        for (player in playerOrder)
            playerMoves[player] = Array(gameField.size) { Array(gameField[0].size) { 0 } }
    }

    fun endTurn(player: Player) {
        if (player != currentPlayer()) throw IllegalTurnOrderException(player)
        turn++
    }

    fun getInteractiveObjects(player: Player): Set<InteractiveObject> {
        if (player != currentPlayer()) throw IllegalTurnOrderException(player)

        val coordinates = playerCoordinates[player] ?: throw NoSuchPlayerException(player)
        return gameField[coordinates.x][coordinates.y].interactiveObjects
    }

    fun activateInteractiveObject(player: Player, interactiveObject: InteractiveObject) {
        if (player != currentPlayer()) throw IllegalTurnOrderException(player)

        val coordinates = playerCoordinates[player] ?: throw NoSuchPlayerException(player)
        if (gameField[coordinates.x][coordinates.y].interactiveObjects.contains(interactiveObject))
            interactiveObject.interact(player)
        else
            throw NoSuchInteractiveObjectException(interactiveObject)
    }

    fun getInteractiveObjectDescription(player: Player, interactiveObject: InteractiveObject): String {
        if (player != currentPlayer()) throw IllegalTurnOrderException(player)

        val coordinates = playerCoordinates[player] ?: throw NoSuchPlayerException(player)
        if (gameField[coordinates.x][coordinates.y].interactiveObjects.contains(interactiveObject))
            return interactiveObject.getInteractDescription()

        throw NoSuchInteractiveObjectException(interactiveObject)
    }

    fun isPlayerPlaying(player: Player) : Boolean {
        return playerMoves.containsKey(player)
    }

    /**
     * @return player, which will make current move
     */
    fun startTurn(player: Player): Player {
        val currentPlayer = currentPlayer()
        if (player != currentPlayer) throw IllegalTurnOrderException(player)

        updatePlayersMoveMatrix(currentPlayer)
        return currentPlayer
    }

    private fun currentPlayer() : Player {
        return playerOrder[turn % playerOrder.size]
    }

    fun movePlayer(player: Player, x: Int, y: Int) : List<Coordinates> {
        if (player != currentPlayer()) throw IllegalTurnOrderException(player)

        val playerCoordinates = playerCoordinates[player] ?: throw NoSuchPlayerException(player)
        val playerMoves = this.playerMoves[player] ?: throw NoSuchPlayerException(player)
        if (!checkMove(player, x, y)) return ArrayList()
        val path = getPath(player, x, y)
        gameField[playerCoordinates.x][playerCoordinates.y].removeMapObject(player)
        player.energy -= playerMoves[x][y]
        gameField[x][y].addMapObject(player)
        this.playerCoordinates[player] = Coordinates(x, y)
        return path
    }

    fun tryStartBattle(player: Player): Unit? {
        if (player != currentPlayer()) throw IllegalTurnOrderException(player)
        val playerCoordinates = playerCoordinates[player] ?: throw NoSuchPlayerException(player)
        val units = gameField[playerCoordinates.x][playerCoordinates.y].units
        if (!units.contains(player))
            throw NoSuchPlayerException(player)
        if (units.size == 1)
            return null

        for (i in units)
            if (i != player)
                return startBattle(player, i)

        return null
    }

    private fun startBattle(player: Player, secondUnit: Unit): Unit {
        return Battle(player, secondUnit).startBattle()
    }

    private fun checkMove(player: Player, x: Int, y: Int): Boolean {
        val playerMoves = this.playerMoves[player] ?: throw NoSuchPlayerException(player)
        return playerMoves[x][y] > 0 && player.energy >= playerMoves[x][y]
    }

    fun getPlayerMoves(player: Player): Array<Array<Int>> {
        return playerMoves[player] ?: throw NoSuchPlayerException(player)
    }

    private fun setMatrixBase(player: Player): Array<Array<Int>> {
        if (player != currentPlayer()) throw IllegalTurnOrderException(player)
        val playerCoordinate = playerCoordinates[player]?: throw NoSuchPlayerException(player)
        val PASSABLE = 0
        val UNPASSABLE = -1
        val UNIT = -2
        val matrix = playerMoves[player] ?: throw NoSuchPlayerException(player)
        for (x in gameField.indices) {
            for (y in gameField[0].indices) {
                if (!gameField[x][y].isPassable)
                    matrix[x][y] = UNPASSABLE
                else if (gameField[x][y].hasUnit)
                    matrix[x][y] = UNIT
                else
                    matrix[x][y] = PASSABLE
            }
        }
        matrix[playerCoordinate.x][playerCoordinate.y] = 0
        return matrix
    }

    /**
     * Updates player's moveMatrix
     * @param player, which path you want to update
     */
    private fun updatePlayersMoveMatrix(player: Player) {
        val moves = setMatrixBase(player)
        val coordinates = playerCoordinates[player] ?: throw NoSuchPlayerException(player)
        val UNIT = -2
        val PASSABLE = 0

        val x = coordinates.x
        val y = coordinates.y
        val queue = LinkedList<Pair<Int, Int>>()
        queue.add(Pair(x, y))
        while (queue.size > 0) {
            val cur = queue.pop()
            val curX = cur.first
            val curY = cur.second
            val curValue = moves[curX][curY]
            for (i in curX - 1..curX + 1) {
                if (i < 0)
                    continue
                if (i >= moves.size)
                    break
                for (j in curY - 1..curY + 1) {
                    if (j < 0)
                        continue
                    if (j >= moves[0].size)
                        break
                    if (moves[i][j] == PASSABLE) {
                        queue.add(Pair(i, j))
                        moves[i][j] = curValue + 1
                    } else if (moves[i][j] == UNIT) {
                        moves[i][j] = curValue + 1
                    }
                }
            }
        }
        moves[x][y] = 0
    }
    /**
     * @param player - player, which path you want to get
     * @param x - target x
     * @param y - target y
     * @return list of coordinates
     */
    fun getPath(player: Player, x: Int, y: Int): List<Coordinates> {
        val moves = ArrayList<Coordinates>()
        val coordinates = playerCoordinates[player] ?: throw NoSuchPlayerException(player)
        val moveMatrix = playerMoves[player] ?: throw NoSuchPlayerException(player)
        if (moveMatrix[x][y] <= 0 && moveMatrix[x][y] != -2) return moves
        val startX = coordinates.x
        val startY = coordinates.y
        var curX = x
        var curY = y
        moves.add(Coordinates(x, y))
        while (curX != startX || curY != startY) {
            val currentValue = moveMatrix[curX][curY]
            outerLoop@ for (i in curX - 1..curX + 1) {
                if (i < 0)
                    continue
                if (i >= moveMatrix.size)
                    break
                for (j in curY - 1..curY + 1) {
                    if (j < 0)
                        continue
                    if (j >= moveMatrix[0].size)
                        break
                    if (currentValue - moveMatrix[i][j] == 1 && moveMatrix[i][j] != -1) {
                        if (moveMatrix[i][j] != 0)
                            moves.add(Coordinates(i, j))
                        curX = i
                        curY = j
                        break@outerLoop
                    }
                }
            }
        }
        return moves.reversed()
    }
}
