package com.russian.desperate.gamedev.gameserver.model

import com.russian.desperate.gamedev.gameserver.model.mapobjects.structures.MapObject
import java.io.Serializable

class Cell(mapObject: MapObject) : Serializable {
    val mapObjects = HashSet<MapObject>()

    init {
        mapObjects.add(mapObject)
    }
    fun removeMapObject(mapObject: MapObject): Boolean {
        return mapObjects.remove(mapObject)
    }
}