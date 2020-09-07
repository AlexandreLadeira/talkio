package com.alelad.talkio.user.service

import com.alelad.talkio.commons.model.UserId
import com.alelad.talkio.user.service.model.User

class UserService {

    suspend fun create(email: String): User {
        return User(
            id = UserId.randomUUID(),
            nickname = "alelad",
            email = email,
            name = "Alexandre",
            lastname = "Ladeira"
        )
    }

    suspend fun fetch(userId: UserId): User {
        TODO("$userId")
    }


}