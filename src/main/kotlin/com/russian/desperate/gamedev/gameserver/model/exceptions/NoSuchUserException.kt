package com.russian.desperate.gamedev.gameserver.model.exceptions

import com.russian.desperate.gamedev.gameserver.model.dto.User

class NoSuchUserException(user: User) : IllegalArgumentException("There is no user $user in the game")