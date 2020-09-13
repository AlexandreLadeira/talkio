package com.alelad.talkio.user.service

import com.alelad.talkio.commons.exceptions.EntityNotFoundException
import com.alelad.talkio.commons.model.UserId
import com.alelad.talkio.user.service.model.AuthenticationPlatform
import com.alelad.talkio.user.service.model.User
import com.alelad.talkio.user.service.repository.UserRepository

class UserService(/*private val repository: UserRepository*/) {

    suspend fun create(email: String): User {

        val u =  User(
            id = UserId.randomUUID(),
            nickname = "alelad",
            email = email,
            name = "Alexandre",
            lastname = "Ladeira"
        )

        throw EntityNotFoundException("User not found $u")
    }

//    suspend fun fetchById(userId: UserId): User = TODO()
//        repository.fetchById(userId)

//    suspend fun fetchByAuthenticationPlatform(id: String, platform: AuthenticationPlatform): User = TODO()
//        repository.fetchByAuthenticationPlatformId(id, platform)

}