package com.russian.desperate.gamedev.gameserver.model.exceptions

import com.russian.desperate.gamedev.gameserver.model.dto.User

class UserLoosedException(val user: User) : RuntimeException("$user loosed")