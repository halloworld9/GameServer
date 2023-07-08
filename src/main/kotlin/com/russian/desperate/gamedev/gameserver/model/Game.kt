package com.russian.desperate.gamedev.gameserver.model

import com.russian.desperate.gamedev.gameserver.model.actions.Action
import com.russian.desperate.gamedev.gameserver.model.exceptions.IllegalMoveException
import com.russian.desperate.gamedev.gameserver.model.exceptions.IllegalTurnOrderException
import com.russian.desperate.gamedev.gameserver.model.exceptions.NoSuchInteractiveObjectException
import com.russian.desperate.gamedev.gameserver.model.exceptions.NoSuchPlayerException
import com.russian.desperate.gamedev.gameserver.model.mapobjects.structures.InteractiveObject
import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.Player
import java.util.*

class Game(playerClasses: Collection<Player>) {

    /**
     * playerMoves contains matrix of move codes
     *
     * -2 is cell with Unit
     *
     * -1 is unpassable Cell
     *
     * 0 is current position
     *
     * &gt;0 is distance to Cell
     *
     */
    private val playerMoves = HashMap<Player, Array<Array<Int>>>()
    private val gameField: Array<Array<Cell>> = GameFieldFactory.createField(playerClasses.size)
    private val playerCoordinates: HashMap<Player, Pair<Int, Int>> = HashMap()
    private var turn = 0
    private val playerOrder = ArrayList<Player>(playerClasses.size)

    init {
        val startPositions = GameFieldFactory.getStartPosition(playerClasses.size)
        playerClasses.forEachIndexed { i, e ->
            val currentPosition = startPositions[i]
            playerOrder.add(e)
            playerCoordinates[e] = currentPosition
            gameField[currentPosition.first][currentPosition.second].addMapObject(e)
        }

        initPlayerMoves()
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
        return gameField[coordinates.first][coordinates.second].interactiveObjects
    }

    fun activateInteractiveObject(player: Player, interactiveObject: InteractiveObject) {
        if (player != currentPlayer()) throw IllegalTurnOrderException(player)

        val coordinates = playerCoordinates[player] ?: throw NoSuchPlayerException(player)
        if (gameField[coordinates.first][coordinates.second].interactiveObjects.contains(interactiveObject))
            interactiveObject.interact(player)
        else
            throw NoSuchInteractiveObjectException(interactiveObject)
    }

    fun getInteractiveObjectDescription(player: Player, interactiveObject: InteractiveObject): String {
        if (player != currentPlayer()) throw IllegalTurnOrderException(player)

        val coordinates = playerCoordinates[player] ?: throw NoSuchPlayerException(player)
        if (gameField[coordinates.first][coordinates.second].interactiveObjects.contains(interactiveObject))
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

    fun movePlayer(player: Player, x: Int, y: Int) {
        if (player != currentPlayer()) throw IllegalTurnOrderException(player)

        val playerCoordinates = playerCoordinates[player] ?: throw NoSuchPlayerException(player)
        val playerMoves = this.playerMoves[player] ?: throw NoSuchPlayerException(player)
        if (!checkMove(player, x, y)) throw IllegalMoveException(player, x, y)

        gameField[playerCoordinates.first][playerCoordinates.second].removeMapObject(player)
        player.energy -= playerMoves[x][y]
        gameField[x][y].addMapObject(player)
        this.playerCoordinates[player] = Pair(x, y)
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

        val matrix = playerMoves[player] ?: throw NoSuchPlayerException(player)
        for (x in gameField.indices) {
            for (y in gameField[0].indices) {
                if (!gameField[x][y].isPassable)
                    matrix[x][y] = -1
                else if (gameField[x][y].hasUnit)
                    matrix[x][y] = -2
                else
                    matrix[x][y] = 0
            }
        }
        return matrix
    }

    /**
     * Updates player's moveMatrix
     * @param player, which path you want to update
     */
    fun updatePlayersMoveMatrix(player: Player) {
        val moves = setMatrixBase(player)
        val coordinates = playerCoordinates[player] ?: throw NoSuchPlayerException(player)
        val UNIT = -2
        val PASSABLE = 0

        val x = coordinates.first
        val y = coordinates.second
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
    fun getPlayersPath(player: Player, x: Int, y: Int): ArrayList<Pair<Int, Int>> {
        val moves = ArrayList<Pair<Int, Int>>()
        val coordinates = playerCoordinates[player] ?: throw NoSuchPlayerException(player)
        val moveMatrix = playerMoves[player] ?: throw NoSuchPlayerException(player)
        if (moveMatrix[x][y] <= 0) return moves
        val startX = coordinates.first
        val startY = coordinates.second
        var curX = x
        var curY = y
        while (curX != startX && curY != startY) {
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
                        moves.add(Pair(i, j))
                        curX = i
                        curY = j
                        break@outerLoop
                    }
                }
            }
        }
        return moves
    }
}
