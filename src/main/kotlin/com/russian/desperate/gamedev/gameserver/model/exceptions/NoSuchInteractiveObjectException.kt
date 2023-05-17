package com.russian.desperate.gamedev.gameserver.model.exceptions

import com.russian.desperate.gamedev.gameserver.model.mapobjects.structures.InteractiveObject

class NoSuchInteractiveObjectException(interactiveObject: InteractiveObject) : IllegalArgumentException("There is no $interactiveObject in current cell")