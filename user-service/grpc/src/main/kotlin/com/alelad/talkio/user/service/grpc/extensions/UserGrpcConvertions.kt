package com.alelad.talkio.user.service.grpc.extensions

import com.alelad.talkio.user.service.grpc.UserResponse
import com.alelad.talkio.user.service.model.User

fun User.toUserResponse(): UserResponse {
    return UserResponse.newBuilder()
        .setId(id.toString())
        .setNickname(nickname)
        .setEmail(email)
        .setName(name)
        .setLastname(lastname)
        .build()
}