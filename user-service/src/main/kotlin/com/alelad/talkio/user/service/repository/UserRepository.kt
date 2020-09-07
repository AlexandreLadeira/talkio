package com.alelad.talkio.user.service.repository

import com.alelad.talkio.commons.model.UserId
import com.alelad.talkio.user.service.model.AuthenticationPlatform
import com.alelad.talkio.user.service.model.User

interface UserRepository {

    suspend fun fetchById(userId: UserId): User

    suspend fun fetchByAuthenticationPlatformId(
        id: String,
        platform: AuthenticationPlatform
    ): User
}