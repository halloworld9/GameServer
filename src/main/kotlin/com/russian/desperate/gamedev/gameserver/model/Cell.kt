package com.russian.desperate.gamedev.gameserver.model

import com.russian.desperate.gamedev.gameserver.model.mapobjects.structures.InteractiveObject
import com.russian.desperate.gamedev.gameserver.model.mapobjects.structures.MapObject
import com.russian.desperate.gamedev.gameserver.model.mapobjects.units.Unit
class Cell(vararg mapObjects: MapObject) {
    private val mapObjects = HashSet<MapObject>()
    val interactiveObjects = HashSet<InteractiveObject>()
    var isPassable = true
        private set
    var hasUnit = false
        private set

    init {
        for (i in mapObjects) {
            this.mapObjects.add(i)
            if (!i.isPassable)
                isPassable = false
            else if (i is Unit)
                hasUnit = true
            else if (i is InteractiveObject)
                interactiveObjects.add(i)
        }
    }

    fun removeMapObject(mapObject: MapObject): Boolean {
        val isRemoved = mapObjects.remove(mapObject)
        if (isRemoved && mapObject is InteractiveObject)
            interactiveObjects.remove(mapObject)
        if (isRemoved && !mapObject.isPassable)
            updateIsPassable()
        else if (isRemoved && mapObject is Unit)
            updateHasUnit()
        return isRemoved
    }

    fun addMapObject(mapObject: MapObject): Boolean {
        val isAdded = mapObjects.add(mapObject)
        if (isAdded && mapObject is InteractiveObject)
            interactiveObjects.add(mapObject)
        if (isAdded && !mapObject.isPassable)
            isPassable = false
        else if (isAdded && mapObject is Unit)
            hasUnit = true

        return isAdded
    }

    private fun updateIsPassable() {
        for (i in mapObjects)
            if (!i.isPassable) {
                isPassable = false
                return
            }
        isPassable = true
    }

    private fun updateHasUnit() {
        for (i in mapObjects)
            if (i is Unit) {
                hasUnit = true
                return
            }
        isPassable = false
    }

}